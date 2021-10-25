package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.GlobalSettings} entity.
 */
public class GlobalSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String availablelanguages;

    @NotNull
    private Boolean trackinventory;

    @NotNull
    private Integer outofstockthreshold;

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

    public String getAvailablelanguages() {
        return availablelanguages;
    }

    public void setAvailablelanguages(String availablelanguages) {
        this.availablelanguages = availablelanguages;
    }

    public Boolean getTrackinventory() {
        return trackinventory;
    }

    public void setTrackinventory(Boolean trackinventory) {
        this.trackinventory = trackinventory;
    }

    public Integer getOutofstockthreshold() {
        return outofstockthreshold;
    }

    public void setOutofstockthreshold(Integer outofstockthreshold) {
        this.outofstockthreshold = outofstockthreshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalSettingsDTO)) {
            return false;
        }

        GlobalSettingsDTO globalSettingsDTO = (GlobalSettingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, globalSettingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlobalSettingsDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", availablelanguages='" + getAvailablelanguages() + "'" +
            ", trackinventory='" + getTrackinventory() + "'" +
            ", outofstockthreshold=" + getOutofstockthreshold() +
            "}";
    }
}
