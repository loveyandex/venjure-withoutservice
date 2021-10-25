package com.venjure.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JobRecord.
 */
@Entity
@Table(name = "job_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JobRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "createdat", nullable = false)
    private Instant createdat;

    @NotNull
    @Column(name = "updatedat", nullable = false)
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    @Column(name = "queuename", length = 255, nullable = false)
    private String queuename;

    @Size(max = 255)
    @Column(name = "data", length = 255)
    private String data;

    @NotNull
    @Size(max = 255)
    @Column(name = "state", length = 255, nullable = false)
    private String state;

    @NotNull
    @Column(name = "progress", nullable = false)
    private Integer progress;

    @Size(max = 255)
    @Column(name = "result", length = 255)
    private String result;

    @Size(max = 255)
    @Column(name = "error", length = 255)
    private String error;

    @Column(name = "startedat")
    private Instant startedat;

    @Column(name = "settledat")
    private Instant settledat;

    @NotNull
    @Column(name = "issettled", nullable = false)
    private Boolean issettled;

    @NotNull
    @Column(name = "retries", nullable = false)
    private Integer retries;

    @NotNull
    @Column(name = "attempts", nullable = false)
    private Integer attempts;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobRecord id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public JobRecord createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public JobRecord updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getQueuename() {
        return this.queuename;
    }

    public JobRecord queuename(String queuename) {
        this.queuename = queuename;
        return this;
    }

    public void setQueuename(String queuename) {
        this.queuename = queuename;
    }

    public String getData() {
        return this.data;
    }

    public JobRecord data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getState() {
        return this.state;
    }

    public JobRecord state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getProgress() {
        return this.progress;
    }

    public JobRecord progress(Integer progress) {
        this.progress = progress;
        return this;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getResult() {
        return this.result;
    }

    public JobRecord result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return this.error;
    }

    public JobRecord error(String error) {
        this.error = error;
        return this;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Instant getStartedat() {
        return this.startedat;
    }

    public JobRecord startedat(Instant startedat) {
        this.startedat = startedat;
        return this;
    }

    public void setStartedat(Instant startedat) {
        this.startedat = startedat;
    }

    public Instant getSettledat() {
        return this.settledat;
    }

    public JobRecord settledat(Instant settledat) {
        this.settledat = settledat;
        return this;
    }

    public void setSettledat(Instant settledat) {
        this.settledat = settledat;
    }

    public Boolean getIssettled() {
        return this.issettled;
    }

    public JobRecord issettled(Boolean issettled) {
        this.issettled = issettled;
        return this;
    }

    public void setIssettled(Boolean issettled) {
        this.issettled = issettled;
    }

    public Integer getRetries() {
        return this.retries;
    }

    public JobRecord retries(Integer retries) {
        this.retries = retries;
        return this;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getAttempts() {
        return this.attempts;
    }

    public JobRecord attempts(Integer attempts) {
        this.attempts = attempts;
        return this;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobRecord)) {
            return false;
        }
        return id != null && id.equals(((JobRecord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobRecord{" +
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
