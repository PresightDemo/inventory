package com.presight.inventory.adapter.web.mapper;

import com.presight.inventory.adapter.web.model.CreateInventoryRequestDTO;
import com.presight.inventory.adapter.web.model.InventoryResourceDTO;
import com.presight.inventory.adapter.web.model.UpdateInventoryRequestDTO;
import com.presight.inventory.adapter.web.model.UpdateInventoryStockRequestDTO;
import com.presight.inventory.domain.InventoryEntity;
import com.presight.inventory.domain.InventoryOperation;
import com.presight.inventory.domain.InventoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryAPIMapper {
    public InventoryRequest toCreateInventoryRequest(CreateInventoryRequestDTO requestDTO) {

        return InventoryRequest.builder()
                .inventoryName(requestDTO.getInventoryName())
                .quantity(requestDTO.getQuantity())
                .build();
    }

    public InventoryResourceDTO toInventoryResourceDTO(InventoryEntity inventory) {
        return InventoryResourceDTO.builder()
                .inventoryId(inventory.getInventoryId())
                .inventoryName(inventory.getInventoryName())
                .quantity(inventory.getQuantity())
                .createdBy(inventory.getCreatedBy())
                .lastModifiedBy(inventory.getLastModifiedBy())
                .createdDate(inventory.getCreatedDate())
                .lastModifiedDate(inventory.getLastModifiedDate())
                .build();
    }

    public InventoryRequest toUpdateInventoryRequest(String inventoryId, UpdateInventoryRequestDTO requestDTO) {
        return InventoryRequest.builder()
                .inventoryId(UUID.fromString(inventoryId))
                .inventoryName(requestDTO.getInventoryName())
                .quantity(requestDTO.getQuantity())
                .inventoryOperation(InventoryOperation.valueOf(requestDTO.getInventoryOperationDTO().name()))
                .build();
    }

    public InventoryRequest toUpdateInventoryStockRequest(String inventoryId, UpdateInventoryStockRequestDTO requestDTO) {
        return InventoryRequest.builder()
                .inventoryId(UUID.fromString(inventoryId))
                .quantity(requestDTO.getQuantity())
                .inventoryOperation(InventoryOperation.valueOf(requestDTO.getInventoryOperationDTO().name()))
                .build();
    }
}
