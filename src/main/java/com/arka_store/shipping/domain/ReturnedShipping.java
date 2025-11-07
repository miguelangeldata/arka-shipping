package com.arka_store.shipping.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ReturnedShipping {
    private UUID id=UUID.randomUUID();
    @Schema(description = "Reason provided for the return.", example = "Wrong item received.")
    private String reason;

    @Schema(description = "List of specific items returned.")
    private List<ShippingItem> items;

    @Schema(description = "Timestamp when the return was logged.", example = "2025-11-12T09:05:00")
    private LocalDateTime returnedAt;

    public ReturnedShipping(String reason, List<ShippingItem> items) {
        this.reason = reason;
        this.items = items;
    }
}
