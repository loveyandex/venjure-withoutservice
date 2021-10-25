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
 * A Fulfillment.
 */
@Entity
@Table(name = "fulfillment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fulfillment implements Serializable {

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
    @Column(name = "state", length = 255, nullable = false)
    private String state;

    @NotNull
    @Size(max = 255)
    @Column(name = "trackingcode", length = 255, nullable = false)
    private String trackingcode;

    @NotNull
    @Size(max = 255)
    @Column(name = "method", length = 255, nullable = false)
    private String method;

    @NotNull
    @Size(max = 255)
    @Column(name = "handlercode", length = 255, nullable = false)
    private String handlercode;

    @ManyToMany(mappedBy = "fulfillments")
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

    public Fulfillment id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Fulfillment createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Fulfillment updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getState() {
        return this.state;
    }

    public Fulfillment state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTrackingcode() {
        return this.trackingcode;
    }

    public Fulfillment trackingcode(String trackingcode) {
        this.trackingcode = trackingcode;
        return this;
    }

    public void setTrackingcode(String trackingcode) {
        this.trackingcode = trackingcode;
    }

    public String getMethod() {
        return this.method;
    }

    public Fulfillment method(String method) {
        this.method = method;
        return this;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHandlercode() {
        return this.handlercode;
    }

    public Fulfillment handlercode(String handlercode) {
        this.handlercode = handlercode;
        return this;
    }

    public void setHandlercode(String handlercode) {
        this.handlercode = handlercode;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public Fulfillment orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public Fulfillment addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.getFulfillments().add(this);
        return this;
    }

    public Fulfillment removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.getFulfillments().remove(this);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.removeFulfillment(this));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.addFulfillment(this));
        }
        this.orderItems = orderItems;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fulfillment)) {
            return false;
        }
        return id != null && id.equals(((Fulfillment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fulfillment{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", state='" + getState() + "'" +
            ", trackingcode='" + getTrackingcode() + "'" +
            ", method='" + getMethod() + "'" +
            ", handlercode='" + getHandlercode() + "'" +
            "}";
    }
}
