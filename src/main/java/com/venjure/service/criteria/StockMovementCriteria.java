package com.venjure.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.venjure.domain.StockMovement} entity. This class is used
 * in {@link com.venjure.web.rest.StockMovementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stock-movements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockMovementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter type;

    private IntegerFilter quantity;

    private StringFilter discriminator;

    private LongFilter productvariantId;

    private LongFilter orderitemId;

    private LongFilter orderlineId;

    public StockMovementCriteria() {}

    public StockMovementCriteria(StockMovementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.discriminator = other.discriminator == null ? null : other.discriminator.copy();
        this.productvariantId = other.productvariantId == null ? null : other.productvariantId.copy();
        this.orderitemId = other.orderitemId == null ? null : other.orderitemId.copy();
        this.orderlineId = other.orderlineId == null ? null : other.orderlineId.copy();
    }

    @Override
    public StockMovementCriteria copy() {
        return new StockMovementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreatedat() {
        return createdat;
    }

    public InstantFilter createdat() {
        if (createdat == null) {
            createdat = new InstantFilter();
        }
        return createdat;
    }

    public void setCreatedat(InstantFilter createdat) {
        this.createdat = createdat;
    }

    public InstantFilter getUpdatedat() {
        return updatedat;
    }

    public InstantFilter updatedat() {
        if (updatedat == null) {
            updatedat = new InstantFilter();
        }
        return updatedat;
    }

    public void setUpdatedat(InstantFilter updatedat) {
        this.updatedat = updatedat;
    }

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getDiscriminator() {
        return discriminator;
    }

    public StringFilter discriminator() {
        if (discriminator == null) {
            discriminator = new StringFilter();
        }
        return discriminator;
    }

    public void setDiscriminator(StringFilter discriminator) {
        this.discriminator = discriminator;
    }

    public LongFilter getProductvariantId() {
        return productvariantId;
    }

    public LongFilter productvariantId() {
        if (productvariantId == null) {
            productvariantId = new LongFilter();
        }
        return productvariantId;
    }

    public void setProductvariantId(LongFilter productvariantId) {
        this.productvariantId = productvariantId;
    }

    public LongFilter getOrderitemId() {
        return orderitemId;
    }

    public LongFilter orderitemId() {
        if (orderitemId == null) {
            orderitemId = new LongFilter();
        }
        return orderitemId;
    }

    public void setOrderitemId(LongFilter orderitemId) {
        this.orderitemId = orderitemId;
    }

    public LongFilter getOrderlineId() {
        return orderlineId;
    }

    public LongFilter orderlineId() {
        if (orderlineId == null) {
            orderlineId = new LongFilter();
        }
        return orderlineId;
    }

    public void setOrderlineId(LongFilter orderlineId) {
        this.orderlineId = orderlineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockMovementCriteria that = (StockMovementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(type, that.type) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(discriminator, that.discriminator) &&
            Objects.equals(productvariantId, that.productvariantId) &&
            Objects.equals(orderitemId, that.orderitemId) &&
            Objects.equals(orderlineId, that.orderlineId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, type, quantity, discriminator, productvariantId, orderitemId, orderlineId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockMovementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (discriminator != null ? "discriminator=" + discriminator + ", " : "") +
            (productvariantId != null ? "productvariantId=" + productvariantId + ", " : "") +
            (orderitemId != null ? "orderitemId=" + orderitemId + ", " : "") +
            (orderlineId != null ? "orderlineId=" + orderlineId + ", " : "") +
            "}";
    }
}
