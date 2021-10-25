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
 * Criteria class for the {@link com.venjure.domain.Product} entity. This class is used
 * in {@link com.venjure.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private InstantFilter deletedat;

    private BooleanFilter enabled;

    private LongFilter featuredassetId;

    private LongFilter productVariantId;

    private LongFilter channelId;

    private LongFilter facetValueId;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.deletedat = other.deletedat == null ? null : other.deletedat.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.featuredassetId = other.featuredassetId == null ? null : other.featuredassetId.copy();
        this.productVariantId = other.productVariantId == null ? null : other.productVariantId.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.facetValueId = other.facetValueId == null ? null : other.facetValueId.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public InstantFilter getDeletedat() {
        return deletedat;
    }

    public InstantFilter deletedat() {
        if (deletedat == null) {
            deletedat = new InstantFilter();
        }
        return deletedat;
    }

    public void setDeletedat(InstantFilter deletedat) {
        this.deletedat = deletedat;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public BooleanFilter enabled() {
        if (enabled == null) {
            enabled = new BooleanFilter();
        }
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public LongFilter getFeaturedassetId() {
        return featuredassetId;
    }

    public LongFilter featuredassetId() {
        if (featuredassetId == null) {
            featuredassetId = new LongFilter();
        }
        return featuredassetId;
    }

    public void setFeaturedassetId(LongFilter featuredassetId) {
        this.featuredassetId = featuredassetId;
    }

    public LongFilter getProductVariantId() {
        return productVariantId;
    }

    public LongFilter productVariantId() {
        if (productVariantId == null) {
            productVariantId = new LongFilter();
        }
        return productVariantId;
    }

    public void setProductVariantId(LongFilter productVariantId) {
        this.productVariantId = productVariantId;
    }

    public LongFilter getChannelId() {
        return channelId;
    }

    public LongFilter channelId() {
        if (channelId == null) {
            channelId = new LongFilter();
        }
        return channelId;
    }

    public void setChannelId(LongFilter channelId) {
        this.channelId = channelId;
    }

    public LongFilter getFacetValueId() {
        return facetValueId;
    }

    public LongFilter facetValueId() {
        if (facetValueId == null) {
            facetValueId = new LongFilter();
        }
        return facetValueId;
    }

    public void setFacetValueId(LongFilter facetValueId) {
        this.facetValueId = facetValueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(deletedat, that.deletedat) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(featuredassetId, that.featuredassetId) &&
            Objects.equals(productVariantId, that.productVariantId) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(facetValueId, that.facetValueId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, deletedat, enabled, featuredassetId, productVariantId, channelId, facetValueId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (deletedat != null ? "deletedat=" + deletedat + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (featuredassetId != null ? "featuredassetId=" + featuredassetId + ", " : "") +
            (productVariantId != null ? "productVariantId=" + productVariantId + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            (facetValueId != null ? "facetValueId=" + facetValueId + ", " : "") +
            "}";
    }
}
