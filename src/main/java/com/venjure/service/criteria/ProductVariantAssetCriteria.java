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
 * Criteria class for the {@link com.venjure.domain.ProductVariantAsset} entity. This class is used
 * in {@link com.venjure.web.rest.ProductVariantAssetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-variant-assets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductVariantAssetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private IntegerFilter position;

    private LongFilter assetId;

    private LongFilter productvariantId;

    public ProductVariantAssetCriteria() {}

    public ProductVariantAssetCriteria(ProductVariantAssetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.assetId = other.assetId == null ? null : other.assetId.copy();
        this.productvariantId = other.productvariantId == null ? null : other.productvariantId.copy();
    }

    @Override
    public ProductVariantAssetCriteria copy() {
        return new ProductVariantAssetCriteria(this);
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

    public IntegerFilter getPosition() {
        return position;
    }

    public IntegerFilter position() {
        if (position == null) {
            position = new IntegerFilter();
        }
        return position;
    }

    public void setPosition(IntegerFilter position) {
        this.position = position;
    }

    public LongFilter getAssetId() {
        return assetId;
    }

    public LongFilter assetId() {
        if (assetId == null) {
            assetId = new LongFilter();
        }
        return assetId;
    }

    public void setAssetId(LongFilter assetId) {
        this.assetId = assetId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductVariantAssetCriteria that = (ProductVariantAssetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(position, that.position) &&
            Objects.equals(assetId, that.assetId) &&
            Objects.equals(productvariantId, that.productvariantId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, position, assetId, productvariantId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantAssetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (position != null ? "position=" + position + ", " : "") +
            (assetId != null ? "assetId=" + assetId + ", " : "") +
            (productvariantId != null ? "productvariantId=" + productvariantId + ", " : "") +
            "}";
    }
}
