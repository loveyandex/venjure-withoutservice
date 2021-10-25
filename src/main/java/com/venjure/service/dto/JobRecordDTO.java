package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.JobRecord} entity.
 */
public class JobRecordDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String queuename;

    @Size(max = 255)
    private String data;

    @NotNull
    @Size(max = 255)
    private String state;

    @NotNull
    private Integer progress;

    @Size(max = 255)
    private String result;

    @Size(max = 255)
    private String error;

    private Instant startedat;

    private Instant settledat;

    @NotNull
    private Boolean issettled;

    @NotNull
    private Integer retries;

    @NotNull
    private Integer attempts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getQueuename() {
        return queuename;
    }

    public void setQueuename(String queuename) {
        this.queuename = queuename;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Instant getStartedat() {
        return startedat;
    }

    public void setStartedat(Instant startedat) {
        this.startedat = startedat;
    }

    public Instant getSettledat() {
        return settledat;
    }

    public void setSettledat(Instant settledat) {
        this.settledat = settledat;
    }

    public Boolean getIssettled() {
        return issettled;
    }

    public void setIssettled(Boolean issettled) {
        this.issettled = issettled;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobRecordDTO)) {
            return false;
        }

        JobRecordDTO jobRecordDTO = (JobRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jobRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobRecordDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", queuename='" + getQueuename() + "'" +
            ", data='" + getData() + "'" +
            ", state='" + getState() + "'" +
            ", progress=" + getProgress() +
            ", result='" + getResult() + "'" +
            ", error='" + getError() + "'" +
            ", startedat='" + getStartedat() + "'" +
            ", settledat='" + getSettledat() + "'" +
            ", issettled='" + getIssettled() + "'" +
            ", retries=" + getRetries() +
            ", attempts=" + getAttempts() +
            "}";
    }
}
