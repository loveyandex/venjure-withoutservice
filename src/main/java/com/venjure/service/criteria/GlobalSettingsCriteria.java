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
 * Criteria class for the {@link com.venjure.domain.GlobalSettings} entity. This class is used
 * in {@link com.venjure.web.rest.GlobalSettingsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /global-settings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GlobalSettingsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter availablelanguages;

    private BooleanFilter trackinventory;

    private IntegerFilter outofstockthreshold;

    public GlobalSettingsCriteria() {}

    public GlobalSettingsCriteria(GlobalSettingsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.availablelanguages = other.availablelanguages == null ? null : other.availablelanguages.copy();
        this.trackinventory = other.trackinventory == null ? null : other.trackinventory.copy();
        this.outofstockthreshold = other.outofstockthreshold == null ? null : other.outofstockthreshold.copy();
    }

    @Override
    public GlobalSettingsCriteria copy() {
        return new GlobalSettingsCriteria(this);
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

    public StringFilter getAvailablelanguages() {
        return availablelanguages;
    }

    public StringFilter availablelanguages() {
        if (availablelanguages == null) {
            availablelanguages = new StringFilter();
        }
        return availablelanguages;
    }

    public void setAvailablelanguages(StringFilter availablelanguages) {
        this.availablelanguages = availablelanguages;
    }

    public BooleanFilter getTrackinventory() {
        return trackinventory;
    }

    public BooleanFilter trackinventory() {
        if (trackinventory == null) {
            trackinventory = new BooleanFilter();
        }
        return trackinventory;
    }

    public void setTrackinventory(BooleanFilter trackinventory) {
        this.trackinventory = trackinventory;
    }

    public IntegerFilter getOutofstockthreshold() {
        return outofstockthreshold;
    }

    public IntegerFilter outofstockthreshold() {
        if (outofstockthreshold == null) {
            outofstockthreshold = new IntegerFilter();
        }
        return outofstockthreshold;
    }

    public void setOutofstockthreshold(IntegerFilter outofstockthreshold) {
        this.outofstockthreshold = outofstockthreshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GlobalSettingsCriteria that = (GlobalSettingsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(availablelanguages, that.availablelanguages) &&
            Objects.equals(trackinventory, that.trackinventory) &&
            Objects.equals(outofstockthreshold, that.outofstockthreshold)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, availablelanguages, trackinventory, outofstockthreshold);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlobalSettingsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (availablelanguages != null ? "availablelanguages=" + availablelanguages + ", " : "") +
            (trackinventory != null ? "trackinventory=" + trackinventory + ", " : "") +
            (outofstockthreshold != null ? "outofstockthreshold=" + outofstockthreshold + ", " : "") +
            "}";
    }
}
