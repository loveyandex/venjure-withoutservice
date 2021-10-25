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
 * Criteria class for the {@link com.venjure.domain.JobRecord} entity. This class is used
 * in {@link com.venjure.web.rest.JobRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /job-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobRecordCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter queuename;

    private StringFilter data;

    private StringFilter state;

    private IntegerFilter progress;

    private StringFilter result;

    private StringFilter error;

    private InstantFilter startedat;

    private InstantFilter settledat;

    private BooleanFilter issettled;

    private IntegerFilter retries;

    private IntegerFilter attempts;

    public JobRecordCriteria() {}

    public JobRecordCriteria(JobRecordCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.queuename = other.queuename == null ? null : other.queuename.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.progress = other.progress == null ? null : other.progress.copy();
        this.result = other.result == null ? null : other.result.copy();
        this.error = other.error == null ? null : other.error.copy();
        this.startedat = other.startedat == null ? null : other.startedat.copy();
        this.settledat = other.settledat == null ? null : other.settledat.copy();
        this.issettled = other.issettled == null ? null : other.issettled.copy();
        this.retries = other.retries == null ? null : other.retries.copy();
        this.attempts = other.attempts == null ? null : other.attempts.copy();
    }

    @Override
    public JobRecordCriteria copy() {
        return new JobRecordCriteria(this);
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

    public StringFilter getQueuename() {
        return queuename;
    }

    public StringFilter queuename() {
        if (queuename == null) {
            queuename = new StringFilter();
        }
        return queuename;
    }

    public void setQueuename(StringFilter queuename) {
        this.queuename = queuename;
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

    public IntegerFilter getProgress() {
        return progress;
    }

    public IntegerFilter progress() {
        if (progress == null) {
            progress = new IntegerFilter();
        }
        return progress;
    }

    public void setProgress(IntegerFilter progress) {
        this.progress = progress;
    }

    public StringFilter getResult() {
        return result;
    }

    public StringFilter result() {
        if (result == null) {
            result = new StringFilter();
        }
        return result;
    }

    public void setResult(StringFilter result) {
        this.result = result;
    }

    public StringFilter getError() {
        return error;
    }

    public StringFilter error() {
        if (error == null) {
            error = new StringFilter();
        }
        return error;
    }

    public void setError(StringFilter error) {
        this.error = error;
    }

    public InstantFilter getStartedat() {
        return startedat;
    }

    public InstantFilter startedat() {
        if (startedat == null) {
            startedat = new InstantFilter();
        }
        return startedat;
    }

    public void setStartedat(InstantFilter startedat) {
        this.startedat = startedat;
    }

    public InstantFilter getSettledat() {
        return settledat;
    }

    public InstantFilter settledat() {
        if (settledat == null) {
            settledat = new InstantFilter();
        }
        return settledat;
    }

    public void setSettledat(InstantFilter settledat) {
        this.settledat = settledat;
    }

    public BooleanFilter getIssettled() {
        return issettled;
    }

    public BooleanFilter issettled() {
        if (issettled == null) {
            issettled = new BooleanFilter();
        }
        return issettled;
    }

    public void setIssettled(BooleanFilter issettled) {
        this.issettled = issettled;
    }

    public IntegerFilter getRetries() {
        return retries;
    }

    public IntegerFilter retries() {
        if (retries == null) {
            retries = new IntegerFilter();
        }
        return retries;
    }

    public void setRetries(IntegerFilter retries) {
        this.retries = retries;
    }

    public IntegerFilter getAttempts() {
        return attempts;
    }

    public IntegerFilter attempts() {
        if (attempts == null) {
            attempts = new IntegerFilter();
        }
        return attempts;
    }

    public void setAttempts(IntegerFilter attempts) {
        this.attempts = attempts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JobRecordCriteria that = (JobRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(queuename, that.queuename) &&
            Objects.equals(data, that.data) &&
            Objects.equals(state, that.state) &&
            Objects.equals(progress, that.progress) &&
            Objects.equals(result, that.result) &&
            Objects.equals(error, that.error) &&
            Objects.equals(startedat, that.startedat) &&
            Objects.equals(settledat, that.settledat) &&
            Objects.equals(issettled, that.issettled) &&
            Objects.equals(retries, that.retries) &&
            Objects.equals(attempts, that.attempts)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            queuename,
            data,
            state,
            progress,
            result,
            error,
            startedat,
            settledat,
            issettled,
            retries,
            attempts
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobRecordCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (queuename != null ? "queuename=" + queuename + ", " : "") +
            (data != null ? "data=" + data + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (progress != null ? "progress=" + progress + ", " : "") +
            (result != null ? "result=" + result + ", " : "") +
            (error != null ? "error=" + error + ", " : "") +
            (startedat != null ? "startedat=" + startedat + ", " : "") +
            (settledat != null ? "settledat=" + settledat + ", " : "") +
            (issettled != null ? "issettled=" + issettled + ", " : "") +
            (retries != null ? "retries=" + retries + ", " : "") +
            (attempts != null ? "attempts=" + attempts + ", " : "") +
            "}";
    }
}
