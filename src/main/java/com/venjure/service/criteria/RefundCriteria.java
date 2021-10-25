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
 * Criteria class for the {@link com.venjure.domain.Refund} entity. This class is used
 * in {@link com.venjure.web.rest.RefundResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /refunds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RefundCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private IntegerFilter items;

    private IntegerFilter shipping;

    private IntegerFilter adjustment;

    private IntegerFilter total;

    private StringFilter method;

    private StringFilter reason;

    private StringFilter state;

    private StringFilter transactionid;

    private StringFilter metadata;

    private LongFilter paymentId;

    private LongFilter orderItemId;

    public RefundCriteria() {}

    public RefundCriteria(RefundCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.items = other.items == null ? null : other.items.copy();
        this.shipping = other.shipping == null ? null : other.shipping.copy();
        this.adjustment = other.adjustment == null ? null : other.adjustment.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.method = other.method == null ? null : other.method.copy();
        this.reason = other.reason == null ? null : other.reason.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.transactionid = other.transactionid == null ? null : other.transactionid.copy();
        this.metadata = other.metadata == null ? null : other.metadata.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
    }

    @Override
    public RefundCriteria copy() {
        return new RefundCriteria(this);
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

    public IntegerFilter getItems() {
        return items;
    }

    public IntegerFilter items() {
        if (items == null) {
            items = new IntegerFilter();
        }
        return items;
    }

    public void setItems(IntegerFilter items) {
        this.items = items;
    }

    public IntegerFilter getShipping() {
        return shipping;
    }

    public IntegerFilter shipping() {
        if (shipping == null) {
            shipping = new IntegerFilter();
        }
        return shipping;
    }

    public void setShipping(IntegerFilter shipping) {
        this.shipping = shipping;
    }

    public IntegerFilter getAdjustment() {
        return adjustment;
    }

    public IntegerFilter adjustment() {
        if (adjustment == null) {
            adjustment = new IntegerFilter();
        }
        return adjustment;
    }

    public void setAdjustment(IntegerFilter adjustment) {
        this.adjustment = adjustment;
    }

    public IntegerFilter getTotal() {
        return total;
    }

    public IntegerFilter total() {
        if (total == null) {
            total = new IntegerFilter();
        }
        return total;
    }

    public void setTotal(IntegerFilter total) {
        this.total = total;
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

    public StringFilter getReason() {
        return reason;
    }

    public StringFilter reason() {
        if (reason == null) {
            reason = new StringFilter();
        }
        return reason;
    }

    public void setReason(StringFilter reason) {
        this.reason = reason;
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

    public StringFilter getTransactionid() {
        return transactionid;
    }

    public StringFilter transactionid() {
        if (transactionid == null) {
            transactionid = new StringFilter();
        }
        return transactionid;
    }

    public void setTransactionid(StringFilter transactionid) {
        this.transactionid = transactionid;
    }

    public StringFilter getMetadata() {
        return metadata;
    }

    public StringFilter metadata() {
        if (metadata == null) {
            metadata = new StringFilter();
        }
        return metadata;
    }

    public void setMetadata(StringFilter metadata) {
        this.metadata = metadata;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
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
        final RefundCriteria that = (RefundCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(items, that.items) &&
            Objects.equals(shipping, that.shipping) &&
            Objects.equals(adjustment, that.adjustment) &&
            Objects.equals(total, that.total) &&
            Objects.equals(method, that.method) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(state, that.state) &&
            Objects.equals(transactionid, that.transactionid) &&
            Objects.equals(metadata, that.metadata) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(orderItemId, that.orderItemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            items,
            shipping,
            adjustment,
            total,
            method,
            reason,
            state,
            transactionid,
            metadata,
            paymentId,
            orderItemId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefundCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (items != null ? "items=" + items + ", " : "") +
            (shipping != null ? "shipping=" + shipping + ", " : "") +
            (adjustment != null ? "adjustment=" + adjustment + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (method != null ? "method=" + method + ", " : "") +
            (reason != null ? "reason=" + reason + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (transactionid != null ? "transactionid=" + transactionid + ", " : "") +
            (metadata != null ? "metadata=" + metadata + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            "}";
    }
}
