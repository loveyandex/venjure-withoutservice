package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShippingLine.
 */
@Entity
@Table(name = "shipping_line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShippingLine implements Serializable {

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
    @Column(name = "listprice", nullable = false)
    private Integer listprice;

    @NotNull
    @Column(name = "listpriceincludestax", nullable = false)
    private Boolean listpriceincludestax;

    @NotNull
    @Size(max = 255)
    @Column(name = "adjustments", length = 255, nullable = false)
    private String adjustments;

    @NotNull
    @Size(max = 255)
    @Column(name = "taxlines", length = 255, nullable = false)
    private String taxlines;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "shippingMethodTranslations", "channels" }, allowSetters = true)
    private ShippingMethod shippingmethod;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "customer",
            "channels",
            "promotions",
            "historyEntries",
            "orderLines",
            "orderModifications",
            "payments",
            "shippingLines",
            "surcharges",
        },
        allowSetters = true
    )
    private Jorder jorder;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShippingLine id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public ShippingLine createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public ShippingLine updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Integer getListprice() {
        return this.listprice;
    }

    public ShippingLine listprice(Integer listprice) {
        this.listprice = listprice;
        return this;
    }

    public void setListprice(Integer listprice) {
        this.listprice = listprice;
    }

    public Boolean getListpriceincludestax() {
        return this.listpriceincludestax;
    }

    public ShippingLine listpriceincludestax(Boolean listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
        return this;
    }

    public void setListpriceincludestax(Boolean listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
    }

    public String getAdjustments() {
        return this.adjustments;
    }

    public ShippingLine adjustments(String adjustments) {
        this.adjustments = adjustments;
        return this;
    }

    public void setAdjustments(String adjustments) {
        this.adjustments = adjustments;
    }

    public String getTaxlines() {
        return this.taxlines;
    }

    public ShippingLine taxlines(String taxlines) {
        this.taxlines = taxlines;
        return this;
    }

    public void setTaxlines(String taxlines) {
        this.taxlines = taxlines;
    }

    public ShippingMethod getShippingmethod() {
        return this.shippingmethod;
    }

    public ShippingLine shippingmethod(ShippingMethod shippingMethod) {
        this.setShippingmethod(shippingMethod);
        return this;
    }

    public void setShippingmethod(ShippingMethod shippingMethod) {
        this.shippingmethod = shippingMethod;
    }

    public Jorder getJorder() {
        return this.jorder;
    }

    public ShippingLine jorder(Jorder jorder) {
        this.setJorder(jorder);
        return this;
    }

    public void setJorder(Jorder jorder) {
        this.jorder = jorder;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShippingLine)) {
            return false;
        }
        return id != null && id.equals(((ShippingLine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingLine{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", listprice=" + getListprice() +
            ", listpriceincludestax='" + getListpriceincludestax() + "'" +
            ", adjustments='" + getAdjustments() + "'" +
            ", taxlines='" + getTaxlines() + "'" +
            "}";
    }
}
