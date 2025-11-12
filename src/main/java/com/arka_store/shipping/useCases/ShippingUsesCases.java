package com.arka_store.shipping.useCases;

import com.arka_store.shipping.domain.Shipping;

import java.util.List;
import java.util.UUID;

public interface ShippingUsesCases {

    Shipping save(Shipping shipping);
    Shipping findById(UUID id);
    List<Shipping> findAll();
    List<Shipping> findAllByUserId(String userId);



}
