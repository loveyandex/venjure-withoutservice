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
 * Criteria class for the {@link com.venjure.domain.ProductOption} entity. This class is used
 * in {@link com.venjure.web.rest.ProductOptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductOptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private InstantFilter deletedat;

    private StringFilter code;

    private LongFilter groupId;

    private LongFilter productOptionTranslationId;

    private LongFilter productVariantId;

    public ProductOptionCriteria() {}

    public ProductOptionCriteria(ProductOptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.deletedat = other.deletedat == null ? null : other.deletedat.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.groupId = other.groupId == null ? null : other.groupId.copy();
        this.productOptionTranslationId = other.productOptionTranslationId == null ? null : other.productOptionTranslationId.copy();
        this.productVariantId = other.productVariantId == null ? null : other.productVariantId.copy();
    }

    @Override
    public ProductOptionCriteria copy() {
        return new ProductOptionCriteria(this);
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

    public LongFilter getGroupId() {
        return groupId;
    }

    public LongFilter groupId() {
        if (groupId == null) {
            groupId = new LongFilter();
        }
        return groupId;
    }

    public void setGroupId(LongFilter groupId) {
        this.groupId = groupId;
    }

    public LongFilter getProductOptionTranslationId() {
        return productOptionTranslationId;
    }

    public LongFilter productOptionTranslationId() {
        if (productOptionTranslationId == null) {
            productOptionTranslationId = new LongFilter();
        }
        return productOptionTranslationId;
    }

    public void setProductOptionTranslationId(LongFilter productOptionTranslationId) {
        this.productOptionTranslationId = productOptionTranslationId;
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
        final ProductOptionCriteria that = (ProductOptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(deletedat, that.deletedat) &&
            Objects.equals(code, that.code) &&
            Objects.equals(groupId, that.groupId) &&
            Objects.equals(productOptionTranslationId, that.productOptionTranslationId) &&
            Objects.equals(productVariantId, that.productVariantId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, deletedat, code, groupId, productOptionTranslationId, productVariantId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (deletedat != null ? "deletedat=" + deletedat + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (groupId != null ? "groupId=" + groupId + ", " : "") +
            (productOptionTranslationId != null ? "productOptionTranslationId=" + productOptionTranslationId + ", " : "") +
            (productVariantId != null ? "productVariantId=" + productVariantId + ", " : "") +
            "}";
    }
}
