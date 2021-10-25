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
 * Criteria class for the {@link com.venjure.domain.TaxCategory} entity. This class is used
 * in {@link com.venjure.web.rest.TaxCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tax-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaxCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter name;

    private BooleanFilter isdefault;

    private LongFilter taxRateId;

    public TaxCategoryCriteria() {}

    public TaxCategoryCriteria(TaxCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.isdefault = other.isdefault == null ? null : other.isdefault.copy();
        this.taxRateId = other.taxRateId == null ? null : other.taxRateId.copy();
    }

    @Override
    public TaxCategoryCriteria copy() {
        return new TaxCategoryCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BooleanFilter getIsdefault() {
        return isdefault;
    }

    public BooleanFilter isdefault() {
        if (isdefault == null) {
            isdefault = new BooleanFilter();
        }
        return isdefault;
    }

    public void setIsdefault(BooleanFilter isdefault) {
        this.isdefault = isdefault;
    }

    public LongFilter getTaxRateId() {
        return taxRateId;
    }

    public LongFilter taxRateId() {
        if (taxRateId == null) {
            taxRateId = new LongFilter();
        }
        return taxRateId;
    }

    public void setTaxRateId(LongFilter taxRateId) {
        this.taxRateId = taxRateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaxCategoryCriteria that = (TaxCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isdefault, that.isdefault) &&
            Objects.equals(taxRateId, that.taxRateId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, name, isdefault, taxRateId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (isdefault != null ? "isdefault=" + isdefault + ", " : "") +
            (taxRateId != null ? "taxRateId=" + taxRateId + ", " : "") +
            "}";
    }
}
