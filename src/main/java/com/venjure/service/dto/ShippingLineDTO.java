package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.ShippingLine} entity.
 */
public class ShippingLineDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    private Integer listprice;

    @NotNull
    private Boolean listpriceincludestax;

    @NotNull
    @Size(max = 255)
    private String adjustments;

    @NotNull
    @Size(max = 255)
    private String taxlines;

    private ShippingMethodDTO shippingmethod;

    private JorderDTO jorder;

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

    public String getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(String adjustments) {
        this.adjustments = adjustments;
    }

    public String getTaxlines() {
        return taxlines;
    }

    public void setTaxlines(String taxlines) {
        this.taxlines = taxlines;
    }

    public ShippingMethodDTO getShippingmethod() {
        return shippingmethod;
    }

    public void setShippingmethod(ShippingMethodDTO shippingmethod) {
        this.shippingmethod = shippingmethod;
    }

    public JorderDTO getJorder() {
        return jorder;
    }

    public void setJorder(JorderDTO jorder) {
        this.jorder = jorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShippingLineDTO)) {
            return false;
        }

        ShippingLineDTO shippingLineDTO = (ShippingLineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shippingLineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingLineDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", listprice=" + getListprice() +
            ", listpriceincludestax='" + getListpriceincludestax() + "'" +
            ", adjustments='" + getAdjustments() + "'" +
            ", taxlines='" + getTaxlines() + "'" +
            ", shippingmethod=" + getShippingmethod() +
            ", jorder=" + getJorder() +
            "}";
    }
}
