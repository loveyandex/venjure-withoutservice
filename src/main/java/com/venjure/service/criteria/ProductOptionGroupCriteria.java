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
 * Criteria class for the {@link com.venjure.domain.ProductOptionGroup} entity. This class is used
 * in {@link com.venjure.web.rest.ProductOptionGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-option-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductOptionGroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private InstantFilter deletedat;

    private StringFilter code;

    private LongFilter productId;

    private LongFilter productOptionId;

    private LongFilter productOptionGroupTranslationId;

    public ProductOptionGroupCriteria() {}

    public ProductOptionGroupCriteria(ProductOptionGroupCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.deletedat = other.deletedat == null ? null : other.deletedat.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.productOptionId = other.productOptionId == null ? null : other.productOptionId.copy();
        this.productOptionGroupTranslationId =
            other.productOptionGroupTranslationId == null ? null : other.productOptionGroupTranslationId.copy();
    }

    @Override
    public ProductOptionGroupCriteria copy() {
        return new ProductOptionGroupCriteria(this);
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

    public LongFilter getProductOptionId() {
        return productOptionId;
    }

    public LongFilter productOptionId() {
        if (productOptionId == null) {
            productOptionId = new LongFilter();
        }
        return productOptionId;
    }

    public void setProductOptionId(LongFilter productOptionId) {
        this.productOptionId = productOptionId;
    }

    public LongFilter getProductOptionGroupTranslationId() {
        return productOptionGroupTranslationId;
    }

    public LongFilter productOptionGroupTranslationId() {
        if (productOptionGroupTranslationId == null) {
            productOptionGroupTranslationId = new LongFilter();
        }
        return productOptionGroupTranslationId;
    }

    public void setProductOptionGroupTranslationId(LongFilter productOptionGroupTranslationId) {
        this.productOptionGroupTranslationId = productOptionGroupTranslationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductOptionGroupCriteria that = (ProductOptionGroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(deletedat, that.deletedat) &&
            Objects.equals(code, that.code) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(productOptionId, that.productOptionId) &&
            Objects.equals(productOptionGroupTranslationId, that.productOptionGroupTranslationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, deletedat, code, productId, productOptionId, productOptionGroupTranslationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOptionGroupCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (deletedat != null ? "deletedat=" + deletedat + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (productOptionId != null ? "productOptionId=" + productOptionId + ", " : "") +
            (productOptionGroupTranslationId != null ? "productOptionGroupTranslationId=" + productOptionGroupTranslationId + ", " : "") +
            "}";
    }
}
