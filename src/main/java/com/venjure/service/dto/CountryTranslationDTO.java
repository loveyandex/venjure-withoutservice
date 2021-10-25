package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.CountryTranslation} entity.
 */
public class CountryTranslationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String languagecode;

    @NotNull
    @Size(max = 255)
    private String name;

    private CountryDTO base;

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

    public String getLanguagecode() {
        return languagecode;
    }

    public void setLanguagecode(String languagecode) {
        this.languagecode = languagecode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryDTO getBase() {
        return base;
    }

    public void setBase(CountryDTO base) {
        this.base = base;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryTranslationDTO)) {
            return false;
        }

        CountryTranslationDTO countryTranslationDTO = (CountryTranslationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countryTranslationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryTranslationDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", languagecode='" + getLanguagecode() + "'" +
            ", name='" + getName() + "'" +
            ", base=" + getBase() +
            "}";
    }
}
