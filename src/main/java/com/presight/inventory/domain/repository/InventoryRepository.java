package com.presight.inventory.domain.repository;

import com.presight.inventory.domain.InventoryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository  extends JpaRepository<InventoryEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<InventoryEntity> findByInventoryId(UUID inventoryId);

    boolean existsByInventoryId(UUID uuid);
}
