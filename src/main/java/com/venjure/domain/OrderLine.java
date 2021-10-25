package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderLine.
 */
@Entity
@Table(name = "order_line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderLine implements Serializable {

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
    @JsonIgnoreProperties(value = { "taxRates" }, allowSetters = true)
    private TaxCategory taxcategory;

    @ManyToOne
    private Asset featuredAsset;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "customer",
            "channels",
            "promotions",
            "historyEntries",
            "orderLines",
            "orderModifications",
            "payments",
            "shippingLines",
            "surcharges",
        },
        allowSetters = true
    )
    private Jorder jorder;

    @OneToMany(mappedBy = "line")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "line", "refund", "fulfillments", "orderModifications", "stockMovements" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "orderline")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productvariant", "orderitem", "orderline" }, allowSetters = true)
    private Set<StockMovement> stockMovements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderLine id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public OrderLine createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public OrderLine updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public ProductVariant getProductvariant() {
        return this.productvariant;
    }

    public OrderLine productvariant(ProductVariant productVariant) {
        this.setProductvariant(productVariant);
        return this;
    }

    public void setProductvariant(ProductVariant productVariant) {
        this.productvariant = productVariant;
    }

    public TaxCategory getTaxcategory() {
        return this.taxcategory;
    }

    public OrderLine taxcategory(TaxCategory taxCategory) {
        this.setTaxcategory(taxCategory);
        return this;
    }

    public void setTaxcategory(TaxCategory taxCategory) {
        this.taxcategory = taxCategory;
    }

    public Asset getFeaturedAsset() {
        return this.featuredAsset;
    }

    public OrderLine featuredAsset(Asset asset) {
        this.setFeaturedAsset(asset);
        return this;
    }

    public void setFeaturedAsset(Asset asset) {
        this.featuredAsset = asset;
    }

    public Jorder getJorder() {
        return this.jorder;
    }

    public OrderLine jorder(Jorder jorder) {
        this.setJorder(jorder);
        return this;
    }

    public void setJorder(Jorder jorder) {
        this.jorder = jorder;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public OrderLine orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public OrderLine addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setLine(this);
        return this;
    }

    public OrderLine removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setLine(null);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setLine(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setLine(this));
        }
        this.orderItems = orderItems;
    }

    public Set<StockMovement> getStockMovements() {
        return this.stockMovements;
    }

    public OrderLine stockMovements(Set<StockMovement> stockMovements) {
        this.setStockMovements(stockMovements);
        return this;
    }

    public OrderLine addStockMovement(StockMovement stockMovement) {
        this.stockMovements.add(stockMovement);
        stockMovement.setOrderline(this);
        return this;
    }

    public OrderLine removeStockMovement(StockMovement stockMovement) {
        this.stockMovements.remove(stockMovement);
        stockMovement.setOrderline(null);
        return this;
    }

    public void setStockMovements(Set<StockMovement> stockMovements) {
        if (this.stockMovements != null) {
            this.stockMovements.forEach(i -> i.setOrderline(null));
        }
        if (stockMovements != null) {
            stockMovements.forEach(i -> i.setOrderline(this));
        }
        this.stockMovements = stockMovements;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderLine)) {
            return false;
        }
        return id != null && id.equals(((OrderLine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderLine{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            "}";
    }
}
