package com.presight.inventory.adapter.web.model;

public enum InventoryOperationDTO {
    ADD,        // Increase inventory (e.g., order cancelled, stock returned)
    DEDUCT      // Decrease inventory (e.g., order created)
}
