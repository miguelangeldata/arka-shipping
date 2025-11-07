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
@Schema(description = "The main domain model representing a shipment and its lifecycle.")
public class Shipping {
    private UUID id=UUID.randomUUID();
    @Schema(description = "ID of the associated customer order.", example = "ORD-453789")
    private String orderId;
    @Schema(description = "ID of the customer who placed the order.", example = "USER-741")
    private String userId;
    @Schema(description = "Customer's email for shipment notifications.", example = "customer@example.com")
    private String userEmail;
    @Schema(description = "List of items included in the shipment.")
    private List<ShippingItem> items;
    @Schema(description = "Timestamp when the shipment was created/initiated.", example = "2025-11-06T20:00:00")
    private LocalDateTime createAt=LocalDateTime.now();
    @Schema(description = "Current status of the shipment (e.g., PACKING, SEND, ARRIVED).", example = "PACKING")
    private ShippingStatus status=ShippingStatus.PACKING;
    @Schema(description = "Final destination address for the shipment.", example = "123 Main St, Apt 4B, City")
    private String shippingAddress;
    @Schema(description = "Timestamp when the shipment was physically sent.", example = "2025-11-07T10:30:00")
    private LocalDateTime sendingAt;
    @Schema(description = "Estimated time until arrival (e.g., '3 days').", example = "3 days")
    private String timeInferenceToArrived;
    @Schema(description = "Timestamp when the customer received the shipment.", example = "2025-11-10T15:00:00")
    private LocalDateTime receivedAt;
    @Schema(description = "Timestamp when the shipment was returned/rejected.", example = "2025-11-12T09:00:00")
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
