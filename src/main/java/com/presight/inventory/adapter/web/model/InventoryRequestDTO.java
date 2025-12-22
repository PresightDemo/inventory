package com.presight.inventory.adapter.web.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDTO {
    private String inventoryId;
    private String inventoryName;
    private InventoryOperationDTO inventoryOperationDTO;
    @Min(0)
    private int quantity;
}
