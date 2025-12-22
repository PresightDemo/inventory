package com.presight.inventory.domain.service;

import com.presight.common_lib.errorhandling.EntityNotFoundException;
import com.presight.common_lib.errorhandling.ValidationException;
import com.presight.inventory.config.InventoryConfigProperties;
import com.presight.inventory.domain.InventoryEntity;
import com.presight.inventory.domain.InventoryRequest;
import com.presight.inventory.domain.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.presight.inventory.domain.InventoryOperation.ADD;
import static com.presight.inventory.domain.InventoryOperation.DEDUCT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryConfigProperties inventoryConfigProperties;

    @InjectMocks
    private InventoryService inventoryService;

    // -------------------- createInventory --------------------

    @Test
    void shouldCreateInventorySuccessfully() {
        InventoryRequest request = new InventoryRequest(null,"IPHONE",100,ADD);

        InventoryEntity savedEntity = InventoryEntity.builder()
                .inventoryId(UUID.randomUUID())
                .inventoryName("IPHONE")
                .quantity(100)
                .build();

        when(inventoryRepository.save(any(InventoryEntity.class)))
                .thenReturn(savedEntity);

        InventoryEntity result = inventoryService.createInventory(request);

        assertNotNull(result);
        assertEquals("IPHONE", result.getInventoryName());
        assertEquals(100, result.getQuantity());
        verify(inventoryRepository).save(any());
    }

    // -------------------- isInventoryPresent --------------------

    @Test
    void shouldReturnTrueWhenInventoryExists() {
        UUID inventoryId = UUID.randomUUID();

        when(inventoryRepository.existsByInventoryId(inventoryId))
                .thenReturn(true);

        boolean present = inventoryService.isInventoryPresent(inventoryId.toString());

        assertTrue(present);
    }

    // -------------------- updateInventory (ADD) --------------------

    @Test
    void shouldAddInventoryQuantity() {
        UUID inventoryId = UUID.randomUUID();

        InventoryEntity existing = InventoryEntity.builder()
                .inventoryId(inventoryId)
                .inventoryName("MACBOOK")
                .quantity(50)
                .build();

        InventoryRequest request = new InventoryRequest(
                inventoryId,
                null,
                20,
                ADD
        );

        when(inventoryRepository.findByInventoryId(inventoryId))
                .thenReturn(Optional.of(existing));

        when(inventoryRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        InventoryEntity updated = inventoryService.updateInventory(request);

        assertEquals(70, updated.getQuantity());
    }

    // -------------------- updateInventory (DEDUCT) --------------------

    @Test
    void shouldDeductInventorySuccessfully() {
        UUID inventoryId = UUID.randomUUID();

        InventoryEntity existing = InventoryEntity.builder()
                .inventoryId(inventoryId)
                .inventoryName("TV")
                .quantity(100)
                .build();

        InventoryRequest request = new InventoryRequest(
                inventoryId,
                null,
                30,
                DEDUCT
        );

        when(inventoryRepository.findByInventoryId(inventoryId))
                .thenReturn(Optional.of(existing));

        when(inventoryRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(inventoryConfigProperties.getThresholdStock())
                .thenReturn(Map.of("TV", 20));

        InventoryEntity updated = inventoryService.updateInventory(request);

        assertEquals(70, updated.getQuantity());
    }

    // -------------------- updateInventory – insufficient inventory --------------------

    @Test
    void shouldThrowValidationExceptionWhenInventoryInsufficient() {
        UUID inventoryId = UUID.randomUUID();

        InventoryEntity existing = InventoryEntity.builder()
                .inventoryId(inventoryId)
                .inventoryName("PEPSI")
                .quantity(10)
                .build();

        InventoryRequest request = new InventoryRequest(
                inventoryId,
                "PEPSI",
                20,
                DEDUCT
        );

        when(inventoryRepository.findByInventoryId(inventoryId))
                .thenReturn(Optional.of(existing));

        assertThrows(ValidationException.class,
                () -> inventoryService.updateInventory(request));
    }

    // -------------------- updateInventory – inventory not found --------------------

    @Test
    void shouldThrowExceptionWhenInventoryNotFound() {
        UUID inventoryId = UUID.randomUUID();

        InventoryRequest request = new InventoryRequest(
                inventoryId,
                "ANY",
                10,
                ADD
        );

        when(inventoryRepository.findByInventoryId(inventoryId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> inventoryService.updateInventory(request));
    }

    // -------------------- getInventoryStock --------------------

    @Test
    void shouldReturnInventoryStock() {
        UUID inventoryId = UUID.randomUUID();

        InventoryEntity entity = InventoryEntity.builder()
                .inventoryId(inventoryId)
                .quantity(200)
                .inventoryName("PEPSI")
                .build();

        when(inventoryRepository.findByInventoryId(inventoryId))
                .thenReturn(Optional.of(entity));

        Integer stock = inventoryService.getInventoryStock(inventoryId.toString());

        assertEquals(200, stock);
    }

    @Test
    void shouldThrowExceptionWhenInventoryNotFoundForStock() {
        UUID inventoryId = UUID.randomUUID();

        when(inventoryRepository.findByInventoryId(inventoryId))
                .thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
                () -> inventoryService.getInventoryStock(inventoryId.toString()));
    }
}
