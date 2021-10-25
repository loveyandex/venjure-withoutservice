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
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

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

    @Column(name = "deletedat")
    private Instant deletedat;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne
    private Asset featuredasset;

    @OneToMany(mappedBy = "product")
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

    @ManyToMany(mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @ManyToMany(mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "facet", "channels", "products", "facetValueTranslations", "productVariants" }, allowSetters = true)
    private Set<FacetValue> facetValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Product createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Product updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Instant getDeletedat() {
        return this.deletedat;
    }

    public Product deletedat(Instant deletedat) {
        this.deletedat = deletedat;
        return this;
    }

    public void setDeletedat(Instant deletedat) {
        this.deletedat = deletedat;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public Product enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Asset getFeaturedasset() {
        return this.featuredasset;
    }

    public Product featuredasset(Asset asset) {
        this.setFeaturedasset(asset);
        return this;
    }

    public void setFeaturedasset(Asset asset) {
        this.featuredasset = asset;
    }

    public Set<ProductVariant> getProductVariants() {
        return this.productVariants;
    }

    public Product productVariants(Set<ProductVariant> productVariants) {
        this.setProductVariants(productVariants);
        return this;
    }

    public Product addProductVariant(ProductVariant productVariant) {
        this.productVariants.add(productVariant);
        productVariant.setProduct(this);
        return this;
    }

    public Product removeProductVariant(ProductVariant productVariant) {
        this.productVariants.remove(productVariant);
        productVariant.setProduct(null);
        return this;
    }

    public void setProductVariants(Set<ProductVariant> productVariants) {
        if (this.productVariants != null) {
            this.productVariants.forEach(i -> i.setProduct(null));
        }
        if (productVariants != null) {
            productVariants.forEach(i -> i.setProduct(this));
        }
        this.productVariants = productVariants;
    }

    public Set<Channel> getChannels() {
        return this.channels;
    }

    public Product channels(Set<Channel> channels) {
        this.setChannels(channels);
        return this;
    }

    public Product addChannel(Channel channel) {
        this.channels.add(channel);
        channel.getProducts().add(this);
        return this;
    }

    public Product removeChannel(Channel channel) {
        this.channels.remove(channel);
        channel.getProducts().remove(this);
        return this;
    }

    public void setChannels(Set<Channel> channels) {
        if (this.channels != null) {
            this.channels.forEach(i -> i.removeProduct(this));
        }
        if (channels != null) {
            channels.forEach(i -> i.addProduct(this));
        }
        this.channels = channels;
    }

    public Set<FacetValue> getFacetValues() {
        return this.facetValues;
    }

    public Product facetValues(Set<FacetValue> facetValues) {
        this.setFacetValues(facetValues);
        return this;
    }

    public Product addFacetValue(FacetValue facetValue) {
        this.facetValues.add(facetValue);
        facetValue.getProducts().add(this);
        return this;
    }

    public Product removeFacetValue(FacetValue facetValue) {
        this.facetValues.remove(facetValue);
        facetValue.getProducts().remove(this);
        return this;
    }

    public void setFacetValues(Set<FacetValue> facetValues) {
        if (this.facetValues != null) {
            this.facetValues.forEach(i -> i.removeProduct(this));
        }
        if (facetValues != null) {
            facetValues.forEach(i -> i.addProduct(this));
        }
        this.facetValues = facetValues;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", enabled='" + getEnabled() + "'" +
            "}";
    }
}
