package com.presight.inventory.adapter.web;

import com.presight.common_lib.errorhandling.EntityNotFoundException;
import com.presight.common_lib.errorhandling.ErrorDTO;
import com.presight.inventory.adapter.web.mapper.InventoryAPIMapper;
import com.presight.inventory.adapter.web.model.CreateInventoryRequestDTO;
import com.presight.inventory.adapter.web.model.InventoryResourceDTO;
import com.presight.inventory.adapter.web.model.UpdateInventoryRequestDTO;
import com.presight.inventory.adapter.web.model.UpdateInventoryStockRequestDTO;
import com.presight.inventory.domain.InventoryEntity;
import com.presight.inventory.domain.InventoryRequest;
import com.presight.inventory.domain.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.presight.common_lib.domain.validator.NotNullValidator.validateNotNull;
import static com.presight.common_lib.errorhandling.model.CommonErrorType.INVENTORY_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryAPIService {

    private final InventoryAPIMapper inventoryAPIMapper;
    private final InventoryService inventoryService;

    public ResponseEntity<InventoryResourceDTO> createInventory(CreateInventoryRequestDTO requestDTO) {

        log.info("handling inventory creation");
        validateNotNull(requestDTO.getInventoryName());
        validateNotNull(requestDTO.getQuantity());

        InventoryRequest inventoryRequest = inventoryAPIMapper.toCreateInventoryRequest(requestDTO);
        InventoryEntity inventory = inventoryService.createInventory(inventoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryAPIMapper.toInventoryResourceDTO(inventory));
    }

    public ResponseEntity<InventoryResourceDTO> updateInventory(String inventoryId, UpdateInventoryRequestDTO requestDTO) {

        log.info("handling modification of inventory Id: {}", inventoryId);

        if (inventoryService.isInventoryPresent(inventoryId)) {
            InventoryRequest inventoryRequest = inventoryAPIMapper.toUpdateInventoryRequest(inventoryId, requestDTO);
            InventoryEntity inventory = inventoryService.updateInventory(inventoryRequest);
            return ResponseEntity.ok(inventoryAPIMapper.toInventoryResourceDTO(inventory));
        }else {
            log.info("Inventory not found with inventoryId : {}",inventoryId);
            throw new EntityNotFoundException( new ErrorDTO(INVENTORY_NOT_EXIST));
        }
    }

    public ResponseEntity<Integer> getInventoryStock(String inventoryId) {
        log.info("handling retrieval of inventory with inventoryId: {}", inventoryId);
        int stock = inventoryService.getInventoryStock(inventoryId);
        return ResponseEntity.ok(stock);

    }

    public ResponseEntity<InventoryResourceDTO> updateInventoryStock(String inventoryId, UpdateInventoryStockRequestDTO requestDTO) {
        log.info("handling modification of inventory stock for inventoryId: {}", inventoryId);

        if (inventoryService.isInventoryPresent(inventoryId)) {
            InventoryRequest inventoryRequest = inventoryAPIMapper.toUpdateInventoryStockRequest(inventoryId, requestDTO);
            InventoryEntity inventory = inventoryService.updateInventory(inventoryRequest);
            return ResponseEntity.ok(inventoryAPIMapper.toInventoryResourceDTO(inventory));
        }else {
            log.info("Inventory not found with inventoryId : {}",inventoryId);
            throw new EntityNotFoundException( new ErrorDTO(INVENTORY_NOT_EXIST));
        }
    }
}
