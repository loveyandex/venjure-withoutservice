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
 * Criteria class for the {@link com.venjure.domain.OrderItem} entity. This class is used
 * in {@link com.venjure.web.rest.OrderItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private IntegerFilter initiallistprice;

    private IntegerFilter listprice;

    private BooleanFilter listpriceincludestax;

    private StringFilter adjustments;

    private StringFilter taxlines;

    private BooleanFilter cancelled;

    private LongFilter lineId;

    private LongFilter refundId;

    private LongFilter fulfillmentId;

    private LongFilter orderModificationId;

    private LongFilter stockMovementId;

    public OrderItemCriteria() {}

    public OrderItemCriteria(OrderItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.initiallistprice = other.initiallistprice == null ? null : other.initiallistprice.copy();
        this.listprice = other.listprice == null ? null : other.listprice.copy();
        this.listpriceincludestax = other.listpriceincludestax == null ? null : other.listpriceincludestax.copy();
        this.adjustments = other.adjustments == null ? null : other.adjustments.copy();
        this.taxlines = other.taxlines == null ? null : other.taxlines.copy();
        this.cancelled = other.cancelled == null ? null : other.cancelled.copy();
        this.lineId = other.lineId == null ? null : other.lineId.copy();
        this.refundId = other.refundId == null ? null : other.refundId.copy();
        this.fulfillmentId = other.fulfillmentId == null ? null : other.fulfillmentId.copy();
        this.orderModificationId = other.orderModificationId == null ? null : other.orderModificationId.copy();
        this.stockMovementId = other.stockMovementId == null ? null : other.stockMovementId.copy();
    }

    @Override
    public OrderItemCriteria copy() {
        return new OrderItemCriteria(this);
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

    public IntegerFilter getInitiallistprice() {
        return initiallistprice;
    }

    public IntegerFilter initiallistprice() {
        if (initiallistprice == null) {
            initiallistprice = new IntegerFilter();
        }
        return initiallistprice;
    }

    public void setInitiallistprice(IntegerFilter initiallistprice) {
        this.initiallistprice = initiallistprice;
    }

    public IntegerFilter getListprice() {
        return listprice;
    }

    public IntegerFilter listprice() {
        if (listprice == null) {
            listprice = new IntegerFilter();
        }
        return listprice;
    }

    public void setListprice(IntegerFilter listprice) {
        this.listprice = listprice;
    }

    public BooleanFilter getListpriceincludestax() {
        return listpriceincludestax;
    }

    public BooleanFilter listpriceincludestax() {
        if (listpriceincludestax == null) {
            listpriceincludestax = new BooleanFilter();
        }
        return listpriceincludestax;
    }

    public void setListpriceincludestax(BooleanFilter listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
    }

    public StringFilter getAdjustments() {
        return adjustments;
    }

    public StringFilter adjustments() {
        if (adjustments == null) {
            adjustments = new StringFilter();
        }
        return adjustments;
    }

    public void setAdjustments(StringFilter adjustments) {
        this.adjustments = adjustments;
    }

    public StringFilter getTaxlines() {
        return taxlines;
    }

    public StringFilter taxlines() {
        if (taxlines == null) {
            taxlines = new StringFilter();
        }
        return taxlines;
    }

    public void setTaxlines(StringFilter taxlines) {
        this.taxlines = taxlines;
    }

    public BooleanFilter getCancelled() {
        return cancelled;
    }

    public BooleanFilter cancelled() {
        if (cancelled == null) {
            cancelled = new BooleanFilter();
        }
        return cancelled;
    }

    public void setCancelled(BooleanFilter cancelled) {
        this.cancelled = cancelled;
    }

    public LongFilter getLineId() {
        return lineId;
    }

    public LongFilter lineId() {
        if (lineId == null) {
            lineId = new LongFilter();
        }
        return lineId;
    }

    public void setLineId(LongFilter lineId) {
        this.lineId = lineId;
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

    public LongFilter getFulfillmentId() {
        return fulfillmentId;
    }

    public LongFilter fulfillmentId() {
        if (fulfillmentId == null) {
            fulfillmentId = new LongFilter();
        }
        return fulfillmentId;
    }

    public void setFulfillmentId(LongFilter fulfillmentId) {
        this.fulfillmentId = fulfillmentId;
    }

    public LongFilter getOrderModificationId() {
        return orderModificationId;
    }

    public LongFilter orderModificationId() {
        if (orderModificationId == null) {
            orderModificationId = new LongFilter();
        }
        return orderModificationId;
    }

    public void setOrderModificationId(LongFilter orderModificationId) {
        this.orderModificationId = orderModificationId;
    }

    public LongFilter getStockMovementId() {
        return stockMovementId;
    }

    public LongFilter stockMovementId() {
        if (stockMovementId == null) {
            stockMovementId = new LongFilter();
        }
        return stockMovementId;
    }

    public void setStockMovementId(LongFilter stockMovementId) {
        this.stockMovementId = stockMovementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderItemCriteria that = (OrderItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(initiallistprice, that.initiallistprice) &&
            Objects.equals(listprice, that.listprice) &&
            Objects.equals(listpriceincludestax, that.listpriceincludestax) &&
            Objects.equals(adjustments, that.adjustments) &&
            Objects.equals(taxlines, that.taxlines) &&
            Objects.equals(cancelled, that.cancelled) &&
            Objects.equals(lineId, that.lineId) &&
            Objects.equals(refundId, that.refundId) &&
            Objects.equals(fulfillmentId, that.fulfillmentId) &&
            Objects.equals(orderModificationId, that.orderModificationId) &&
            Objects.equals(stockMovementId, that.stockMovementId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            initiallistprice,
            listprice,
            listpriceincludestax,
            adjustments,
            taxlines,
            cancelled,
            lineId,
            refundId,
            fulfillmentId,
            orderModificationId,
            stockMovementId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (initiallistprice != null ? "initiallistprice=" + initiallistprice + ", " : "") +
            (listprice != null ? "listprice=" + listprice + ", " : "") +
            (listpriceincludestax != null ? "listpriceincludestax=" + listpriceincludestax + ", " : "") +
            (adjustments != null ? "adjustments=" + adjustments + ", " : "") +
            (taxlines != null ? "taxlines=" + taxlines + ", " : "") +
            (cancelled != null ? "cancelled=" + cancelled + ", " : "") +
            (lineId != null ? "lineId=" + lineId + ", " : "") +
            (refundId != null ? "refundId=" + refundId + ", " : "") +
            (fulfillmentId != null ? "fulfillmentId=" + fulfillmentId + ", " : "") +
            (orderModificationId != null ? "orderModificationId=" + orderModificationId + ", " : "") +
            (stockMovementId != null ? "stockMovementId=" + stockMovementId + ", " : "") +
            "}";
    }
}
