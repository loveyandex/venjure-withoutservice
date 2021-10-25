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
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment implements Serializable {

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
    @Column(name = "method", length = 255, nullable = false)
    private String method;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @NotNull
    @Size(max = 255)
    @Column(name = "state", length = 255, nullable = false)
    private String state;

    @Size(max = 255)
    @Column(name = "errormessage", length = 255)
    private String errormessage;

    @Size(max = 255)
    @Column(name = "transactionid", length = 255)
    private String transactionid;

    @NotNull
    @Size(max = 255)
    @Column(name = "metadata", length = 255, nullable = false)
    private String metadata;

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

    @OneToMany(mappedBy = "payment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payment", "orderItems" }, allowSetters = true)
    private Set<Refund> refunds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Payment id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Payment createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Payment updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getMethod() {
        return this.method;
    }

    public Payment method(String method) {
        this.method = method;
        return this;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public Payment amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getState() {
        return this.state;
    }

    public Payment state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getErrormessage() {
        return this.errormessage;
    }

    public Payment errormessage(String errormessage) {
        this.errormessage = errormessage;
        return this;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public String getTransactionid() {
        return this.transactionid;
    }

    public Payment transactionid(String transactionid) {
        this.transactionid = transactionid;
        return this;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public Payment metadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Jorder getJorder() {
        return this.jorder;
    }

    public Payment jorder(Jorder jorder) {
        this.setJorder(jorder);
        return this;
    }

    public void setJorder(Jorder jorder) {
        this.jorder = jorder;
    }

    public Set<Refund> getRefunds() {
        return this.refunds;
    }

    public Payment refunds(Set<Refund> refunds) {
        this.setRefunds(refunds);
        return this;
    }

    public Payment addRefund(Refund refund) {
        this.refunds.add(refund);
        refund.setPayment(this);
        return this;
    }

    public Payment removeRefund(Refund refund) {
        this.refunds.remove(refund);
        refund.setPayment(null);
        return this;
    }

    public void setRefunds(Set<Refund> refunds) {
        if (this.refunds != null) {
            this.refunds.forEach(i -> i.setPayment(null));
        }
        if (refunds != null) {
            refunds.forEach(i -> i.setPayment(this));
        }
        this.refunds = refunds;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", method='" + getMethod() + "'" +
            ", amount=" + getAmount() +
            ", state='" + getState() + "'" +
            ", errormessage='" + getErrormessage() + "'" +
            ", transactionid='" + getTransactionid() + "'" +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
