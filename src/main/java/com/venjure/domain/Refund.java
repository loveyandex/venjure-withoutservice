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
 * A Refund.
 */
@Entity
@Table(name = "refund")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Refund implements Serializable {

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
    @Column(name = "items", nullable = false)
    private Integer items;

    @NotNull
    @Column(name = "shipping", nullable = false)
    private Integer shipping;

    @NotNull
    @Column(name = "adjustment", nullable = false)
    private Integer adjustment;

    @NotNull
    @Column(name = "total", nullable = false)
    private Integer total;

    @NotNull
    @Size(max = 255)
    @Column(name = "method", length = 255, nullable = false)
    private String method;

    @Size(max = 255)
    @Column(name = "reason", length = 255)
    private String reason;

    @NotNull
    @Size(max = 255)
    @Column(name = "state", length = 255, nullable = false)
    private String state;

    @Size(max = 255)
    @Column(name = "transactionid", length = 255)
    private String transactionid;

    @NotNull
    @Size(max = 255)
    @Column(name = "metadata", length = 255, nullable = false)
    private String metadata;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "jorder", "refunds" }, allowSetters = true)
    private Payment payment;

    @OneToMany(mappedBy = "refund")
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

    public Refund id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Refund createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Refund updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Integer getItems() {
        return this.items;
    }

    public Refund items(Integer items) {
        this.items = items;
        return this;
    }

    public void setItems(Integer items) {
        this.items = items;
    }

    public Integer getShipping() {
        return this.shipping;
    }

    public Refund shipping(Integer shipping) {
        this.shipping = shipping;
        return this;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public Integer getAdjustment() {
        return this.adjustment;
    }

    public Refund adjustment(Integer adjustment) {
        this.adjustment = adjustment;
        return this;
    }

    public void setAdjustment(Integer adjustment) {
        this.adjustment = adjustment;
    }

    public Integer getTotal() {
        return this.total;
    }

    public Refund total(Integer total) {
        this.total = total;
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getMethod() {
        return this.method;
    }

    public Refund method(String method) {
        this.method = method;
        return this;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReason() {
        return this.reason;
    }

    public Refund reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getState() {
        return this.state;
    }

    public Refund state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTransactionid() {
        return this.transactionid;
    }

    public Refund transactionid(String transactionid) {
        this.transactionid = transactionid;
        return this;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public Refund metadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public Refund payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public Refund orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public Refund addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setRefund(this);
        return this;
    }

    public Refund removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setRefund(null);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setRefund(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setRefund(this));
        }
        this.orderItems = orderItems;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Refund)) {
            return false;
        }
        return id != null && id.equals(((Refund) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Refund{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", items=" + getItems() +
            ", shipping=" + getShipping() +
            ", adjustment=" + getAdjustment() +
            ", total=" + getTotal() +
            ", method='" + getMethod() + "'" +
            ", reason='" + getReason() + "'" +
            ", state='" + getState() + "'" +
            ", transactionid='" + getTransactionid() + "'" +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
