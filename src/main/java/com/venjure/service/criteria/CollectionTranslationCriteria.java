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
 * Criteria class for the {@link com.venjure.domain.CollectionTranslation} entity. This class is used
 * in {@link com.venjure.web.rest.CollectionTranslationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /collection-translations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CollectionTranslationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter languagecode;

    private StringFilter name;

    private StringFilter slug;

    private StringFilter description;

    private LongFilter baseId;

    public CollectionTranslationCriteria() {}

    public CollectionTranslationCriteria(CollectionTranslationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.languagecode = other.languagecode == null ? null : other.languagecode.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.baseId = other.baseId == null ? null : other.baseId.copy();
    }

    @Override
    public CollectionTranslationCriteria copy() {
        return new CollectionTranslationCriteria(this);
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

    public StringFilter getSlug() {
        return slug;
    }

    public StringFilter slug() {
        if (slug == null) {
            slug = new StringFilter();
        }
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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
        final CollectionTranslationCriteria that = (CollectionTranslationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(languagecode, that.languagecode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(description, that.description) &&
            Objects.equals(baseId, that.baseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, languagecode, name, slug, description, baseId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollectionTranslationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (languagecode != null ? "languagecode=" + languagecode + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (slug != null ? "slug=" + slug + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (baseId != null ? "baseId=" + baseId + ", " : "") +
            "}";
    }
}
