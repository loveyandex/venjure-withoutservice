package com.venjure.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.venjure.domain.TaxRate} entity. This class is used
 * in {@link com.venjure.web.rest.TaxRateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tax-rates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaxRateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter name;

    private BooleanFilter enabled;

    private BigDecimalFilter value;

    private LongFilter categoryId;

    private LongFilter zoneId;

    private LongFilter customergroupId;

    public TaxRateCriteria() {}

    public TaxRateCriteria(TaxRateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.zoneId = other.zoneId == null ? null : other.zoneId.copy();
        this.customergroupId = other.customergroupId == null ? null : other.customergroupId.copy();
    }

    @Override
    public TaxRateCriteria copy() {
        return new TaxRateCriteria(this);
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

    public BigDecimalFilter getValue() {
        return value;
    }

    public BigDecimalFilter value() {
        if (value == null) {
            value = new BigDecimalFilter();
        }
        return value;
    }

    public void setValue(BigDecimalFilter value) {
        this.value = value;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getZoneId() {
        return zoneId;
    }

    public LongFilter zoneId() {
        if (zoneId == null) {
            zoneId = new LongFilter();
        }
        return zoneId;
    }

    public void setZoneId(LongFilter zoneId) {
        this.zoneId = zoneId;
    }

    public LongFilter getCustomergroupId() {
        return customergroupId;
    }

    public LongFilter customergroupId() {
        if (customergroupId == null) {
            customergroupId = new LongFilter();
        }
        return customergroupId;
    }

    public void setCustomergroupId(LongFilter customergroupId) {
        this.customergroupId = customergroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaxRateCriteria that = (TaxRateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(name, that.name) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(value, that.value) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(zoneId, that.zoneId) &&
            Objects.equals(customergroupId, that.customergroupId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, name, enabled, value, categoryId, zoneId, customergroupId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxRateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (zoneId != null ? "zoneId=" + zoneId + ", " : "") +
            (customergroupId != null ? "customergroupId=" + customergroupId + ", " : "") +
            "}";
    }
}
