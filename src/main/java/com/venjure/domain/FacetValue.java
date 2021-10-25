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
 * A FacetValue.
 */
@Entity
@Table(name = "facet_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FacetValue implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "channels", "facetTranslations", "facetValues" }, allowSetters = true)
    private Facet facet;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_facet_value__channel",
        joinColumns = @JoinColumn(name = "facet_value_id"),
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
        name = "rel_facet_value__product",
        joinColumns = @JoinColumn(name = "facet_value_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnoreProperties(value = { "featuredasset", "productVariants", "channels", "facetValues" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "base")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Set<FacetValueTranslation> facetValueTranslations = new HashSet<>();

    @ManyToMany(mappedBy = "facetValues")
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

    public FacetValue id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public FacetValue createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public FacetValue updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getCode() {
        return this.code;
    }

    public FacetValue code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Facet getFacet() {
        return this.facet;
    }

    public FacetValue facet(Facet facet) {
        this.setFacet(facet);
        return this;
    }

    public void setFacet(Facet facet) {
        this.facet = facet;
    }

    public Set<Channel> getChannels() {
        return this.channels;
    }

    public FacetValue channels(Set<Channel> channels) {
        this.setChannels(channels);
        return this;
    }

    public FacetValue addChannel(Channel channel) {
        this.channels.add(channel);
        channel.getFacetValues().add(this);
        return this;
    }

    public FacetValue removeChannel(Channel channel) {
        this.channels.remove(channel);
        channel.getFacetValues().remove(this);
        return this;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public FacetValue products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public FacetValue addProduct(Product product) {
        this.products.add(product);
        product.getFacetValues().add(this);
        return this;
    }

    public FacetValue removeProduct(Product product) {
        this.products.remove(product);
        product.getFacetValues().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<FacetValueTranslation> getFacetValueTranslations() {
        return this.facetValueTranslations;
    }

    public FacetValue facetValueTranslations(Set<FacetValueTranslation> facetValueTranslations) {
        this.setFacetValueTranslations(facetValueTranslations);
        return this;
    }

    public FacetValue addFacetValueTranslation(FacetValueTranslation facetValueTranslation) {
        this.facetValueTranslations.add(facetValueTranslation);
        facetValueTranslation.setBase(this);
        return this;
    }

    public FacetValue removeFacetValueTranslation(FacetValueTranslation facetValueTranslation) {
        this.facetValueTranslations.remove(facetValueTranslation);
        facetValueTranslation.setBase(null);
        return this;
    }

    public void setFacetValueTranslations(Set<FacetValueTranslation> facetValueTranslations) {
        if (this.facetValueTranslations != null) {
            this.facetValueTranslations.forEach(i -> i.setBase(null));
        }
        if (facetValueTranslations != null) {
            facetValueTranslations.forEach(i -> i.setBase(this));
        }
        this.facetValueTranslations = facetValueTranslations;
    }

    public Set<ProductVariant> getProductVariants() {
        return this.productVariants;
    }

    public FacetValue productVariants(Set<ProductVariant> productVariants) {
        this.setProductVariants(productVariants);
        return this;
    }

    public FacetValue addProductVariant(ProductVariant productVariant) {
        this.productVariants.add(productVariant);
        productVariant.getFacetValues().add(this);
        return this;
    }

    public FacetValue removeProductVariant(ProductVariant productVariant) {
        this.productVariants.remove(productVariant);
        productVariant.getFacetValues().remove(this);
        return this;
    }

    public void setProductVariants(Set<ProductVariant> productVariants) {
        if (this.productVariants != null) {
            this.productVariants.forEach(i -> i.removeFacetValue(this));
        }
        if (productVariants != null) {
            productVariants.forEach(i -> i.addFacetValue(this));
        }
        this.productVariants = productVariants;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacetValue)) {
            return false;
        }
        return id != null && id.equals(((FacetValue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacetValue{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
