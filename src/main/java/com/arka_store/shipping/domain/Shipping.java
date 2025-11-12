package com.arka_store.shipping.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Shipping {
    private UUID id=UUID.randomUUID();
    private String orderId;
    private String userId;
    private String userEmail;
    private List<ShippingItem> items;
    private LocalDateTime createAt=LocalDateTime.now();
    private ShippingStatus status=ShippingStatus.PACKING;
    private String shippingAddress;
    private LocalDateTime sendingAt;
    private String timeInferenceToArrived;
    private LocalDateTime receivedAt;
    private LocalDateTime rejectedAt;


    public Shipping(String orderId, String userId, List<ShippingItem> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.shippingAddress = shippingAddress;
    }
    public void switchToSend(){
        if(this.status.equals(ShippingStatus.PACKING)){
            this.status=ShippingStatus.SEND;
        }
    }
    public void switchToArrived(){
        if(this.status.equals(ShippingStatus.SEND)){
            this.status=ShippingStatus.ARRIVED;
        }
    }
    public void switchToReturned(){
        if(this.status.equals(ShippingStatus.ARRIVED)){
            this.status=ShippingStatus.RETURNED;
        }
    }


}
