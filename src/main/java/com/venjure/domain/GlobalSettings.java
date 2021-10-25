package com.venjure.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GlobalSettings.
 */
@Entity
@Table(name = "global_settings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GlobalSettings implements Serializable {

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
    @Column(name = "availablelanguages", length = 255, nullable = false)
    private String availablelanguages;

    @NotNull
    @Column(name = "trackinventory", nullable = false)
    private Boolean trackinventory;

    @NotNull
    @Column(name = "outofstockthreshold", nullable = false)
    private Integer outofstockthreshold;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GlobalSettings id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public GlobalSettings createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public GlobalSettings updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getAvailablelanguages() {
        return this.availablelanguages;
    }

    public GlobalSettings availablelanguages(String availablelanguages) {
        this.availablelanguages = availablelanguages;
        return this;
    }

    public void setAvailablelanguages(String availablelanguages) {
        this.availablelanguages = availablelanguages;
    }

    public Boolean getTrackinventory() {
        return this.trackinventory;
    }

    public GlobalSettings trackinventory(Boolean trackinventory) {
        this.trackinventory = trackinventory;
        return this;
    }

    public void setTrackinventory(Boolean trackinventory) {
        this.trackinventory = trackinventory;
    }

    public Integer getOutofstockthreshold() {
        return this.outofstockthreshold;
    }

    public GlobalSettings outofstockthreshold(Integer outofstockthreshold) {
        this.outofstockthreshold = outofstockthreshold;
        return this;
    }

    public void setOutofstockthreshold(Integer outofstockthreshold) {
        this.outofstockthreshold = outofstockthreshold;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalSettings)) {
            return false;
        }
        return id != null && id.equals(((GlobalSettings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlobalSettings{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", availablelanguages='" + getAvailablelanguages() + "'" +
            ", trackinventory='" + getTrackinventory() + "'" +
            ", outofstockthreshold=" + getOutofstockthreshold() +
            "}";
    }
}
