package com.presight.inventory.adapter.web.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class InventoryResourceDTO {

    private UUID inventoryId;    // Product ID
    private String inventoryName;    // Product ID
    private int quantity;      // Current stock quantity

    // Optional: Audit info
    private String createdBy;
    private String lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
