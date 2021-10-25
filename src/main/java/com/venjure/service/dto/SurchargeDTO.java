package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Surcharge} entity.
 */
public class SurchargeDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String description;

    @NotNull
    private Integer listprice;

    @NotNull
    private Boolean listpriceincludestax;

    @NotNull
    @Size(max = 255)
    private String sku;

    @NotNull
    @Size(max = 255)
    private String taxlines;

    private JorderDTO jorder;

    private OrderModificationDTO ordermodification;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getListprice() {
        return listprice;
    }

    public void setListprice(Integer listprice) {
        this.listprice = listprice;
    }

    public Boolean getListpriceincludestax() {
        return listpriceincludestax;
    }

    public void setListpriceincludestax(Boolean listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTaxlines() {
        return taxlines;
    }

    public void setTaxlines(String taxlines) {
        this.taxlines = taxlines;
    }

    public JorderDTO getJorder() {
        return jorder;
    }

    public void setJorder(JorderDTO jorder) {
        this.jorder = jorder;
    }

    public OrderModificationDTO getOrdermodification() {
        return ordermodification;
    }

    public void setOrdermodification(OrderModificationDTO ordermodification) {
        this.ordermodification = ordermodification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SurchargeDTO)) {
            return false;
        }

        SurchargeDTO surchargeDTO = (SurchargeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, surchargeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SurchargeDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", description='" + getDescription() + "'" +
            ", listprice=" + getListprice() +
            ", listpriceincludestax='" + getListpriceincludestax() + "'" +
            ", sku='" + getSku() + "'" +
            ", taxlines='" + getTaxlines() + "'" +
            ", jorder=" + getJorder() +
            ", ordermodification=" + getOrdermodification() +
            "}";
    }
}
