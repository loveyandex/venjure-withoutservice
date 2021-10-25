package com.venjure.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.TaxRate} entity.
 */
public class TaxRateDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Boolean enabled;

    @NotNull
    private BigDecimal value;

    private TaxCategoryDTO category;

    private ZoneDTO zone;

    private CustomerGroupDTO customergroup;

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public TaxCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(TaxCategoryDTO category) {
        this.category = category;
    }

    public ZoneDTO getZone() {
        return zone;
    }

    public void setZone(ZoneDTO zone) {
        this.zone = zone;
    }

    public CustomerGroupDTO getCustomergroup() {
        return customergroup;
    }

    public void setCustomergroup(CustomerGroupDTO customergroup) {
        this.customergroup = customergroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxRateDTO)) {
            return false;
        }

        TaxRateDTO taxRateDTO = (TaxRateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taxRateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxRateDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", name='" + getName() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", value=" + getValue() +
            ", category=" + getCategory() +
            ", zone=" + getZone() +
            ", customergroup=" + getCustomergroup() +
            "}";
    }
}
