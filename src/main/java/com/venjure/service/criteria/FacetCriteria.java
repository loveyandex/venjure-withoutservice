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
 * Criteria class for the {@link com.venjure.domain.Facet} entity. This class is used
 * in {@link com.venjure.web.rest.FacetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FacetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private BooleanFilter isprivate;

    private StringFilter code;

    private LongFilter channelId;

    private LongFilter facetTranslationId;

    private LongFilter facetValueId;

    public FacetCriteria() {}

    public FacetCriteria(FacetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.isprivate = other.isprivate == null ? null : other.isprivate.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.facetTranslationId = other.facetTranslationId == null ? null : other.facetTranslationId.copy();
        this.facetValueId = other.facetValueId == null ? null : other.facetValueId.copy();
    }

    @Override
    public FacetCriteria copy() {
        return new FacetCriteria(this);
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

    public LongFilter getFacetTranslationId() {
        return facetTranslationId;
    }

    public LongFilter facetTranslationId() {
        if (facetTranslationId == null) {
            facetTranslationId = new LongFilter();
        }
        return facetTranslationId;
    }

    public void setFacetTranslationId(LongFilter facetTranslationId) {
        this.facetTranslationId = facetTranslationId;
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
        final FacetCriteria that = (FacetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(isprivate, that.isprivate) &&
            Objects.equals(code, that.code) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(facetTranslationId, that.facetTranslationId) &&
            Objects.equals(facetValueId, that.facetValueId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, isprivate, code, channelId, facetTranslationId, facetValueId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (isprivate != null ? "isprivate=" + isprivate + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            (facetTranslationId != null ? "facetTranslationId=" + facetTranslationId + ", " : "") +
            (facetValueId != null ? "facetValueId=" + facetValueId + ", " : "") +
            "}";
    }
}
