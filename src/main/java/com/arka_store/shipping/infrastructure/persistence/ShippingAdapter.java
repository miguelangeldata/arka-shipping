package com.arka_store.shipping.infrastructure.persistence;

import com.arka_store.shipping.domain.MetricsForShipping;
import com.arka_store.shipping.domain.Shipping;
import com.arka_store.shipping.infrastructure.ShippingMapper;
import com.arka_store.shipping.infrastructure.persistence.repositories.ShippingJpaRepository;
import com.arka_store.shipping.useCases.ShippingUsesCases;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShippingAdapter implements ShippingUsesCases {
    private final ShippingJpaRepository repository;
    private final ShippingMapper mapper;

    @Override
    public Shipping save(Shipping shipping) {
        return mapper.entityToDomain(repository.save(mapper.domainToEntity(shipping)));
    }

    @Override
    public Shipping findById(UUID id) {
        return repository.findById(id).map(mapper::entityToDomain)
                .orElseThrow(() -> new IllegalArgumentException("Shipping not FInd By Id" + id));
    }

    @Override
    public List<Shipping> findAll() {
        return repository.findAll().stream().map(mapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Shipping> findAllByUserId(String userId) {
        return repository.findAllByUserId(userId).stream().map(mapper::entityToDomain)
                .collect(Collectors.toList());
    }
}
