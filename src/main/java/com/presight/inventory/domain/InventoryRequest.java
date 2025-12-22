package com.presight.inventory.domain;

import com.presight.inventory.adapter.web.model.InventoryOperationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Builder
@Accessors(fluent = true)
@AllArgsConstructor
public class InventoryRequest {

    private UUID inventoryId;
    private String inventoryName;
    private Integer quantity;
    private InventoryOperation inventoryOperation;
}
