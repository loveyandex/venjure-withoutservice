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
 * Criteria class for the {@link com.venjure.domain.FacetTranslation} entity. This class is used
 * in {@link com.venjure.web.rest.FacetTranslationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facet-translations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FacetTranslationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter languagecode;

    private StringFilter name;

    private LongFilter baseId;

    public FacetTranslationCriteria() {}

    public FacetTranslationCriteria(FacetTranslationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.languagecode = other.languagecode == null ? null : other.languagecode.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.baseId = other.baseId == null ? null : other.baseId.copy();
    }

    @Override
    public FacetTranslationCriteria copy() {
        return new FacetTranslationCriteria(this);
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

    public StringFilter getLanguagecode() {
        return languagecode;
    }

    public StringFilter languagecode() {
        if (languagecode == null) {
            languagecode = new StringFilter();
        }
        return languagecode;
    }

    public void setLanguagecode(StringFilter languagecode) {
        this.languagecode = languagecode;
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

    public LongFilter getBaseId() {
        return baseId;
    }

    public LongFilter baseId() {
        if (baseId == null) {
            baseId = new LongFilter();
        }
        return baseId;
    }

    public void setBaseId(LongFilter baseId) {
        this.baseId = baseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FacetTranslationCriteria that = (FacetTranslationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(languagecode, that.languagecode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(baseId, that.baseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, languagecode, name, baseId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacetTranslationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (languagecode != null ? "languagecode=" + languagecode + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (baseId != null ? "baseId=" + baseId + ", " : "") +
            "}";
    }
}
