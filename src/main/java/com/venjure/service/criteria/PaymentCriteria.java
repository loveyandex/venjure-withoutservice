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
 * Criteria class for the {@link com.venjure.domain.Payment} entity. This class is used
 * in {@link com.venjure.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter method;

    private IntegerFilter amount;

    private StringFilter state;

    private StringFilter errormessage;

    private StringFilter transactionid;

    private StringFilter metadata;

    private LongFilter jorderId;

    private LongFilter refundId;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.method = other.method == null ? null : other.method.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.errormessage = other.errormessage == null ? null : other.errormessage.copy();
        this.transactionid = other.transactionid == null ? null : other.transactionid.copy();
        this.metadata = other.metadata == null ? null : other.metadata.copy();
        this.jorderId = other.jorderId == null ? null : other.jorderId.copy();
        this.refundId = other.refundId == null ? null : other.refundId.copy();
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public StringFilter getMethod() {
        return method;
    }

    public StringFilter method() {
        if (method == null) {
            method = new StringFilter();
        }
        return method;
    }

    public void setMethod(StringFilter method) {
        this.method = method;
    }

    public IntegerFilter getAmount() {
        return amount;
    }

    public IntegerFilter amount() {
        if (amount == null) {
            amount = new IntegerFilter();
        }
        return amount;
    }

    public void setAmount(IntegerFilter amount) {
        this.amount = amount;
    }

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getErrormessage() {
        return errormessage;
    }

    public StringFilter errormessage() {
        if (errormessage == null) {
            errormessage = new StringFilter();
        }
        return errormessage;
    }

    public void setErrormessage(StringFilter errormessage) {
        this.errormessage = errormessage;
    }

    public StringFilter getTransactionid() {
        return transactionid;
    }

    public StringFilter transactionid() {
        if (transactionid == null) {
            transactionid = new StringFilter();
        }
        return transactionid;
    }

    public void setTransactionid(StringFilter transactionid) {
        this.transactionid = transactionid;
    }

    public StringFilter getMetadata() {
        return metadata;
    }

    public StringFilter metadata() {
        if (metadata == null) {
            metadata = new StringFilter();
        }
        return metadata;
    }

    public void setMetadata(StringFilter metadata) {
        this.metadata = metadata;
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

    public LongFilter getRefundId() {
        return refundId;
    }

    public LongFilter refundId() {
        if (refundId == null) {
            refundId = new LongFilter();
        }
        return refundId;
    }

    public void setRefundId(LongFilter refundId) {
        this.refundId = refundId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(method, that.method) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(state, that.state) &&
            Objects.equals(errormessage, that.errormessage) &&
            Objects.equals(transactionid, that.transactionid) &&
            Objects.equals(metadata, that.metadata) &&
            Objects.equals(jorderId, that.jorderId) &&
            Objects.equals(refundId, that.refundId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, method, amount, state, errormessage, transactionid, metadata, jorderId, refundId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (method != null ? "method=" + method + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (errormessage != null ? "errormessage=" + errormessage + ", " : "") +
            (transactionid != null ? "transactionid=" + transactionid + ", " : "") +
            (metadata != null ? "metadata=" + metadata + ", " : "") +
            (jorderId != null ? "jorderId=" + jorderId + ", " : "") +
            (refundId != null ? "refundId=" + refundId + ", " : "") +
            "}";
    }
}
