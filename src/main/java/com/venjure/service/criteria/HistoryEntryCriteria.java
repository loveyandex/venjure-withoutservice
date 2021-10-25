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
 * Criteria class for the {@link com.venjure.domain.HistoryEntry} entity. This class is used
 * in {@link com.venjure.web.rest.HistoryEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /history-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HistoryEntryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter type;

    private BooleanFilter ispublic;

    private StringFilter data;

    private StringFilter discriminator;

    private LongFilter administratorId;

    private LongFilter customerId;

    private LongFilter jorderId;

    public HistoryEntryCriteria() {}

    public HistoryEntryCriteria(HistoryEntryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.ispublic = other.ispublic == null ? null : other.ispublic.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.discriminator = other.discriminator == null ? null : other.discriminator.copy();
        this.administratorId = other.administratorId == null ? null : other.administratorId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.jorderId = other.jorderId == null ? null : other.jorderId.copy();
    }

    @Override
    public HistoryEntryCriteria copy() {
        return new HistoryEntryCriteria(this);
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

    public BooleanFilter getIspublic() {
        return ispublic;
    }

    public BooleanFilter ispublic() {
        if (ispublic == null) {
            ispublic = new BooleanFilter();
        }
        return ispublic;
    }

    public void setIspublic(BooleanFilter ispublic) {
        this.ispublic = ispublic;
    }

    public StringFilter getData() {
        return data;
    }

    public StringFilter data() {
        if (data == null) {
            data = new StringFilter();
        }
        return data;
    }

    public void setData(StringFilter data) {
        this.data = data;
    }

    public StringFilter getDiscriminator() {
        return discriminator;
    }

    public StringFilter discriminator() {
        if (discriminator == null) {
            discriminator = new StringFilter();
        }
        return discriminator;
    }

    public void setDiscriminator(StringFilter discriminator) {
        this.discriminator = discriminator;
    }

    public LongFilter getAdministratorId() {
        return administratorId;
    }

    public LongFilter administratorId() {
        if (administratorId == null) {
            administratorId = new LongFilter();
        }
        return administratorId;
    }

    public void setAdministratorId(LongFilter administratorId) {
        this.administratorId = administratorId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getJorderId() {
        return jorderId;
    }

    public LongFilter jorderId() {
        if (jorderId == null) {
            jorderId = new LongFilter();
        }
        return jorderId;
    }

    public void setJorderId(LongFilter jorderId) {
        this.jorderId = jorderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HistoryEntryCriteria that = (HistoryEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(type, that.type) &&
            Objects.equals(ispublic, that.ispublic) &&
            Objects.equals(data, that.data) &&
            Objects.equals(discriminator, that.discriminator) &&
            Objects.equals(administratorId, that.administratorId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(jorderId, that.jorderId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, type, ispublic, data, discriminator, administratorId, customerId, jorderId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryEntryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (ispublic != null ? "ispublic=" + ispublic + ", " : "") +
            (data != null ? "data=" + data + ", " : "") +
            (discriminator != null ? "discriminator=" + discriminator + ", " : "") +
            (administratorId != null ? "administratorId=" + administratorId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (jorderId != null ? "jorderId=" + jorderId + ", " : "") +
            "}";
    }
}
