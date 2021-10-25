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
 * A TaxCategory.
 */
@Entity
@Table(name = "tax_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaxCategory implements Serializable {

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
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Column(name = "isdefault", nullable = false)
    private Boolean isdefault;

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "zone", "customergroup" }, allowSetters = true)
    private Set<TaxRate> taxRates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaxCategory id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public TaxCategory createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public TaxCategory updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getName() {
        return this.name;
    }

    public TaxCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsdefault() {
        return this.isdefault;
    }

    public TaxCategory isdefault(Boolean isdefault) {
        this.isdefault = isdefault;
        return this;
    }

    public void setIsdefault(Boolean isdefault) {
        this.isdefault = isdefault;
    }

    public Set<TaxRate> getTaxRates() {
        return this.taxRates;
    }

    public TaxCategory taxRates(Set<TaxRate> taxRates) {
        this.setTaxRates(taxRates);
        return this;
    }

    public TaxCategory addTaxRate(TaxRate taxRate) {
        this.taxRates.add(taxRate);
        taxRate.setCategory(this);
        return this;
    }

    public TaxCategory removeTaxRate(TaxRate taxRate) {
        this.taxRates.remove(taxRate);
        taxRate.setCategory(null);
        return this;
    }

    public void setTaxRates(Set<TaxRate> taxRates) {
        if (this.taxRates != null) {
            this.taxRates.forEach(i -> i.setCategory(null));
        }
        if (taxRates != null) {
            taxRates.forEach(i -> i.setCategory(this));
        }
        this.taxRates = taxRates;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxCategory)) {
            return false;
        }
        return id != null && id.equals(((TaxCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxCategory{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", name='" + getName() + "'" +
            ", isdefault='" + getIsdefault() + "'" +
            "}";
    }
}
