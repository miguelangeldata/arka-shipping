package com.arka_store.shipping.infrastructure.web.controllers;

import com.arka_store.shipping.application.ShippingService;
import com.arka_store.shipping.domain.Shipping;
import com.arka_store.shipping.infrastructure.web.resources.ShippingRequest;
import com.arka_store.shipping.infrastructure.web.resources.ShippingResponse;
import com.arka_store.shipping.infrastructure.web.resources.ShippingReturnsReason;
import com.arka_store.shipping.infrastructure.web.resources.UserInfoForShipping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
@Tag(name = "Shipping Management", description = "Operations related to creating, sending, tracking, and returning shipments.")
public class ShippingController {
    private final ShippingService service;

    @Operation(
            summary = "Create a New Shipment",
            description = "Registers a new shipment order, typically in a Packing status."
    )
    @ApiResponse(responseCode = "200", description = "Shipment successfully created")
    @PostMapping
    public ResponseEntity<Shipping> createShipping(@RequestBody ShippingRequest request){
        Shipping shipping=service.createShipping(request);
        return ResponseEntity.ok(shipping);
    }
    @Operation(
            summary = "Process and Send Shipment",
            description = "Updates the shipment status to SENT/SHIPPED and records the final destination address and user email."
    )
    @ApiResponse(responseCode = "200", description = "Shipment processed and marked as SENT")
    @ApiResponse(responseCode = "404", description = "Shipment not found", content = @Content(schema = @Schema(hidden = true)))
    @PostMapping("/send/{shippingId}")
    public ResponseEntity<ShippingResponse> sendShipping(
            @PathVariable("shippingId") UUID shippingId,
            @RequestBody UserInfoForShipping infoForShipping){
        StringBuilder message=service.sendShipping(shippingId,infoForShipping.address(),infoForShipping.userEmail());
        ShippingResponse response=new ShippingResponse(message);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "Mark Shipment as Arrived/Received",
            description = "Updates the shipment status to RECEIVED by the end user."
    )
    @ApiResponse(responseCode = "200", description = "Shipment successfully marked as RECEIVED")
    @PutMapping("/arrived/{shippingId}")
    public ResponseEntity<ShippingResponse> arrivedShipping(@PathVariable("shippingId") UUID shippingId){
        service.arrivedShipping(shippingId);
        StringBuilder message=new StringBuilder("The User Received the Shipping Successfully");
        ShippingResponse response=new ShippingResponse(message);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "Process Shipment Return",
            description = "Updates the shipment status to RETURNED and logs the reason for the return."
    )
    @ApiResponse(responseCode = "200", description = "Shipment successfully marked as RETURNED")
    @PostMapping("/return/{shippingId}")
    public ResponseEntity<ShippingResponse> returnShipping(
            @PathVariable("shippingId")UUID shippingId,
            @RequestBody ShippingReturnsReason shippingReturnsReason){
        service.returnShipping(shippingId,shippingReturnsReason.reason());
        StringBuilder message=new StringBuilder("The Shipping was Returned Successfully");
        ShippingResponse response=new ShippingResponse(message);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "Get All Shipments",
            description = "Retrieves a list of all shipments registered in the system."
    )
    @ApiResponse(responseCode = "200", description = "List of shipments returned successfully")
    @GetMapping
    public ResponseEntity<List<Shipping>> getAll(){
        List<Shipping> shippingList=service.getAllShipping();
        return ResponseEntity.ok(shippingList);
    }





}
