package com.arka_store.shipping.application;

import com.arka_store.shipping.domain.MetricsForShipping;
import com.arka_store.shipping.domain.Shipping;
import com.arka_store.shipping.domain.ShippingStatus;
import com.arka_store.shipping.domain.events.ShippingSendEvent;
import com.arka_store.shipping.domain.utils.RandomShippingDateGenerator;
import com.arka_store.shipping.infrastructure.feign.ReturnsClient;
import com.arka_store.shipping.infrastructure.web.resources.ReturnShippingRequest;
import com.arka_store.shipping.infrastructure.web.resources.ShippingRequest;
import com.arka_store.shipping.useCases.Publisher;
import com.arka_store.shipping.useCases.ShippingUsesCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class
ShippingService {
    private  final ShippingUsesCases usesCases;
    private final ReturnsClient returnsClient;
    private final Publisher publisher;

    public Shipping createShipping(ShippingRequest request){

        Shipping shipping=new Shipping(request.orderId(), request.userId(),request.items());
        return usesCases.save(shipping);
    }
    public StringBuilder sendShipping(UUID id,String address,String userEmail){
        Shipping shipping=getById(id);
        LocalDateTime sendingAt=LocalDateTime.now();
        String estimateArrived= RandomShippingDateGenerator.generateAleatoryDate();
        shipping.setShippingAddress(address);
        shipping.setUserEmail(userEmail);
        shipping.setTimeInferenceToArrived(estimateArrived);
        shipping.setSendingAt(sendingAt);
        shipping.switchToSend();
        Shipping savedShipping=usesCases.save(shipping);
        StringBuilder response=new StringBuilder("Shipping was Sending for "+savedShipping.getUserId());
        response.append("Time calculated To arrived : ").append(savedShipping.getTimeInferenceToArrived());
        ShippingSendEvent event=new ShippingSendEvent(savedShipping.getUserEmail(),savedShipping.getUserId(),savedShipping.getTimeInferenceToArrived());
        publisher.ShippingSendEvent(event);
        return response;
    }
    public Shipping getById(UUID id){
        return usesCases.findById(id);
    }
    public void arrivedShipping(UUID id){
        LocalDateTime arrivedAt=LocalDateTime.now();
        Shipping shipping=getById(id);
        shipping.switchToArrived();
        shipping.setReceivedAt(arrivedAt);
        usesCases.save(shipping);
    }
    public void returnShipping(UUID id,String reason){
        Shipping shipping=getById(id);
        ReturnShippingRequest request=buildShippingRequest(shipping);
        request.setReason(reason);
        shipping.switchToReturned();
        returnsClient.returnsShipping(request);
        usesCases.save(shipping);

    }
    public List<Shipping> getAllShippingByUserId(String userId){
        return usesCases.findAllByUserId(userId);
    }
    public List<Shipping> getAllShipping(){
        return usesCases.findAll();
    }
    private ReturnShippingRequest buildShippingRequest(Shipping shipping){
        return ReturnShippingRequest.builder()
                .userId(shipping.getUserId())
                .userEmail(shipping.getUserEmail())
                .items(shipping.getItems())
                .build();
    }
    public MetricsForShipping getMetricsForShipping(){
        List<Shipping>shippings=getAllShipping();
        Integer totalShippings=totalShipping(shippings);
        Integer totalShippingsSent=totalShippingSent(shippings);
        Integer totalShippingReceived=totalShippingReceived(shippings);
        Integer totalShippingReturned=totalShippingReturned(shippings);
        return new MetricsForShipping(totalShippings,totalShippingsSent,totalShippingReceived,totalShippingReturned);
    }
    private Integer totalShipping(List<Shipping> shippings){
        return shippings.size();
    }
    private Integer totalShippingSent(List<Shipping> shippings){
        return Math.toIntExact(shippings.stream()
                .filter(shipping -> shipping.getStatus().equals(ShippingStatus.SEND)).count());
    }
    private Integer totalShippingReceived(List<Shipping> shippings){
        return Math.toIntExact(shippings.stream()
                .filter(shipping -> shipping.getStatus().equals(ShippingStatus.ARRIVED)).count());
    }

    private Integer totalShippingReturned(List<Shipping> shippings){
        return Math.toIntExact(shippings.stream()
                .filter(shipping -> shipping.getStatus().equals(ShippingStatus.RETURNED)).count());
    }

}

