package com.arka_store.shipping.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Details of an individual product within the shipment order.")
public class ShippingItem {
    @Schema(description = "Line item ID from the original order.", example = "201")
    private Long itemId;

    @Schema(description = "Product ID from the catalog.", example = "1005")
    private Long productId;

    @Schema(description = "Quantity of the product being shipped.", example = "2")
    private Integer quantity;;
}
