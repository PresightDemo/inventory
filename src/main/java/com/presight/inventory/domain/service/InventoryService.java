package com.presight.inventory.domain.service;

import com.presight.common_lib.errorhandling.EntityNotFoundException;
import com.presight.common_lib.errorhandling.ErrorDTO;
import com.presight.common_lib.errorhandling.ValidationException;
import com.presight.inventory.config.InventoryConfigProperties;
import com.presight.inventory.domain.InventoryEntity;
import com.presight.inventory.domain.InventoryRequest;
import com.presight.inventory.domain.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.presight.common_lib.errorhandling.model.CommonErrorType.INSUFFICIENT_INVENTORY;
import static com.presight.common_lib.errorhandling.model.CommonErrorType.INVENTORY_NOT_EXIST;
import static com.presight.inventory.domain.InventoryOperation.ADD;
import static com.presight.inventory.domain.InventoryOperation.DEDUCT;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final InventoryConfigProperties inventoryThresholdConfig;

    public InventoryEntity createInventory(InventoryRequest inventoryRequest) {

        log.info("Attempting to create a new inventory");
        InventoryEntity inventory = InventoryEntity.builder()
                .inventoryId(UUID.randomUUID())
                .inventoryName(inventoryRequest.inventoryName())
                .quantity(inventoryRequest.quantity())
                .build();

        return inventoryRepository.save(inventory);

    }

    public boolean isInventoryPresent(String inventoryId) {
        return inventoryRepository.existsByInventoryId(UUID.fromString(inventoryId));
    }

    public InventoryEntity updateInventory(InventoryRequest inventoryRequest) {
        UUID inventoryId = inventoryRequest.inventoryId();
        log.info("Attempting to modify inventory with inventoryId ; {}", inventoryId);

        Optional<InventoryEntity> existingInventory = inventoryRepository.findByInventoryId(inventoryId);
        if(existingInventory.isPresent()){
            return modifyAndSaveInventory(existingInventory.get(), inventoryRequest);
        }
        else {
            log.error("Failed to update Inventory. No inventory present with inventoryId : {}", inventoryId);
            throw new EntityNotFoundException(new ErrorDTO(INVENTORY_NOT_EXIST));
        }
    }

    private InventoryEntity modifyAndSaveInventory(InventoryEntity inventory, InventoryRequest inventoryRequest) {

        if(inventoryRequest.inventoryName() != null){
            inventory.setInventoryName(inventoryRequest.inventoryName());
        }
        inventory.setQuantity(getNetQuantity(inventory, inventoryRequest));
        return inventoryRepository.save(inventory);
    }

    private int getNetQuantity(InventoryEntity inventory, InventoryRequest inventoryRequest) {

        int existingQuantity = inventory.getQuantity();
        String inventoryName = inventory.getInventoryName();
        int netQuantity = 0;

        if (inventoryRequest.inventoryOperation() == ADD) {
            netQuantity = existingQuantity + inventoryRequest.quantity();

        } else  if (inventoryRequest.inventoryOperation() == DEDUCT) {

             netQuantity = existingQuantity - inventoryRequest.quantity();
            quantitySufficientCheck(netQuantity);

             if( inventoryThresholdConfig.getThresholdStock().containsKey(inventoryName)){
                 if(netQuantity<= inventoryThresholdConfig.getThresholdStock().get(inventoryName)){
                     log.warn("Current stock of {} is {} and is below the markedThreashold", inventoryName,netQuantity );
                 }
             }else{
                 log.warn("Threshold is not set for inventory Name: {}, inventoryId: {} in config",
                         inventoryName,inventory.getInventoryId());
             }
            return netQuantity;
        }
        return netQuantity;
    }

    private void quantitySufficientCheck(int netQuantity) {
        if(netQuantity < 0){
            throw new ValidationException(new ErrorDTO(INSUFFICIENT_INVENTORY,
                    "Insufficient Quantity"));
        }
    }

    public Integer getInventoryStock(String inventoryId) {
       return inventoryRepository.findByInventoryId(UUID.fromString(inventoryId))
                .map(InventoryEntity::getQuantity)
                .orElseThrow(() -> new ValidationException(new ErrorDTO(INVENTORY_NOT_EXIST)));


    }
}
