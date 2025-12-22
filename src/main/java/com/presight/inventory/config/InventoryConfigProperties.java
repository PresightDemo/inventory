package com.presight.inventory.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "inventory")
public class InventoryConfigProperties {
    /**
     * Key: inventoryName
     * Value: quantity
     */
    private Map<String, Integer> thresholdStock;
}
