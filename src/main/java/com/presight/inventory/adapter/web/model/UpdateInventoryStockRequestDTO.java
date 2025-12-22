package com.presight.inventory.adapter.web.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInventoryStockRequestDTO {

    private InventoryOperationDTO inventoryOperationDTO;
    @Min(0)
    private int quantity;
}
