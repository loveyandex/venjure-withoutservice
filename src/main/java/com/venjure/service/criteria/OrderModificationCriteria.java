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
 * Criteria class for the {@link com.venjure.domain.OrderModification} entity. This class is used
 * in {@link com.venjure.web.rest.OrderModificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-modifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderModificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter note;

    private IntegerFilter pricechange;

    private StringFilter shippingaddresschange;

    private StringFilter billingaddresschange;

    private LongFilter paymentId;

    private LongFilter refundId;

    private LongFilter jorderId;

    private LongFilter surchargeId;

    private LongFilter orderItemId;

    public OrderModificationCriteria() {}

    public OrderModificationCriteria(OrderModificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.pricechange = other.pricechange == null ? null : other.pricechange.copy();
        this.shippingaddresschange = other.shippingaddresschange == null ? null : other.shippingaddresschange.copy();
        this.billingaddresschange = other.billingaddresschange == null ? null : other.billingaddresschange.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.refundId = other.refundId == null ? null : other.refundId.copy();
        this.jorderId = other.jorderId == null ? null : other.jorderId.copy();
        this.surchargeId = other.surchargeId == null ? null : other.surchargeId.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
    }

    @Override
    public OrderModificationCriteria copy() {
        return new OrderModificationCriteria(this);
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

    public StringFilter getNote() {
        return note;
    }

    public StringFilter note() {
        if (note == null) {
            note = new StringFilter();
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public IntegerFilter getPricechange() {
        return pricechange;
    }

    public IntegerFilter pricechange() {
        if (pricechange == null) {
            pricechange = new IntegerFilter();
        }
        return pricechange;
    }

    public void setPricechange(IntegerFilter pricechange) {
        this.pricechange = pricechange;
    }

    public StringFilter getShippingaddresschange() {
        return shippingaddresschange;
    }

    public StringFilter shippingaddresschange() {
        if (shippingaddresschange == null) {
            shippingaddresschange = new StringFilter();
        }
        return shippingaddresschange;
    }

    public void setShippingaddresschange(StringFilter shippingaddresschange) {
        this.shippingaddresschange = shippingaddresschange;
    }

    public StringFilter getBillingaddresschange() {
        return billingaddresschange;
    }

    public StringFilter billingaddresschange() {
        if (billingaddresschange == null) {
            billingaddresschange = new StringFilter();
        }
        return billingaddresschange;
    }

    public void setBillingaddresschange(StringFilter billingaddresschange) {
        this.billingaddresschange = billingaddresschange;
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

    public LongFilter getRefundId() {
        return refundId;
    }

    public LongFilter refundId() {
        if (refundId == null) {
            refundId = new LongFilter();
        }
        return refundId;
    }

    public void setRefundId(LongFilter refundId) {
        this.refundId = refundId;
    }

    public LongFilter getJorderId() {
        return jorderId;
    }

    public LongFilter jorderId() {
        if (jorderId == null) {
            jorderId = new LongFilter();
        }
        return jorderId;
    }

    public void setJorderId(LongFilter jorderId) {
        this.jorderId = jorderId;
    }

    public LongFilter getSurchargeId() {
        return surchargeId;
    }

    public LongFilter surchargeId() {
        if (surchargeId == null) {
            surchargeId = new LongFilter();
        }
        return surchargeId;
    }

    public void setSurchargeId(LongFilter surchargeId) {
        this.surchargeId = surchargeId;
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
        final OrderModificationCriteria that = (OrderModificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(note, that.note) &&
            Objects.equals(pricechange, that.pricechange) &&
            Objects.equals(shippingaddresschange, that.shippingaddresschange) &&
            Objects.equals(billingaddresschange, that.billingaddresschange) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(refundId, that.refundId) &&
            Objects.equals(jorderId, that.jorderId) &&
            Objects.equals(surchargeId, that.surchargeId) &&
            Objects.equals(orderItemId, that.orderItemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            note,
            pricechange,
            shippingaddresschange,
            billingaddresschange,
            paymentId,
            refundId,
            jorderId,
            surchargeId,
            orderItemId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderModificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (pricechange != null ? "pricechange=" + pricechange + ", " : "") +
            (shippingaddresschange != null ? "shippingaddresschange=" + shippingaddresschange + ", " : "") +
            (billingaddresschange != null ? "billingaddresschange=" + billingaddresschange + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (refundId != null ? "refundId=" + refundId + ", " : "") +
            (jorderId != null ? "jorderId=" + jorderId + ", " : "") +
            (surchargeId != null ? "surchargeId=" + surchargeId + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            "}";
    }
}
