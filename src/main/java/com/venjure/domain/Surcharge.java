package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Surcharge.
 */
@Entity
@Table(name = "surcharge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Surcharge implements Serializable {

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
    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @NotNull
    @Column(name = "listprice", nullable = false)
    private Integer listprice;

    @NotNull
    @Column(name = "listpriceincludestax", nullable = false)
    private Boolean listpriceincludestax;

    @NotNull
    @Size(max = 255)
    @Column(name = "sku", length = 255, nullable = false)
    private String sku;

    @NotNull
    @Size(max = 255)
    @Column(name = "taxlines", length = 255, nullable = false)
    private String taxlines;

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "payment", "refund", "jorder", "surcharges", "orderItems" }, allowSetters = true)
    private OrderModification ordermodification;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Surcharge id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Surcharge createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Surcharge updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getDescription() {
        return this.description;
    }

    public Surcharge description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getListprice() {
        return this.listprice;
    }

    public Surcharge listprice(Integer listprice) {
        this.listprice = listprice;
        return this;
    }

    public void setListprice(Integer listprice) {
        this.listprice = listprice;
    }

    public Boolean getListpriceincludestax() {
        return this.listpriceincludestax;
    }

    public Surcharge listpriceincludestax(Boolean listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
        return this;
    }

    public void setListpriceincludestax(Boolean listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
    }

    public String getSku() {
        return this.sku;
    }

    public Surcharge sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTaxlines() {
        return this.taxlines;
    }

    public Surcharge taxlines(String taxlines) {
        this.taxlines = taxlines;
        return this;
    }

    public void setTaxlines(String taxlines) {
        this.taxlines = taxlines;
    }

    public Jorder getJorder() {
        return this.jorder;
    }

    public Surcharge jorder(Jorder jorder) {
        this.setJorder(jorder);
        return this;
    }

    public void setJorder(Jorder jorder) {
        this.jorder = jorder;
    }

    public OrderModification getOrdermodification() {
        return this.ordermodification;
    }

    public Surcharge ordermodification(OrderModification orderModification) {
        this.setOrdermodification(orderModification);
        return this;
    }

    public void setOrdermodification(OrderModification orderModification) {
        this.ordermodification = orderModification;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Surcharge)) {
            return false;
        }
        return id != null && id.equals(((Surcharge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Surcharge{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", description='" + getDescription() + "'" +
            ", listprice=" + getListprice() +
            ", listpriceincludestax='" + getListpriceincludestax() + "'" +
            ", sku='" + getSku() + "'" +
            ", taxlines='" + getTaxlines() + "'" +
            "}";
    }
}
