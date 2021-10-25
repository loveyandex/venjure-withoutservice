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
 * Criteria class for the {@link com.venjure.domain.Promotion} entity. This class is used
 * in {@link com.venjure.web.rest.PromotionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /promotions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PromotionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private InstantFilter deletedat;

    private InstantFilter startsat;

    private InstantFilter endsat;

    private StringFilter couponcode;

    private IntegerFilter percustomerusagelimit;

    private StringFilter name;

    private BooleanFilter enabled;

    private StringFilter conditions;

    private StringFilter actions;

    private IntegerFilter priorityscore;

    private LongFilter jorderId;

    private LongFilter channelId;

    public PromotionCriteria() {}

    public PromotionCriteria(PromotionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.deletedat = other.deletedat == null ? null : other.deletedat.copy();
        this.startsat = other.startsat == null ? null : other.startsat.copy();
        this.endsat = other.endsat == null ? null : other.endsat.copy();
        this.couponcode = other.couponcode == null ? null : other.couponcode.copy();
        this.percustomerusagelimit = other.percustomerusagelimit == null ? null : other.percustomerusagelimit.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.conditions = other.conditions == null ? null : other.conditions.copy();
        this.actions = other.actions == null ? null : other.actions.copy();
        this.priorityscore = other.priorityscore == null ? null : other.priorityscore.copy();
        this.jorderId = other.jorderId == null ? null : other.jorderId.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
    }

    @Override
    public PromotionCriteria copy() {
        return new PromotionCriteria(this);
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

    public InstantFilter getStartsat() {
        return startsat;
    }

    public InstantFilter startsat() {
        if (startsat == null) {
            startsat = new InstantFilter();
        }
        return startsat;
    }

    public void setStartsat(InstantFilter startsat) {
        this.startsat = startsat;
    }

    public InstantFilter getEndsat() {
        return endsat;
    }

    public InstantFilter endsat() {
        if (endsat == null) {
            endsat = new InstantFilter();
        }
        return endsat;
    }

    public void setEndsat(InstantFilter endsat) {
        this.endsat = endsat;
    }

    public StringFilter getCouponcode() {
        return couponcode;
    }

    public StringFilter couponcode() {
        if (couponcode == null) {
            couponcode = new StringFilter();
        }
        return couponcode;
    }

    public void setCouponcode(StringFilter couponcode) {
        this.couponcode = couponcode;
    }

    public IntegerFilter getPercustomerusagelimit() {
        return percustomerusagelimit;
    }

    public IntegerFilter percustomerusagelimit() {
        if (percustomerusagelimit == null) {
            percustomerusagelimit = new IntegerFilter();
        }
        return percustomerusagelimit;
    }

    public void setPercustomerusagelimit(IntegerFilter percustomerusagelimit) {
        this.percustomerusagelimit = percustomerusagelimit;
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

    public StringFilter getConditions() {
        return conditions;
    }

    public StringFilter conditions() {
        if (conditions == null) {
            conditions = new StringFilter();
        }
        return conditions;
    }

    public void setConditions(StringFilter conditions) {
        this.conditions = conditions;
    }

    public StringFilter getActions() {
        return actions;
    }

    public StringFilter actions() {
        if (actions == null) {
            actions = new StringFilter();
        }
        return actions;
    }

    public void setActions(StringFilter actions) {
        this.actions = actions;
    }

    public IntegerFilter getPriorityscore() {
        return priorityscore;
    }

    public IntegerFilter priorityscore() {
        if (priorityscore == null) {
            priorityscore = new IntegerFilter();
        }
        return priorityscore;
    }

    public void setPriorityscore(IntegerFilter priorityscore) {
        this.priorityscore = priorityscore;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PromotionCriteria that = (PromotionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(deletedat, that.deletedat) &&
            Objects.equals(startsat, that.startsat) &&
            Objects.equals(endsat, that.endsat) &&
            Objects.equals(couponcode, that.couponcode) &&
            Objects.equals(percustomerusagelimit, that.percustomerusagelimit) &&
            Objects.equals(name, that.name) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(conditions, that.conditions) &&
            Objects.equals(actions, that.actions) &&
            Objects.equals(priorityscore, that.priorityscore) &&
            Objects.equals(jorderId, that.jorderId) &&
            Objects.equals(channelId, that.channelId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            deletedat,
            startsat,
            endsat,
            couponcode,
            percustomerusagelimit,
            name,
            enabled,
            conditions,
            actions,
            priorityscore,
            jorderId,
            channelId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromotionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (deletedat != null ? "deletedat=" + deletedat + ", " : "") +
            (startsat != null ? "startsat=" + startsat + ", " : "") +
            (endsat != null ? "endsat=" + endsat + ", " : "") +
            (couponcode != null ? "couponcode=" + couponcode + ", " : "") +
            (percustomerusagelimit != null ? "percustomerusagelimit=" + percustomerusagelimit + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (conditions != null ? "conditions=" + conditions + ", " : "") +
            (actions != null ? "actions=" + actions + ", " : "") +
            (priorityscore != null ? "priorityscore=" + priorityscore + ", " : "") +
            (jorderId != null ? "jorderId=" + jorderId + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            "}";
    }
}
