package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HistoryEntry.
 */
@Entity
@Table(name = "history_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HistoryEntry implements Serializable {

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
    @Column(name = "type", length = 255, nullable = false)
    private String type;

    @NotNull
    @Column(name = "ispublic", nullable = false)
    private Boolean ispublic;

    @NotNull
    @Size(max = 255)
    @Column(name = "data", length = 255, nullable = false)
    private String data;

    @NotNull
    @Size(max = 255)
    @Column(name = "discriminator", length = 255, nullable = false)
    private String discriminator;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "historyEntries" }, allowSetters = true)
    private Administrator administrator;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "avatar", "channels", "customerGroups", "addresses", "historyEntries", "jorders" },
        allowSetters = true
    )
    private Customer customer;

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

    public HistoryEntry id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public HistoryEntry createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public HistoryEntry updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getType() {
        return this.type;
    }

    public HistoryEntry type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIspublic() {
        return this.ispublic;
    }

    public HistoryEntry ispublic(Boolean ispublic) {
        this.ispublic = ispublic;
        return this;
    }

    public void setIspublic(Boolean ispublic) {
        this.ispublic = ispublic;
    }

    public String getData() {
        return this.data;
    }

    public HistoryEntry data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDiscriminator() {
        return this.discriminator;
    }

    public HistoryEntry discriminator(String discriminator) {
        this.discriminator = discriminator;
        return this;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public Administrator getAdministrator() {
        return this.administrator;
    }

    public HistoryEntry administrator(Administrator administrator) {
        this.setAdministrator(administrator);
        return this;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public HistoryEntry customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Jorder getJorder() {
        return this.jorder;
    }

    public HistoryEntry jorder(Jorder jorder) {
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
        if (!(o instanceof HistoryEntry)) {
            return false;
        }
        return id != null && id.equals(((HistoryEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryEntry{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", type='" + getType() + "'" +
            ", ispublic='" + getIspublic() + "'" +
            ", data='" + getData() + "'" +
            ", discriminator='" + getDiscriminator() + "'" +
            "}";
    }
}
