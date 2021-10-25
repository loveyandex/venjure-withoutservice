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
 * Criteria class for the {@link com.venjure.domain.OrderLine} entity. This class is used
 * in {@link com.venjure.web.rest.OrderLineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-lines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderLineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private LongFilter productvariantId;

    private LongFilter taxcategoryId;

    private LongFilter featuredAssetId;

    private LongFilter jorderId;

    private LongFilter orderItemId;

    private LongFilter stockMovementId;

    public OrderLineCriteria() {}

    public OrderLineCriteria(OrderLineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.productvariantId = other.productvariantId == null ? null : other.productvariantId.copy();
        this.taxcategoryId = other.taxcategoryId == null ? null : other.taxcategoryId.copy();
        this.featuredAssetId = other.featuredAssetId == null ? null : other.featuredAssetId.copy();
        this.jorderId = other.jorderId == null ? null : other.jorderId.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
        this.stockMovementId = other.stockMovementId == null ? null : other.stockMovementId.copy();
    }

    @Override
    public OrderLineCriteria copy() {
        return new OrderLineCriteria(this);
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

    public LongFilter getTaxcategoryId() {
        return taxcategoryId;
    }

    public LongFilter taxcategoryId() {
        if (taxcategoryId == null) {
            taxcategoryId = new LongFilter();
        }
        return taxcategoryId;
    }

    public void setTaxcategoryId(LongFilter taxcategoryId) {
        this.taxcategoryId = taxcategoryId;
    }

    public LongFilter getFeaturedAssetId() {
        return featuredAssetId;
    }

    public LongFilter featuredAssetId() {
        if (featuredAssetId == null) {
            featuredAssetId = new LongFilter();
        }
        return featuredAssetId;
    }

    public void setFeaturedAssetId(LongFilter featuredAssetId) {
        this.featuredAssetId = featuredAssetId;
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
        final OrderLineCriteria that = (OrderLineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(productvariantId, that.productvariantId) &&
            Objects.equals(taxcategoryId, that.taxcategoryId) &&
            Objects.equals(featuredAssetId, that.featuredAssetId) &&
            Objects.equals(jorderId, that.jorderId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(stockMovementId, that.stockMovementId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            productvariantId,
            taxcategoryId,
            featuredAssetId,
            jorderId,
            orderItemId,
            stockMovementId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderLineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (productvariantId != null ? "productvariantId=" + productvariantId + ", " : "") +
            (taxcategoryId != null ? "taxcategoryId=" + taxcategoryId + ", " : "") +
            (featuredAssetId != null ? "featuredAssetId=" + featuredAssetId + ", " : "") +
            (jorderId != null ? "jorderId=" + jorderId + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            (stockMovementId != null ? "stockMovementId=" + stockMovementId + ", " : "") +
            "}";
    }
}
