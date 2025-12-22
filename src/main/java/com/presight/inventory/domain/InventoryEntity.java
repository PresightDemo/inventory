package com.presight.inventory.domain;

import com.presight.common_lib.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name= "T_INVENTORY")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class InventoryEntity  extends BaseEntity implements Serializable {

    @Column(name = "INVENTORY_ID")
    @NonNull
    private UUID inventoryId;

    @Column(name = "INVENTORY_NAME")
    @NonNull
    private String inventoryName;

    @Column(name = "QUANTITY")
    private int quantity;

}
