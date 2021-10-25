package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Zone} entity.
 */
public class ZoneDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String name;

    private Set<CountryDTO> countries = new HashSet<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(Set<CountryDTO> countries) {
        this.countries = countries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZoneDTO)) {
            return false;
        }

        ZoneDTO zoneDTO = (ZoneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, zoneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZoneDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", name='" + getName() + "'" +
            ", countries=" + getCountries() +
            "}";
    }
}
