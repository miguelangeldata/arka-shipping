package com.arka_store.shipping.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class MetricsForShipping {
    private Integer totalOfShipping;
    private Integer totalSend;
    private Integer totalReceived;
    private Integer totalReturned;
}
