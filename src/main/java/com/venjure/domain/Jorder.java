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
 * A Jorder.
 */
@Entity
@Table(name = "jorder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Jorder implements Serializable {

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
    @Column(name = "code", length = 255, nullable = false)
    private String code;

    @NotNull
    @Size(max = 255)
    @Column(name = "state", length = 255, nullable = false)
    private String state;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "orderplacedat")
    private Instant orderplacedat;

    @NotNull
    @Size(max = 255)
    @Column(name = "couponcodes", length = 255, nullable = false)
    private String couponcodes;

    @NotNull
    @Size(max = 255)
    @Column(name = "shippingaddress", length = 255, nullable = false)
    private String shippingaddress;

    @NotNull
    @Size(max = 255)
    @Column(name = "billingaddress", length = 255, nullable = false)
    private String billingaddress;

    @NotNull
    @Size(max = 255)
    @Column(name = "currencycode", length = 255, nullable = false)
    private String currencycode;

    @NotNull
    @Column(name = "subtotal", nullable = false)
    private Integer subtotal;

    @NotNull
    @Column(name = "subtotalwithtax", nullable = false)
    private Integer subtotalwithtax;

    @NotNull
    @Column(name = "shipping", nullable = false)
    private Integer shipping;

    @NotNull
    @Column(name = "shippingwithtax", nullable = false)
    private Integer shippingwithtax;

    @Column(name = "taxzoneid")
    private Integer taxzoneid;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "avatar", "channels", "customerGroups", "addresses", "historyEntries", "jorders" },
        allowSetters = true
    )
    private Customer customer;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_jorder__channel",
        joinColumns = @JoinColumn(name = "jorder_id"),
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

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_jorder__promotion",
        joinColumns = @JoinColumn(name = "jorder_id"),
        inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    @JsonIgnoreProperties(value = { "jorders", "channels" }, allowSetters = true)
    private Set<Promotion> promotions = new HashSet<>();

    @OneToMany(mappedBy = "jorder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "administrator", "customer", "jorder" }, allowSetters = true)
    private Set<HistoryEntry> historyEntries = new HashSet<>();

    @OneToMany(mappedBy = "jorder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "productvariant", "taxcategory", "featuredAsset", "jorder", "orderItems", "stockMovements" },
        allowSetters = true
    )
    private Set<OrderLine> orderLines = new HashSet<>();

    @OneToMany(mappedBy = "jorder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payment", "refund", "jorder", "surcharges", "orderItems" }, allowSetters = true)
    private Set<OrderModification> orderModifications = new HashSet<>();

    @OneToMany(mappedBy = "jorder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jorder", "refunds" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "jorder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "shippingmethod", "jorder" }, allowSetters = true)
    private Set<ShippingLine> shippingLines = new HashSet<>();

    @OneToMany(mappedBy = "jorder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jorder", "ordermodification" }, allowSetters = true)
    private Set<Surcharge> surcharges = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jorder id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Jorder createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Jorder updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getCode() {
        return this.code;
    }

    public Jorder code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return this.state;
    }

    public Jorder state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Jorder active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getOrderplacedat() {
        return this.orderplacedat;
    }

    public Jorder orderplacedat(Instant orderplacedat) {
        this.orderplacedat = orderplacedat;
        return this;
    }

    public void setOrderplacedat(Instant orderplacedat) {
        this.orderplacedat = orderplacedat;
    }

    public String getCouponcodes() {
        return this.couponcodes;
    }

    public Jorder couponcodes(String couponcodes) {
        this.couponcodes = couponcodes;
        return this;
    }

    public void setCouponcodes(String couponcodes) {
        this.couponcodes = couponcodes;
    }

    public String getShippingaddress() {
        return this.shippingaddress;
    }

    public Jorder shippingaddress(String shippingaddress) {
        this.shippingaddress = shippingaddress;
        return this;
    }

    public void setShippingaddress(String shippingaddress) {
        this.shippingaddress = shippingaddress;
    }

    public String getBillingaddress() {
        return this.billingaddress;
    }

    public Jorder billingaddress(String billingaddress) {
        this.billingaddress = billingaddress;
        return this;
    }

    public void setBillingaddress(String billingaddress) {
        this.billingaddress = billingaddress;
    }

    public String getCurrencycode() {
        return this.currencycode;
    }

    public Jorder currencycode(String currencycode) {
        this.currencycode = currencycode;
        return this;
    }

    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode;
    }

    public Integer getSubtotal() {
        return this.subtotal;
    }

    public Jorder subtotal(Integer subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getSubtotalwithtax() {
        return this.subtotalwithtax;
    }

    public Jorder subtotalwithtax(Integer subtotalwithtax) {
        this.subtotalwithtax = subtotalwithtax;
        return this;
    }

    public void setSubtotalwithtax(Integer subtotalwithtax) {
        this.subtotalwithtax = subtotalwithtax;
    }

    public Integer getShipping() {
        return this.shipping;
    }

    public Jorder shipping(Integer shipping) {
        this.shipping = shipping;
        return this;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public Integer getShippingwithtax() {
        return this.shippingwithtax;
    }

    public Jorder shippingwithtax(Integer shippingwithtax) {
        this.shippingwithtax = shippingwithtax;
        return this;
    }

    public void setShippingwithtax(Integer shippingwithtax) {
        this.shippingwithtax = shippingwithtax;
    }

    public Integer getTaxzoneid() {
        return this.taxzoneid;
    }

    public Jorder taxzoneid(Integer taxzoneid) {
        this.taxzoneid = taxzoneid;
        return this;
    }

    public void setTaxzoneid(Integer taxzoneid) {
        this.taxzoneid = taxzoneid;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Jorder customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Channel> getChannels() {
        return this.channels;
    }

    public Jorder channels(Set<Channel> channels) {
        this.setChannels(channels);
        return this;
    }

    public Jorder addChannel(Channel channel) {
        this.channels.add(channel);
        channel.getJorders().add(this);
        return this;
    }

    public Jorder removeChannel(Channel channel) {
        this.channels.remove(channel);
        channel.getJorders().remove(this);
        return this;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public Set<Promotion> getPromotions() {
        return this.promotions;
    }

    public Jorder promotions(Set<Promotion> promotions) {
        this.setPromotions(promotions);
        return this;
    }

    public Jorder addPromotion(Promotion promotion) {
        this.promotions.add(promotion);
        promotion.getJorders().add(this);
        return this;
    }

    public Jorder removePromotion(Promotion promotion) {
        this.promotions.remove(promotion);
        promotion.getJorders().remove(this);
        return this;
    }

    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Set<HistoryEntry> getHistoryEntries() {
        return this.historyEntries;
    }

    public Jorder historyEntries(Set<HistoryEntry> historyEntries) {
        this.setHistoryEntries(historyEntries);
        return this;
    }

    public Jorder addHistoryEntry(HistoryEntry historyEntry) {
        this.historyEntries.add(historyEntry);
        historyEntry.setJorder(this);
        return this;
    }

    public Jorder removeHistoryEntry(HistoryEntry historyEntry) {
        this.historyEntries.remove(historyEntry);
        historyEntry.setJorder(null);
        return this;
    }

    public void setHistoryEntries(Set<HistoryEntry> historyEntries) {
        if (this.historyEntries != null) {
            this.historyEntries.forEach(i -> i.setJorder(null));
        }
        if (historyEntries != null) {
            historyEntries.forEach(i -> i.setJorder(this));
        }
        this.historyEntries = historyEntries;
    }

    public Set<OrderLine> getOrderLines() {
        return this.orderLines;
    }

    public Jorder orderLines(Set<OrderLine> orderLines) {
        this.setOrderLines(orderLines);
        return this;
    }

    public Jorder addOrderLine(OrderLine orderLine) {
        this.orderLines.add(orderLine);
        orderLine.setJorder(this);
        return this;
    }

    public Jorder removeOrderLine(OrderLine orderLine) {
        this.orderLines.remove(orderLine);
        orderLine.setJorder(null);
        return this;
    }

    public void setOrderLines(Set<OrderLine> orderLines) {
        if (this.orderLines != null) {
            this.orderLines.forEach(i -> i.setJorder(null));
        }
        if (orderLines != null) {
            orderLines.forEach(i -> i.setJorder(this));
        }
        this.orderLines = orderLines;
    }

    public Set<OrderModification> getOrderModifications() {
        return this.orderModifications;
    }

    public Jorder orderModifications(Set<OrderModification> orderModifications) {
        this.setOrderModifications(orderModifications);
        return this;
    }

    public Jorder addOrderModification(OrderModification orderModification) {
        this.orderModifications.add(orderModification);
        orderModification.setJorder(this);
        return this;
    }

    public Jorder removeOrderModification(OrderModification orderModification) {
        this.orderModifications.remove(orderModification);
        orderModification.setJorder(null);
        return this;
    }

    public void setOrderModifications(Set<OrderModification> orderModifications) {
        if (this.orderModifications != null) {
            this.orderModifications.forEach(i -> i.setJorder(null));
        }
        if (orderModifications != null) {
            orderModifications.forEach(i -> i.setJorder(this));
        }
        this.orderModifications = orderModifications;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public Jorder payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Jorder addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setJorder(this);
        return this;
    }

    public Jorder removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setJorder(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setJorder(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setJorder(this));
        }
        this.payments = payments;
    }

    public Set<ShippingLine> getShippingLines() {
        return this.shippingLines;
    }

    public Jorder shippingLines(Set<ShippingLine> shippingLines) {
        this.setShippingLines(shippingLines);
        return this;
    }

    public Jorder addShippingLine(ShippingLine shippingLine) {
        this.shippingLines.add(shippingLine);
        shippingLine.setJorder(this);
        return this;
    }

    public Jorder removeShippingLine(ShippingLine shippingLine) {
        this.shippingLines.remove(shippingLine);
        shippingLine.setJorder(null);
        return this;
    }

    public void setShippingLines(Set<ShippingLine> shippingLines) {
        if (this.shippingLines != null) {
            this.shippingLines.forEach(i -> i.setJorder(null));
        }
        if (shippingLines != null) {
            shippingLines.forEach(i -> i.setJorder(this));
        }
        this.shippingLines = shippingLines;
    }

    public Set<Surcharge> getSurcharges() {
        return this.surcharges;
    }

    public Jorder surcharges(Set<Surcharge> surcharges) {
        this.setSurcharges(surcharges);
        return this;
    }

    public Jorder addSurcharge(Surcharge surcharge) {
        this.surcharges.add(surcharge);
        surcharge.setJorder(this);
        return this;
    }

    public Jorder removeSurcharge(Surcharge surcharge) {
        this.surcharges.remove(surcharge);
        surcharge.setJorder(null);
        return this;
    }

    public void setSurcharges(Set<Surcharge> surcharges) {
        if (this.surcharges != null) {
            this.surcharges.forEach(i -> i.setJorder(null));
        }
        if (surcharges != null) {
            surcharges.forEach(i -> i.setJorder(this));
        }
        this.surcharges = surcharges;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Jorder)) {
            return false;
        }
        return id != null && id.equals(((Jorder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Jorder{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", code='" + getCode() + "'" +
            ", state='" + getState() + "'" +
            ", active='" + getActive() + "'" +
            ", orderplacedat='" + getOrderplacedat() + "'" +
            ", couponcodes='" + getCouponcodes() + "'" +
            ", shippingaddress='" + getShippingaddress() + "'" +
            ", billingaddress='" + getBillingaddress() + "'" +
            ", currencycode='" + getCurrencycode() + "'" +
            ", subtotal=" + getSubtotal() +
            ", subtotalwithtax=" + getSubtotalwithtax() +
            ", shipping=" + getShipping() +
            ", shippingwithtax=" + getShippingwithtax() +
            ", taxzoneid=" + getTaxzoneid() +
            "}";
    }
}
