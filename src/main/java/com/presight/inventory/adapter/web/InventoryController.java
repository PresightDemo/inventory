package com.presight.inventory.adapter.web;


import com.presight.inventory.adapter.web.model.CreateInventoryRequestDTO;
import com.presight.inventory.adapter.web.model.InventoryResourceDTO;
import com.presight.inventory.adapter.web.model.UpdateInventoryRequestDTO;
import com.presight.inventory.adapter.web.model.UpdateInventoryStockRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryAPIService inventoryAPIService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryResourceDTO> createInventory(@RequestBody CreateInventoryRequestDTO request) {
        return inventoryAPIService.createInventory(request);

    }

    @PatchMapping("/udpate/{inventoryId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InventoryResourceDTO> updateInventory(@PathVariable String inventoryId,
                                                                @RequestBody UpdateInventoryRequestDTO requestDTO) {
        return  inventoryAPIService.updateInventory(inventoryId, requestDTO);
    }

    @PatchMapping("/udpateStock/{inventoryId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InventoryResourceDTO> updateInventoryStock(@PathVariable String inventoryId,
                                                                     @RequestBody UpdateInventoryStockRequestDTO requestDTO) {
        return  inventoryAPIService.updateInventoryStock(inventoryId, requestDTO);
    }

    /**
     * Get current stock for a inventory
     */
    @GetMapping("/stock/{inventoryId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> getStock(@PathVariable String inventoryId) {
        return  inventoryAPIService.getInventoryStock(inventoryId);
    }
}
