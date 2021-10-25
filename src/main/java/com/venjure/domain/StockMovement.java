package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StockMovement.
 */
@Entity
@Table(name = "stock_movement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StockMovement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "createdat", nullable = false)
    private Instant createdat;

    @NotNull
    @Column(name = "updatedat", nullable = false)
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    @Column(name = "type", length = 255, nullable = false)
    private String type;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Size(max = 255)
    @Column(name = "discriminator", length = 255, nullable = false)
    private String discriminator;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "product",
            "featuredasset",
            "taxcategory",
            "channels",
            "productVariants",
            "facetValues",
            "productOptions",
            "productVariantAssets",
            "productVariantPrices",
            "productVariantTranslations",
            "stockMovements",
        },
        allowSetters = true
    )
    private ProductVariant productvariant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "line", "refund", "fulfillments", "orderModifications", "stockMovements" }, allowSetters = true)
    private OrderItem orderitem;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "productvariant", "taxcategory", "featuredAsset", "jorder", "orderItems", "stockMovements" },
        allowSetters = true
    )
    private OrderLine orderline;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockMovement id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public StockMovement createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public StockMovement updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getType() {
        return this.type;
    }

    public StockMovement type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public StockMovement quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDiscriminator() {
        return this.discriminator;
    }

    public StockMovement discriminator(String discriminator) {
        this.discriminator = discriminator;
        return this;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public ProductVariant getProductvariant() {
        return this.productvariant;
    }

    public StockMovement productvariant(ProductVariant productVariant) {
        this.setProductvariant(productVariant);
        return this;
    }

    public void setProductvariant(ProductVariant productVariant) {
        this.productvariant = productVariant;
    }

    public OrderItem getOrderitem() {
        return this.orderitem;
    }

    public StockMovement orderitem(OrderItem orderItem) {
        this.setOrderitem(orderItem);
        return this;
    }

    public void setOrderitem(OrderItem orderItem) {
        this.orderitem = orderItem;
    }

    public OrderLine getOrderline() {
        return this.orderline;
    }

    public StockMovement orderline(OrderLine orderLine) {
        this.setOrderline(orderLine);
        return this;
    }

    public void setOrderline(OrderLine orderLine) {
        this.orderline = orderLine;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockMovement)) {
            return false;
        }
        return id != null && id.equals(((StockMovement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockMovement{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", type='" + getType() + "'" +
            ", quantity=" + getQuantity() +
            ", discriminator='" + getDiscriminator() + "'" +
            "}";
    }
}
