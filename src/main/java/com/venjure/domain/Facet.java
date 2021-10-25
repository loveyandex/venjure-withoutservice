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
 * A Facet.
 */
@Entity
@Table(name = "facet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Facet implements Serializable {

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
    @Column(name = "isprivate", nullable = false)
    private Boolean isprivate;

    @NotNull
    @Size(max = 255)
    @Column(name = "code", length = 255, nullable = false, unique = true)
    private String code;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_facet__channel",
        joinColumns = @JoinColumn(name = "facet_id"),
        inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    @JsonIgnoreProperties(
        value = {
            "defaulttaxzone",
            "defaultshippingzone",
            "paymentMethods",
            "products",
            "promotions",
            "shippingMethods",
            "customers",
            "facets",
            "facetValues",
            "jorders",
            "productVariants",
        },
        allowSetters = true
    )
    private Set<Channel> channels = new HashSet<>();

    @OneToMany(mappedBy = "base")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Set<FacetTranslation> facetTranslations = new HashSet<>();

    @OneToMany(mappedBy = "facet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "facet", "channels", "products", "facetValueTranslations", "productVariants" }, allowSetters = true)
    private Set<FacetValue> facetValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Facet id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Facet createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Facet updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Boolean getIsprivate() {
        return this.isprivate;
    }

    public Facet isprivate(Boolean isprivate) {
        this.isprivate = isprivate;
        return this;
    }

    public void setIsprivate(Boolean isprivate) {
        this.isprivate = isprivate;
    }

    public String getCode() {
        return this.code;
    }

    public Facet code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Channel> getChannels() {
        return this.channels;
    }

    public Facet channels(Set<Channel> channels) {
        this.setChannels(channels);
        return this;
    }

    public Facet addChannel(Channel channel) {
        this.channels.add(channel);
        channel.getFacets().add(this);
        return this;
    }

    public Facet removeChannel(Channel channel) {
        this.channels.remove(channel);
        channel.getFacets().remove(this);
        return this;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public Set<FacetTranslation> getFacetTranslations() {
        return this.facetTranslations;
    }

    public Facet facetTranslations(Set<FacetTranslation> facetTranslations) {
        this.setFacetTranslations(facetTranslations);
        return this;
    }

    public Facet addFacetTranslation(FacetTranslation facetTranslation) {
        this.facetTranslations.add(facetTranslation);
        facetTranslation.setBase(this);
        return this;
    }

    public Facet removeFacetTranslation(FacetTranslation facetTranslation) {
        this.facetTranslations.remove(facetTranslation);
        facetTranslation.setBase(null);
        return this;
    }

    public void setFacetTranslations(Set<FacetTranslation> facetTranslations) {
        if (this.facetTranslations != null) {
            this.facetTranslations.forEach(i -> i.setBase(null));
        }
        if (facetTranslations != null) {
            facetTranslations.forEach(i -> i.setBase(this));
        }
        this.facetTranslations = facetTranslations;
    }

    public Set<FacetValue> getFacetValues() {
        return this.facetValues;
    }

    public Facet facetValues(Set<FacetValue> facetValues) {
        this.setFacetValues(facetValues);
        return this;
    }

    public Facet addFacetValue(FacetValue facetValue) {
        this.facetValues.add(facetValue);
        facetValue.setFacet(this);
        return this;
    }

    public Facet removeFacetValue(FacetValue facetValue) {
        this.facetValues.remove(facetValue);
        facetValue.setFacet(null);
        return this;
    }

    public void setFacetValues(Set<FacetValue> facetValues) {
        if (this.facetValues != null) {
            this.facetValues.forEach(i -> i.setFacet(null));
        }
        if (facetValues != null) {
            facetValues.forEach(i -> i.setFacet(this));
        }
        this.facetValues = facetValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facet)) {
            return false;
        }
        return id != null && id.equals(((Facet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facet{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", isprivate='" + getIsprivate() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
