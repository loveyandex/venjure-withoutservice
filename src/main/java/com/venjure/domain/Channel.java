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
 * A Channel.
 */
@Entity
@Table(name = "channel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Channel implements Serializable {

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
    @Column(name = "code", length = 255, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 255)
    @Column(name = "token", length = 255, nullable = false, unique = true)
    private String token;

    @NotNull
    @Size(max = 255)
    @Column(name = "defaultlanguagecode", length = 255, nullable = false)
    private String defaultlanguagecode;

    @NotNull
    @Size(max = 255)
    @Column(name = "currencycode", length = 255, nullable = false)
    private String currencycode;

    @NotNull
    @Column(name = "pricesincludetax", nullable = false)
    private Boolean pricesincludetax;

    @ManyToOne
    @JsonIgnoreProperties(value = { "countries" }, allowSetters = true)
    private Zone defaulttaxzone;

    @ManyToOne
    @JsonIgnoreProperties(value = { "countries" }, allowSetters = true)
    private Zone defaultshippingzone;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_channel__payment_method",
        joinColumns = @JoinColumn(name = "channel_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_method_id")
    )
    @JsonIgnoreProperties(value = { "channels" }, allowSetters = true)
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_channel__product",
        joinColumns = @JoinColumn(name = "channel_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnoreProperties(value = { "featuredasset", "productVariants", "channels", "facetValues" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_channel__promotion",
        joinColumns = @JoinColumn(name = "channel_id"),
        inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    @JsonIgnoreProperties(value = { "jorders", "channels" }, allowSetters = true)
    private Set<Promotion> promotions = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_channel__shipping_method",
        joinColumns = @JoinColumn(name = "channel_id"),
        inverseJoinColumns = @JoinColumn(name = "shipping_method_id")
    )
    @JsonIgnoreProperties(value = { "shippingMethodTranslations", "channels" }, allowSetters = true)
    private Set<ShippingMethod> shippingMethods = new HashSet<>();

    @ManyToMany(mappedBy = "channels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "user", "avatar", "channels", "customerGroups", "addresses", "historyEntries", "jorders" },
        allowSetters = true
    )
    private Set<Customer> customers = new HashSet<>();

    @ManyToMany(mappedBy = "channels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "channels", "facetTranslations", "facetValues" }, allowSetters = true)
    private Set<Facet> facets = new HashSet<>();

    @ManyToMany(mappedBy = "channels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "facet", "channels", "products", "facetValueTranslations", "productVariants" }, allowSetters = true)
    private Set<FacetValue> facetValues = new HashSet<>();

    @ManyToMany(mappedBy = "channels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Jorder> jorders = new HashSet<>();

    @ManyToMany(mappedBy = "channels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "product",
            "featuredasset",
            "taxcategory",
            "channels",
            "productVariants",
            "facetValues",
            "productOptions",
            "productVariantAssets",
            "productVariantPrices",
            "productVariantTranslations",
            "stockMovements",
        },
        allowSetters = true
    )
    private Set<ProductVariant> productVariants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Channel id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Channel createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Channel updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getCode() {
        return this.code;
    }

    public Channel code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return this.token;
    }

    public Channel token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDefaultlanguagecode() {
        return this.defaultlanguagecode;
    }

    public Channel defaultlanguagecode(String defaultlanguagecode) {
        this.defaultlanguagecode = defaultlanguagecode;
        return this;
    }

    public void setDefaultlanguagecode(String defaultlanguagecode) {
        this.defaultlanguagecode = defaultlanguagecode;
    }

    public String getCurrencycode() {
        return this.currencycode;
    }

    public Channel currencycode(String currencycode) {
        this.currencycode = currencycode;
        return this;
    }

    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode;
    }

    public Boolean getPricesincludetax() {
        return this.pricesincludetax;
    }

    public Channel pricesincludetax(Boolean pricesincludetax) {
        this.pricesincludetax = pricesincludetax;
        return this;
    }

    public void setPricesincludetax(Boolean pricesincludetax) {
        this.pricesincludetax = pricesincludetax;
    }

    public Zone getDefaulttaxzone() {
        return this.defaulttaxzone;
    }

    public Channel defaulttaxzone(Zone zone) {
        this.setDefaulttaxzone(zone);
        return this;
    }

    public void setDefaulttaxzone(Zone zone) {
        this.defaulttaxzone = zone;
    }

    public Zone getDefaultshippingzone() {
        return this.defaultshippingzone;
    }

    public Channel defaultshippingzone(Zone zone) {
        this.setDefaultshippingzone(zone);
        return this;
    }

    public void setDefaultshippingzone(Zone zone) {
        this.defaultshippingzone = zone;
    }

    public Set<PaymentMethod> getPaymentMethods() {
        return this.paymentMethods;
    }

    public Channel paymentMethods(Set<PaymentMethod> paymentMethods) {
        this.setPaymentMethods(paymentMethods);
        return this;
    }

    public Channel addPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.add(paymentMethod);
        paymentMethod.getChannels().add(this);
        return this;
    }

    public Channel removePaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.remove(paymentMethod);
        paymentMethod.getChannels().remove(this);
        return this;
    }

    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public Channel products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Channel addProduct(Product product) {
        this.products.add(product);
        product.getChannels().add(this);
        return this;
    }

    public Channel removeProduct(Product product) {
        this.products.remove(product);
        product.getChannels().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<Promotion> getPromotions() {
        return this.promotions;
    }

    public Channel promotions(Set<Promotion> promotions) {
        this.setPromotions(promotions);
        return this;
    }

    public Channel addPromotion(Promotion promotion) {
        this.promotions.add(promotion);
        promotion.getChannels().add(this);
        return this;
    }

    public Channel removePromotion(Promotion promotion) {
        this.promotions.remove(promotion);
        promotion.getChannels().remove(this);
        return this;
    }

    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Set<ShippingMethod> getShippingMethods() {
        return this.shippingMethods;
    }

    public Channel shippingMethods(Set<ShippingMethod> shippingMethods) {
        this.setShippingMethods(shippingMethods);
        return this;
    }

    public Channel addShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethods.add(shippingMethod);
        shippingMethod.getChannels().add(this);
        return this;
    }

    public Channel removeShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethods.remove(shippingMethod);
        shippingMethod.getChannels().remove(this);
        return this;
    }

    public void setShippingMethods(Set<ShippingMethod> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public Channel customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public Channel addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.getChannels().add(this);
        return this;
    }

    public Channel removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.getChannels().remove(this);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.removeChannel(this));
        }
        if (customers != null) {
            customers.forEach(i -> i.addChannel(this));
        }
        this.customers = customers;
    }

    public Set<Facet> getFacets() {
        return this.facets;
    }

    public Channel facets(Set<Facet> facets) {
        this.setFacets(facets);
        return this;
    }

    public Channel addFacet(Facet facet) {
        this.facets.add(facet);
        facet.getChannels().add(this);
        return this;
    }

    public Channel removeFacet(Facet facet) {
        this.facets.remove(facet);
        facet.getChannels().remove(this);
        return this;
    }

    public void setFacets(Set<Facet> facets) {
        if (this.facets != null) {
            this.facets.forEach(i -> i.removeChannel(this));
        }
        if (facets != null) {
            facets.forEach(i -> i.addChannel(this));
        }
        this.facets = facets;
    }

    public Set<FacetValue> getFacetValues() {
        return this.facetValues;
    }

    public Channel facetValues(Set<FacetValue> facetValues) {
        this.setFacetValues(facetValues);
        return this;
    }

    public Channel addFacetValue(FacetValue facetValue) {
        this.facetValues.add(facetValue);
        facetValue.getChannels().add(this);
        return this;
    }

    public Channel removeFacetValue(FacetValue facetValue) {
        this.facetValues.remove(facetValue);
        facetValue.getChannels().remove(this);
        return this;
    }

    public void setFacetValues(Set<FacetValue> facetValues) {
        if (this.facetValues != null) {
            this.facetValues.forEach(i -> i.removeChannel(this));
        }
        if (facetValues != null) {
            facetValues.forEach(i -> i.addChannel(this));
        }
        this.facetValues = facetValues;
    }

    public Set<Jorder> getJorders() {
        return this.jorders;
    }

    public Channel jorders(Set<Jorder> jorders) {
        this.setJorders(jorders);
        return this;
    }

    public Channel addJorder(Jorder jorder) {
        this.jorders.add(jorder);
        jorder.getChannels().add(this);
        return this;
    }

    public Channel removeJorder(Jorder jorder) {
        this.jorders.remove(jorder);
        jorder.getChannels().remove(this);
        return this;
    }

    public void setJorders(Set<Jorder> jorders) {
        if (this.jorders != null) {
            this.jorders.forEach(i -> i.removeChannel(this));
        }
        if (jorders != null) {
            jorders.forEach(i -> i.addChannel(this));
        }
        this.jorders = jorders;
    }

    public Set<ProductVariant> getProductVariants() {
        return this.productVariants;
    }

    public Channel productVariants(Set<ProductVariant> productVariants) {
        this.setProductVariants(productVariants);
        return this;
    }

    public Channel addProductVariant(ProductVariant productVariant) {
        this.productVariants.add(productVariant);
        productVariant.getChannels().add(this);
        return this;
    }

    public Channel removeProductVariant(ProductVariant productVariant) {
        this.productVariants.remove(productVariant);
        productVariant.getChannels().remove(this);
        return this;
    }

    public void setProductVariants(Set<ProductVariant> productVariants) {
        if (this.productVariants != null) {
            this.productVariants.forEach(i -> i.removeChannel(this));
        }
        if (productVariants != null) {
            productVariants.forEach(i -> i.addChannel(this));
        }
        this.productVariants = productVariants;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Channel)) {
            return false;
        }
        return id != null && id.equals(((Channel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Channel{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", code='" + getCode() + "'" +
            ", token='" + getToken() + "'" +
            ", defaultlanguagecode='" + getDefaultlanguagecode() + "'" +
            ", currencycode='" + getCurrencycode() + "'" +
            ", pricesincludetax='" + getPricesincludetax() + "'" +
            "}";
    }
}
