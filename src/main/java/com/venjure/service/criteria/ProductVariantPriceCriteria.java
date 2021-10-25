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
 * Criteria class for the {@link com.venjure.domain.ProductVariantPrice} entity. This class is used
 * in {@link com.venjure.web.rest.ProductVariantPriceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-variant-prices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductVariantPriceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private IntegerFilter price;

    private IntegerFilter channelid;

    private LongFilter variantId;

    public ProductVariantPriceCriteria() {}

    public ProductVariantPriceCriteria(ProductVariantPriceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.channelid = other.channelid == null ? null : other.channelid.copy();
        this.variantId = other.variantId == null ? null : other.variantId.copy();
    }

    @Override
    public ProductVariantPriceCriteria copy() {
        return new ProductVariantPriceCriteria(this);
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

    public IntegerFilter getPrice() {
        return price;
    }

    public IntegerFilter price() {
        if (price == null) {
            price = new IntegerFilter();
        }
        return price;
    }

    public void setPrice(IntegerFilter price) {
        this.price = price;
    }

    public IntegerFilter getChannelid() {
        return channelid;
    }

    public IntegerFilter channelid() {
        if (channelid == null) {
            channelid = new IntegerFilter();
        }
        return channelid;
    }

    public void setChannelid(IntegerFilter channelid) {
        this.channelid = channelid;
    }

    public LongFilter getVariantId() {
        return variantId;
    }

    public LongFilter variantId() {
        if (variantId == null) {
            variantId = new LongFilter();
        }
        return variantId;
    }

    public void setVariantId(LongFilter variantId) {
        this.variantId = variantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductVariantPriceCriteria that = (ProductVariantPriceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(price, that.price) &&
            Objects.equals(channelid, that.channelid) &&
            Objects.equals(variantId, that.variantId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, price, channelid, variantId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantPriceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (channelid != null ? "channelid=" + channelid + ", " : "") +
            (variantId != null ? "variantId=" + variantId + ", " : "") +
            "}";
    }
}
