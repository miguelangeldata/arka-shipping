package com.arka_store.shipping.infrastructure.feign;

import com.arka_store.shipping.infrastructure.web.resources.ReturnShippingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "PRODUCTS-SERVICE",path = "/returns")
public interface ReturnsClient {
    @PostMapping
    void returnsShipping(ReturnShippingRequest shippingRequest);

}
