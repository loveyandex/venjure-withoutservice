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
 * A OrderModification.
 */
@Entity
@Table(name = "order_modification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderModification implements Serializable {

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
    @Column(name = "note", length = 255, nullable = false)
    private String note;

    @NotNull
    @Column(name = "pricechange", nullable = false)
    private Integer pricechange;

    @Size(max = 255)
    @Column(name = "shippingaddresschange", length = 255)
    private String shippingaddresschange;

    @Size(max = 255)
    @Column(name = "billingaddresschange", length = 255)
    private String billingaddresschange;

    @JsonIgnoreProperties(value = { "jorder", "refunds" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Payment payment;

    @JsonIgnoreProperties(value = { "payment", "orderItems" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Refund refund;

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

    @OneToMany(mappedBy = "ordermodification")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jorder", "ordermodification" }, allowSetters = true)
    private Set<Surcharge> surcharges = new HashSet<>();

    @ManyToMany(mappedBy = "orderModifications")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "line", "refund", "fulfillments", "orderModifications", "stockMovements" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderModification id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public OrderModification createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public OrderModification updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getNote() {
        return this.note;
    }

    public OrderModification note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getPricechange() {
        return this.pricechange;
    }

    public OrderModification pricechange(Integer pricechange) {
        this.pricechange = pricechange;
        return this;
    }

    public void setPricechange(Integer pricechange) {
        this.pricechange = pricechange;
    }

    public String getShippingaddresschange() {
        return this.shippingaddresschange;
    }

    public OrderModification shippingaddresschange(String shippingaddresschange) {
        this.shippingaddresschange = shippingaddresschange;
        return this;
    }

    public void setShippingaddresschange(String shippingaddresschange) {
        this.shippingaddresschange = shippingaddresschange;
    }

    public String getBillingaddresschange() {
        return this.billingaddresschange;
    }

    public OrderModification billingaddresschange(String billingaddresschange) {
        this.billingaddresschange = billingaddresschange;
        return this;
    }

    public void setBillingaddresschange(String billingaddresschange) {
        this.billingaddresschange = billingaddresschange;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public OrderModification payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Refund getRefund() {
        return this.refund;
    }

    public OrderModification refund(Refund refund) {
        this.setRefund(refund);
        return this;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public Jorder getJorder() {
        return this.jorder;
    }

    public OrderModification jorder(Jorder jorder) {
        this.setJorder(jorder);
        return this;
    }

    public void setJorder(Jorder jorder) {
        this.jorder = jorder;
    }

    public Set<Surcharge> getSurcharges() {
        return this.surcharges;
    }

    public OrderModification surcharges(Set<Surcharge> surcharges) {
        this.setSurcharges(surcharges);
        return this;
    }

    public OrderModification addSurcharge(Surcharge surcharge) {
        this.surcharges.add(surcharge);
        surcharge.setOrdermodification(this);
        return this;
    }

    public OrderModification removeSurcharge(Surcharge surcharge) {
        this.surcharges.remove(surcharge);
        surcharge.setOrdermodification(null);
        return this;
    }

    public void setSurcharges(Set<Surcharge> surcharges) {
        if (this.surcharges != null) {
            this.surcharges.forEach(i -> i.setOrdermodification(null));
        }
        if (surcharges != null) {
            surcharges.forEach(i -> i.setOrdermodification(this));
        }
        this.surcharges = surcharges;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public OrderModification orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public OrderModification addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.getOrderModifications().add(this);
        return this;
    }

    public OrderModification removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.getOrderModifications().remove(this);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.removeOrderModification(this));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.addOrderModification(this));
        }
        this.orderItems = orderItems;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderModification)) {
            return false;
        }
        return id != null && id.equals(((OrderModification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderModification{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", note='" + getNote() + "'" +
            ", pricechange=" + getPricechange() +
            ", shippingaddresschange='" + getShippingaddresschange() + "'" +
            ", billingaddresschange='" + getBillingaddresschange() + "'" +
            "}";
    }
}
