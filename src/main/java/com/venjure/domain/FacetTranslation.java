package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FacetTranslation.
 */
@Entity
@Table(name = "facet_translation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FacetTranslation implements Serializable {

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
    @Column(name = "languagecode", length = 255, nullable = false)
    private String languagecode;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = { "channels", "facetTranslations", "facetValues" }, allowSetters = true)
    private Facet base;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FacetTranslation id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public FacetTranslation createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public FacetTranslation updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getLanguagecode() {
        return this.languagecode;
    }

    public FacetTranslation languagecode(String languagecode) {
        this.languagecode = languagecode;
        return this;
    }

    public void setLanguagecode(String languagecode) {
        this.languagecode = languagecode;
    }

    public String getName() {
        return this.name;
    }

    public FacetTranslation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Facet getBase() {
        return this.base;
    }

    public FacetTranslation base(Facet facet) {
        this.setBase(facet);
        return this;
    }

    public void setBase(Facet facet) {
        this.base = facet;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacetTranslation)) {
            return false;
        }
        return id != null && id.equals(((FacetTranslation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacetTranslation{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", languagecode='" + getLanguagecode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
