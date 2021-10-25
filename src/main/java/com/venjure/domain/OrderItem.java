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
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderItem implements Serializable {

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

    @Column(name = "initiallistprice")
    private Integer initiallistprice;

    @NotNull
    @Column(name = "listprice", nullable = false)
    private Integer listprice;

    @NotNull
    @Column(name = "listpriceincludestax", nullable = false)
    private Boolean listpriceincludestax;

    @NotNull
    @Size(max = 255)
    @Column(name = "adjustments", length = 255, nullable = false)
    private String adjustments;

    @NotNull
    @Size(max = 255)
    @Column(name = "taxlines", length = 255, nullable = false)
    private String taxlines;

    @NotNull
    @Column(name = "cancelled", nullable = false)
    private Boolean cancelled;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "productvariant", "taxcategory", "featuredAsset", "jorder", "orderItems", "stockMovements" },
        allowSetters = true
    )
    private OrderLine line;

    @ManyToOne
    @JsonIgnoreProperties(value = { "payment", "orderItems" }, allowSetters = true)
    private Refund refund;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_order_item__fulfillment",
        joinColumns = @JoinColumn(name = "order_item_id"),
        inverseJoinColumns = @JoinColumn(name = "fulfillment_id")
    )
    @JsonIgnoreProperties(value = { "orderItems" }, allowSetters = true)
    private Set<Fulfillment> fulfillments = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_order_item__order_modification",
        joinColumns = @JoinColumn(name = "order_item_id"),
        inverseJoinColumns = @JoinColumn(name = "order_modification_id")
    )
    @JsonIgnoreProperties(value = { "payment", "refund", "jorder", "surcharges", "orderItems" }, allowSetters = true)
    private Set<OrderModification> orderModifications = new HashSet<>();

    @OneToMany(mappedBy = "orderitem")
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

    public OrderItem id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public OrderItem createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public OrderItem updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Integer getInitiallistprice() {
        return this.initiallistprice;
    }

    public OrderItem initiallistprice(Integer initiallistprice) {
        this.initiallistprice = initiallistprice;
        return this;
    }

    public void setInitiallistprice(Integer initiallistprice) {
        this.initiallistprice = initiallistprice;
    }

    public Integer getListprice() {
        return this.listprice;
    }

    public OrderItem listprice(Integer listprice) {
        this.listprice = listprice;
        return this;
    }

    public void setListprice(Integer listprice) {
        this.listprice = listprice;
    }

    public Boolean getListpriceincludestax() {
        return this.listpriceincludestax;
    }

    public OrderItem listpriceincludestax(Boolean listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
        return this;
    }

    public void setListpriceincludestax(Boolean listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
    }

    public String getAdjustments() {
        return this.adjustments;
    }

    public OrderItem adjustments(String adjustments) {
        this.adjustments = adjustments;
        return this;
    }

    public void setAdjustments(String adjustments) {
        this.adjustments = adjustments;
    }

    public String getTaxlines() {
        return this.taxlines;
    }

    public OrderItem taxlines(String taxlines) {
        this.taxlines = taxlines;
        return this;
    }

    public void setTaxlines(String taxlines) {
        this.taxlines = taxlines;
    }

    public Boolean getCancelled() {
        return this.cancelled;
    }

    public OrderItem cancelled(Boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public OrderLine getLine() {
        return this.line;
    }

    public OrderItem line(OrderLine orderLine) {
        this.setLine(orderLine);
        return this;
    }

    public void setLine(OrderLine orderLine) {
        this.line = orderLine;
    }

    public Refund getRefund() {
        return this.refund;
    }

    public OrderItem refund(Refund refund) {
        this.setRefund(refund);
        return this;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public Set<Fulfillment> getFulfillments() {
        return this.fulfillments;
    }

    public OrderItem fulfillments(Set<Fulfillment> fulfillments) {
        this.setFulfillments(fulfillments);
        return this;
    }

    public OrderItem addFulfillment(Fulfillment fulfillment) {
        this.fulfillments.add(fulfillment);
        fulfillment.getOrderItems().add(this);
        return this;
    }

    public OrderItem removeFulfillment(Fulfillment fulfillment) {
        this.fulfillments.remove(fulfillment);
        fulfillment.getOrderItems().remove(this);
        return this;
    }

    public void setFulfillments(Set<Fulfillment> fulfillments) {
        this.fulfillments = fulfillments;
    }

    public Set<OrderModification> getOrderModifications() {
        return this.orderModifications;
    }

    public OrderItem orderModifications(Set<OrderModification> orderModifications) {
        this.setOrderModifications(orderModifications);
        return this;
    }

    public OrderItem addOrderModification(OrderModification orderModification) {
        this.orderModifications.add(orderModification);
        orderModification.getOrderItems().add(this);
        return this;
    }

    public OrderItem removeOrderModification(OrderModification orderModification) {
        this.orderModifications.remove(orderModification);
        orderModification.getOrderItems().remove(this);
        return this;
    }

    public void setOrderModifications(Set<OrderModification> orderModifications) {
        this.orderModifications = orderModifications;
    }

    public Set<StockMovement> getStockMovements() {
        return this.stockMovements;
    }

    public OrderItem stockMovements(Set<StockMovement> stockMovements) {
        this.setStockMovements(stockMovements);
        return this;
    }

    public OrderItem addStockMovement(StockMovement stockMovement) {
        this.stockMovements.add(stockMovement);
        stockMovement.setOrderitem(this);
        return this;
    }

    public OrderItem removeStockMovement(StockMovement stockMovement) {
        this.stockMovements.remove(stockMovement);
        stockMovement.setOrderitem(null);
        return this;
    }

    public void setStockMovements(Set<StockMovement> stockMovements) {
        if (this.stockMovements != null) {
            this.stockMovements.forEach(i -> i.setOrderitem(null));
        }
        if (stockMovements != null) {
            stockMovements.forEach(i -> i.setOrderitem(this));
        }
        this.stockMovements = stockMovements;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", initiallistprice=" + getInitiallistprice() +
            ", listprice=" + getListprice() +
            ", listpriceincludestax='" + getListpriceincludestax() + "'" +
            ", adjustments='" + getAdjustments() + "'" +
            ", taxlines='" + getTaxlines() + "'" +
            ", cancelled='" + getCancelled() + "'" +
            "}";
    }
}
