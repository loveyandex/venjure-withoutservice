package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.StockMovement} entity.
 */
public class StockMovementDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String type;

    @NotNull
    private Integer quantity;

    @NotNull
    @Size(max = 255)
    private String discriminator;

    private ProductVariantDTO productvariant;

    private OrderItemDTO orderitem;

    private OrderLineDTO orderline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public ProductVariantDTO getProductvariant() {
        return productvariant;
    }

    public void setProductvariant(ProductVariantDTO productvariant) {
        this.productvariant = productvariant;
    }

    public OrderItemDTO getOrderitem() {
        return orderitem;
    }

    public void setOrderitem(OrderItemDTO orderitem) {
        this.orderitem = orderitem;
    }

    public OrderLineDTO getOrderline() {
        return orderline;
    }

    public void setOrderline(OrderLineDTO orderline) {
        this.orderline = orderline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockMovementDTO)) {
            return false;
        }

        StockMovementDTO stockMovementDTO = (StockMovementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockMovementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockMovementDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", type='" + getType() + "'" +
            ", quantity=" + getQuantity() +
            ", discriminator='" + getDiscriminator() + "'" +
            ", productvariant=" + getProductvariant() +
            ", orderitem=" + getOrderitem() +
            ", orderline=" + getOrderline() +
            "}";
    }
}
