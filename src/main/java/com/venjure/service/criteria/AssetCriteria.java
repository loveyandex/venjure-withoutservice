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
 * Criteria class for the {@link com.venjure.domain.Asset} entity. This class is used
 * in {@link com.venjure.web.rest.AssetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter name;

    private StringFilter type;

    private StringFilter mimetype;

    private IntegerFilter width;

    private IntegerFilter height;

    private IntegerFilter filesize;

    private StringFilter source;

    private StringFilter preview;

    private StringFilter focalpoint;

    public AssetCriteria() {}

    public AssetCriteria(AssetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.mimetype = other.mimetype == null ? null : other.mimetype.copy();
        this.width = other.width == null ? null : other.width.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.filesize = other.filesize == null ? null : other.filesize.copy();
        this.source = other.source == null ? null : other.source.copy();
        this.preview = other.preview == null ? null : other.preview.copy();
        this.focalpoint = other.focalpoint == null ? null : other.focalpoint.copy();
    }

    @Override
    public AssetCriteria copy() {
        return new AssetCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getMimetype() {
        return mimetype;
    }

    public StringFilter mimetype() {
        if (mimetype == null) {
            mimetype = new StringFilter();
        }
        return mimetype;
    }

    public void setMimetype(StringFilter mimetype) {
        this.mimetype = mimetype;
    }

    public IntegerFilter getWidth() {
        return width;
    }

    public IntegerFilter width() {
        if (width == null) {
            width = new IntegerFilter();
        }
        return width;
    }

    public void setWidth(IntegerFilter width) {
        this.width = width;
    }

    public IntegerFilter getHeight() {
        return height;
    }

    public IntegerFilter height() {
        if (height == null) {
            height = new IntegerFilter();
        }
        return height;
    }

    public void setHeight(IntegerFilter height) {
        this.height = height;
    }

    public IntegerFilter getFilesize() {
        return filesize;
    }

    public IntegerFilter filesize() {
        if (filesize == null) {
            filesize = new IntegerFilter();
        }
        return filesize;
    }

    public void setFilesize(IntegerFilter filesize) {
        this.filesize = filesize;
    }

    public StringFilter getSource() {
        return source;
    }

    public StringFilter source() {
        if (source == null) {
            source = new StringFilter();
        }
        return source;
    }

    public void setSource(StringFilter source) {
        this.source = source;
    }

    public StringFilter getPreview() {
        return preview;
    }

    public StringFilter preview() {
        if (preview == null) {
            preview = new StringFilter();
        }
        return preview;
    }

    public void setPreview(StringFilter preview) {
        this.preview = preview;
    }

    public StringFilter getFocalpoint() {
        return focalpoint;
    }

    public StringFilter focalpoint() {
        if (focalpoint == null) {
            focalpoint = new StringFilter();
        }
        return focalpoint;
    }

    public void setFocalpoint(StringFilter focalpoint) {
        this.focalpoint = focalpoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssetCriteria that = (AssetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(mimetype, that.mimetype) &&
            Objects.equals(width, that.width) &&
            Objects.equals(height, that.height) &&
            Objects.equals(filesize, that.filesize) &&
            Objects.equals(source, that.source) &&
            Objects.equals(preview, that.preview) &&
            Objects.equals(focalpoint, that.focalpoint)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, name, type, mimetype, width, height, filesize, source, preview, focalpoint);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (mimetype != null ? "mimetype=" + mimetype + ", " : "") +
            (width != null ? "width=" + width + ", " : "") +
            (height != null ? "height=" + height + ", " : "") +
            (filesize != null ? "filesize=" + filesize + ", " : "") +
            (source != null ? "source=" + source + ", " : "") +
            (preview != null ? "preview=" + preview + ", " : "") +
            (focalpoint != null ? "focalpoint=" + focalpoint + ", " : "") +
            "}";
    }
}
