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
 * Criteria class for the {@link com.venjure.domain.Fulfillment} entity. This class is used
 * in {@link com.venjure.web.rest.FulfillmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fulfillments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FulfillmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter state;

    private StringFilter trackingcode;

    private StringFilter method;

    private StringFilter handlercode;

    private LongFilter orderItemId;

    public FulfillmentCriteria() {}

    public FulfillmentCriteria(FulfillmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.trackingcode = other.trackingcode == null ? null : other.trackingcode.copy();
        this.method = other.method == null ? null : other.method.copy();
        this.handlercode = other.handlercode == null ? null : other.handlercode.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
    }

    @Override
    public FulfillmentCriteria copy() {
        return new FulfillmentCriteria(this);
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

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getTrackingcode() {
        return trackingcode;
    }

    public StringFilter trackingcode() {
        if (trackingcode == null) {
            trackingcode = new StringFilter();
        }
        return trackingcode;
    }

    public void setTrackingcode(StringFilter trackingcode) {
        this.trackingcode = trackingcode;
    }

    public StringFilter getMethod() {
        return method;
    }

    public StringFilter method() {
        if (method == null) {
            method = new StringFilter();
        }
        return method;
    }

    public void setMethod(StringFilter method) {
        this.method = method;
    }

    public StringFilter getHandlercode() {
        return handlercode;
    }

    public StringFilter handlercode() {
        if (handlercode == null) {
            handlercode = new StringFilter();
        }
        return handlercode;
    }

    public void setHandlercode(StringFilter handlercode) {
        this.handlercode = handlercode;
    }

    public LongFilter getOrderItemId() {
        return orderItemId;
    }

    public LongFilter orderItemId() {
        if (orderItemId == null) {
            orderItemId = new LongFilter();
        }
        return orderItemId;
    }

    public void setOrderItemId(LongFilter orderItemId) {
        this.orderItemId = orderItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FulfillmentCriteria that = (FulfillmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(state, that.state) &&
            Objects.equals(trackingcode, that.trackingcode) &&
            Objects.equals(method, that.method) &&
            Objects.equals(handlercode, that.handlercode) &&
            Objects.equals(orderItemId, that.orderItemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, state, trackingcode, method, handlercode, orderItemId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FulfillmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (trackingcode != null ? "trackingcode=" + trackingcode + ", " : "") +
            (method != null ? "method=" + method + ", " : "") +
            (handlercode != null ? "handlercode=" + handlercode + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            "}";
    }
}
