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
 * Criteria class for the {@link com.venjure.domain.Collection} entity. This class is used
 * in {@link com.venjure.web.rest.CollectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /collections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CollectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private BooleanFilter isroot;

    private IntegerFilter position;

    private BooleanFilter isprivate;

    private StringFilter filters;

    private LongFilter featuredassetId;

    private LongFilter parentId;

    private LongFilter collectionTranslationId;

    private LongFilter productvariantId;

    public CollectionCriteria() {}

    public CollectionCriteria(CollectionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.isroot = other.isroot == null ? null : other.isroot.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.isprivate = other.isprivate == null ? null : other.isprivate.copy();
        this.filters = other.filters == null ? null : other.filters.copy();
        this.featuredassetId = other.featuredassetId == null ? null : other.featuredassetId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.collectionTranslationId = other.collectionTranslationId == null ? null : other.collectionTranslationId.copy();
        this.productvariantId = other.productvariantId == null ? null : other.productvariantId.copy();
    }

    @Override
    public CollectionCriteria copy() {
        return new CollectionCriteria(this);
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

    public BooleanFilter getIsroot() {
        return isroot;
    }

    public BooleanFilter isroot() {
        if (isroot == null) {
            isroot = new BooleanFilter();
        }
        return isroot;
    }

    public void setIsroot(BooleanFilter isroot) {
        this.isroot = isroot;
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

    public BooleanFilter getIsprivate() {
        return isprivate;
    }

    public BooleanFilter isprivate() {
        if (isprivate == null) {
            isprivate = new BooleanFilter();
        }
        return isprivate;
    }

    public void setIsprivate(BooleanFilter isprivate) {
        this.isprivate = isprivate;
    }

    public StringFilter getFilters() {
        return filters;
    }

    public StringFilter filters() {
        if (filters == null) {
            filters = new StringFilter();
        }
        return filters;
    }

    public void setFilters(StringFilter filters) {
        this.filters = filters;
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

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getCollectionTranslationId() {
        return collectionTranslationId;
    }

    public LongFilter collectionTranslationId() {
        if (collectionTranslationId == null) {
            collectionTranslationId = new LongFilter();
        }
        return collectionTranslationId;
    }

    public void setCollectionTranslationId(LongFilter collectionTranslationId) {
        this.collectionTranslationId = collectionTranslationId;
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
        final CollectionCriteria that = (CollectionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(isroot, that.isroot) &&
            Objects.equals(position, that.position) &&
            Objects.equals(isprivate, that.isprivate) &&
            Objects.equals(filters, that.filters) &&
            Objects.equals(featuredassetId, that.featuredassetId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(collectionTranslationId, that.collectionTranslationId) &&
            Objects.equals(productvariantId, that.productvariantId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            isroot,
            position,
            isprivate,
            filters,
            featuredassetId,
            parentId,
            collectionTranslationId,
            productvariantId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollectionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (isroot != null ? "isroot=" + isroot + ", " : "") +
            (position != null ? "position=" + position + ", " : "") +
            (isprivate != null ? "isprivate=" + isprivate + ", " : "") +
            (filters != null ? "filters=" + filters + ", " : "") +
            (featuredassetId != null ? "featuredassetId=" + featuredassetId + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (collectionTranslationId != null ? "collectionTranslationId=" + collectionTranslationId + ", " : "") +
            (productvariantId != null ? "productvariantId=" + productvariantId + ", " : "") +
            "}";
    }
}
