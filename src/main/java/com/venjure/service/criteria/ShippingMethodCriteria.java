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
 * Criteria class for the {@link com.venjure.domain.ShippingMethod} entity. This class is used
 * in {@link com.venjure.web.rest.ShippingMethodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shipping-methods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShippingMethodCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private InstantFilter deletedat;

    private StringFilter code;

    private StringFilter checker;

    private StringFilter calculator;

    private StringFilter fulfillmenthandlercode;

    private LongFilter shippingMethodTranslationId;

    private LongFilter channelId;

    public ShippingMethodCriteria() {}

    public ShippingMethodCriteria(ShippingMethodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.deletedat = other.deletedat == null ? null : other.deletedat.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.checker = other.checker == null ? null : other.checker.copy();
        this.calculator = other.calculator == null ? null : other.calculator.copy();
        this.fulfillmenthandlercode = other.fulfillmenthandlercode == null ? null : other.fulfillmenthandlercode.copy();
        this.shippingMethodTranslationId = other.shippingMethodTranslationId == null ? null : other.shippingMethodTranslationId.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
    }

    @Override
    public ShippingMethodCriteria copy() {
        return new ShippingMethodCriteria(this);
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

    public StringFilter getChecker() {
        return checker;
    }

    public StringFilter checker() {
        if (checker == null) {
            checker = new StringFilter();
        }
        return checker;
    }

    public void setChecker(StringFilter checker) {
        this.checker = checker;
    }

    public StringFilter getCalculator() {
        return calculator;
    }

    public StringFilter calculator() {
        if (calculator == null) {
            calculator = new StringFilter();
        }
        return calculator;
    }

    public void setCalculator(StringFilter calculator) {
        this.calculator = calculator;
    }

    public StringFilter getFulfillmenthandlercode() {
        return fulfillmenthandlercode;
    }

    public StringFilter fulfillmenthandlercode() {
        if (fulfillmenthandlercode == null) {
            fulfillmenthandlercode = new StringFilter();
        }
        return fulfillmenthandlercode;
    }

    public void setFulfillmenthandlercode(StringFilter fulfillmenthandlercode) {
        this.fulfillmenthandlercode = fulfillmenthandlercode;
    }

    public LongFilter getShippingMethodTranslationId() {
        return shippingMethodTranslationId;
    }

    public LongFilter shippingMethodTranslationId() {
        if (shippingMethodTranslationId == null) {
            shippingMethodTranslationId = new LongFilter();
        }
        return shippingMethodTranslationId;
    }

    public void setShippingMethodTranslationId(LongFilter shippingMethodTranslationId) {
        this.shippingMethodTranslationId = shippingMethodTranslationId;
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
        final ShippingMethodCriteria that = (ShippingMethodCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(deletedat, that.deletedat) &&
            Objects.equals(code, that.code) &&
            Objects.equals(checker, that.checker) &&
            Objects.equals(calculator, that.calculator) &&
            Objects.equals(fulfillmenthandlercode, that.fulfillmenthandlercode) &&
            Objects.equals(shippingMethodTranslationId, that.shippingMethodTranslationId) &&
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
            code,
            checker,
            calculator,
            fulfillmenthandlercode,
            shippingMethodTranslationId,
            channelId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingMethodCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (deletedat != null ? "deletedat=" + deletedat + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (checker != null ? "checker=" + checker + ", " : "") +
            (calculator != null ? "calculator=" + calculator + ", " : "") +
            (fulfillmenthandlercode != null ? "fulfillmenthandlercode=" + fulfillmenthandlercode + ", " : "") +
            (shippingMethodTranslationId != null ? "shippingMethodTranslationId=" + shippingMethodTranslationId + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            "}";
    }
}
