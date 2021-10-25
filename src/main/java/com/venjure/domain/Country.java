package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Country implements Serializable {

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
    @Column(name = "code", length = 255, nullable = false)
    private String code;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "base")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Set<CountryTranslation> countryTranslations = new HashSet<>();

    @ManyToMany(mappedBy = "countries")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "countries" }, allowSetters = true)
    private Set<Zone> zones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Country createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Country updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getCode() {
        return this.code;
    }

    public Country code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public Country enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<CountryTranslation> getCountryTranslations() {
        return this.countryTranslations;
    }

    public Country countryTranslations(Set<CountryTranslation> countryTranslations) {
        this.setCountryTranslations(countryTranslations);
        return this;
    }

    public Country addCountryTranslation(CountryTranslation countryTranslation) {
        this.countryTranslations.add(countryTranslation);
        countryTranslation.setBase(this);
        return this;
    }

    public Country removeCountryTranslation(CountryTranslation countryTranslation) {
        this.countryTranslations.remove(countryTranslation);
        countryTranslation.setBase(null);
        return this;
    }

    public void setCountryTranslations(Set<CountryTranslation> countryTranslations) {
        if (this.countryTranslations != null) {
            this.countryTranslations.forEach(i -> i.setBase(null));
        }
        if (countryTranslations != null) {
            countryTranslations.forEach(i -> i.setBase(this));
        }
        this.countryTranslations = countryTranslations;
    }

    public Set<Zone> getZones() {
        return this.zones;
    }

    public Country zones(Set<Zone> zones) {
        this.setZones(zones);
        return this;
    }

    public Country addZone(Zone zone) {
        this.zones.add(zone);
        zone.getCountries().add(this);
        return this;
    }

    public Country removeZone(Zone zone) {
        this.zones.remove(zone);
        zone.getCountries().remove(this);
        return this;
    }

    public void setZones(Set<Zone> zones) {
        if (this.zones != null) {
            this.zones.forEach(i -> i.removeCountry(this));
        }
        if (zones != null) {
            zones.forEach(i -> i.addCountry(this));
        }
        this.zones = zones;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", code='" + getCode() + "'" +
            ", enabled='" + getEnabled() + "'" +
            "}";
    }
}
