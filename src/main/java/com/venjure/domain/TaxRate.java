package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TaxRate.
 */
@Entity
@Table(name = "tax_rate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaxRate implements Serializable {

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
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NotNull
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "taxRates" }, allowSetters = true)
    private TaxCategory category;

    @ManyToOne
    @JsonIgnoreProperties(value = { "countries" }, allowSetters = true)
    private Zone zone;

    @ManyToOne
    @JsonIgnoreProperties(value = { "customers" }, allowSetters = true)
    private CustomerGroup customergroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaxRate id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public TaxRate createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public TaxRate updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getName() {
        return this.name;
    }

    public TaxRate name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public TaxRate enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public TaxRate value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public TaxCategory getCategory() {
        return this.category;
    }

    public TaxRate category(TaxCategory taxCategory) {
        this.setCategory(taxCategory);
        return this;
    }

    public void setCategory(TaxCategory taxCategory) {
        this.category = taxCategory;
    }

    public Zone getZone() {
        return this.zone;
    }

    public TaxRate zone(Zone zone) {
        this.setZone(zone);
        return this;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public CustomerGroup getCustomergroup() {
        return this.customergroup;
    }

    public TaxRate customergroup(CustomerGroup customerGroup) {
        this.setCustomergroup(customerGroup);
        return this;
    }

    public void setCustomergroup(CustomerGroup customerGroup) {
        this.customergroup = customerGroup;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxRate)) {
            return false;
        }
        return id != null && id.equals(((TaxRate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxRate{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", name='" + getName() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
