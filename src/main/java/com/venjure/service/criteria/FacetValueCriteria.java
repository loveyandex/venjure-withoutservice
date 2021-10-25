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
 * Criteria class for the {@link com.venjure.domain.FacetValue} entity. This class is used
 * in {@link com.venjure.web.rest.FacetValueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facet-values?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FacetValueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter code;

    private LongFilter facetId;

    private LongFilter channelId;

    private LongFilter productId;

    private LongFilter facetValueTranslationId;

    private LongFilter productVariantId;

    public FacetValueCriteria() {}

    public FacetValueCriteria(FacetValueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.facetId = other.facetId == null ? null : other.facetId.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.facetValueTranslationId = other.facetValueTranslationId == null ? null : other.facetValueTranslationId.copy();
        this.productVariantId = other.productVariantId == null ? null : other.productVariantId.copy();
    }

    @Override
    public FacetValueCriteria copy() {
        return new FacetValueCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public LongFilter getFacetId() {
        return facetId;
    }

    public LongFilter facetId() {
        if (facetId == null) {
            facetId = new LongFilter();
        }
        return facetId;
    }

    public void setFacetId(LongFilter facetId) {
        this.facetId = facetId;
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

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getFacetValueTranslationId() {
        return facetValueTranslationId;
    }

    public LongFilter facetValueTranslationId() {
        if (facetValueTranslationId == null) {
            facetValueTranslationId = new LongFilter();
        }
        return facetValueTranslationId;
    }

    public void setFacetValueTranslationId(LongFilter facetValueTranslationId) {
        this.facetValueTranslationId = facetValueTranslationId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FacetValueCriteria that = (FacetValueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(code, that.code) &&
            Objects.equals(facetId, that.facetId) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(facetValueTranslationId, that.facetValueTranslationId) &&
            Objects.equals(productVariantId, that.productVariantId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, code, facetId, channelId, productId, facetValueTranslationId, productVariantId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacetValueCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (facetId != null ? "facetId=" + facetId + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (facetValueTranslationId != null ? "facetValueTranslationId=" + facetValueTranslationId + ", " : "") +
            (productVariantId != null ? "productVariantId=" + productVariantId + ", " : "") +
            "}";
    }
}
