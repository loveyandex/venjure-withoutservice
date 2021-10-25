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
 * A ProductVariant.
 */
@Entity
@Table(name = "product_variant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductVariant implements Serializable {

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

    @NotNull
    @Size(max = 255)
    @Column(name = "sku", length = 255, nullable = false)
    private String sku;

    @NotNull
    @Column(name = "stockonhand", nullable = false)
    private Integer stockonhand;

    @NotNull
    @Column(name = "stockallocated", nullable = false)
    private Integer stockallocated;

    @NotNull
    @Column(name = "outofstockthreshold", nullable = false)
    private Integer outofstockthreshold;

    @NotNull
    @Column(name = "useglobaloutofstockthreshold", nullable = false)
    private Boolean useglobaloutofstockthreshold;

    @NotNull
    @Size(max = 255)
    @Column(name = "trackinventory", length = 255, nullable = false)
    private String trackinventory;

    @ManyToOne
    @JsonIgnoreProperties(value = { "featuredasset", "productVariants", "channels", "facetValues" }, allowSetters = true)
    private Product product;

    @ManyToOne
    private Asset featuredasset;

    @ManyToOne
    @JsonIgnoreProperties(value = { "taxRates" }, allowSetters = true)
    private TaxCategory taxcategory;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_product_variant__channel",
        joinColumns = @JoinColumn(name = "product_variant_id"),
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
        name = "rel_product_variant__product_variants",
        joinColumns = @JoinColumn(name = "product_variant_id"),
        inverseJoinColumns = @JoinColumn(name = "product_variants_id")
    )
    @JsonIgnoreProperties(value = { "featuredasset", "parent", "collectionTranslations", "productvariants" }, allowSetters = true)
    private Set<Collection> productVariants = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_product_variant__facet_value",
        joinColumns = @JoinColumn(name = "product_variant_id"),
        inverseJoinColumns = @JoinColumn(name = "facet_value_id")
    )
    @JsonIgnoreProperties(value = { "facet", "channels", "products", "facetValueTranslations", "productVariants" }, allowSetters = true)
    private Set<FacetValue> facetValues = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_product_variant__product_option",
        joinColumns = @JoinColumn(name = "product_variant_id"),
        inverseJoinColumns = @JoinColumn(name = "product_option_id")
    )
    @JsonIgnoreProperties(value = { "group", "productOptionTranslations", "productVariants" }, allowSetters = true)
    private Set<ProductOption> productOptions = new HashSet<>();

    @OneToMany(mappedBy = "productvariant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "asset", "productvariant" }, allowSetters = true)
    private Set<ProductVariantAsset> productVariantAssets = new HashSet<>();

    @OneToMany(mappedBy = "variant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "variant" }, allowSetters = true)
    private Set<ProductVariantPrice> productVariantPrices = new HashSet<>();

    @OneToMany(mappedBy = "base")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Set<ProductVariantTranslation> productVariantTranslations = new HashSet<>();

    @OneToMany(mappedBy = "productvariant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productvariant", "orderitem", "orderline" }, allowSetters = true)
    private Set<StockMovement> stockMovements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductVariant id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public ProductVariant createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public ProductVariant updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Instant getDeletedat() {
        return this.deletedat;
    }

    public ProductVariant deletedat(Instant deletedat) {
        this.deletedat = deletedat;
        return this;
    }

    public void setDeletedat(Instant deletedat) {
        this.deletedat = deletedat;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public ProductVariant enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSku() {
        return this.sku;
    }

    public ProductVariant sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getStockonhand() {
        return this.stockonhand;
    }

    public ProductVariant stockonhand(Integer stockonhand) {
        this.stockonhand = stockonhand;
        return this;
    }

    public void setStockonhand(Integer stockonhand) {
        this.stockonhand = stockonhand;
    }

    public Integer getStockallocated() {
        return this.stockallocated;
    }

    public ProductVariant stockallocated(Integer stockallocated) {
        this.stockallocated = stockallocated;
        return this;
    }

    public void setStockallocated(Integer stockallocated) {
        this.stockallocated = stockallocated;
    }

    public Integer getOutofstockthreshold() {
        return this.outofstockthreshold;
    }

    public ProductVariant outofstockthreshold(Integer outofstockthreshold) {
        this.outofstockthreshold = outofstockthreshold;
        return this;
    }

    public void setOutofstockthreshold(Integer outofstockthreshold) {
        this.outofstockthreshold = outofstockthreshold;
    }

    public Boolean getUseglobaloutofstockthreshold() {
        return this.useglobaloutofstockthreshold;
    }

    public ProductVariant useglobaloutofstockthreshold(Boolean useglobaloutofstockthreshold) {
        this.useglobaloutofstockthreshold = useglobaloutofstockthreshold;
        return this;
    }

    public void setUseglobaloutofstockthreshold(Boolean useglobaloutofstockthreshold) {
        this.useglobaloutofstockthreshold = useglobaloutofstockthreshold;
    }

    public String getTrackinventory() {
        return this.trackinventory;
    }

    public ProductVariant trackinventory(String trackinventory) {
        this.trackinventory = trackinventory;
        return this;
    }

    public void setTrackinventory(String trackinventory) {
        this.trackinventory = trackinventory;
    }

    public Product getProduct() {
        return this.product;
    }

    public ProductVariant product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Asset getFeaturedasset() {
        return this.featuredasset;
    }

    public ProductVariant featuredasset(Asset asset) {
        this.setFeaturedasset(asset);
        return this;
    }

    public void setFeaturedasset(Asset asset) {
        this.featuredasset = asset;
    }

    public TaxCategory getTaxcategory() {
        return this.taxcategory;
    }

    public ProductVariant taxcategory(TaxCategory taxCategory) {
        this.setTaxcategory(taxCategory);
        return this;
    }

    public void setTaxcategory(TaxCategory taxCategory) {
        this.taxcategory = taxCategory;
    }

    public Set<Channel> getChannels() {
        return this.channels;
    }

    public ProductVariant channels(Set<Channel> channels) {
        this.setChannels(channels);
        return this;
    }

    public ProductVariant addChannel(Channel channel) {
        this.channels.add(channel);
        channel.getProductVariants().add(this);
        return this;
    }

    public ProductVariant removeChannel(Channel channel) {
        this.channels.remove(channel);
        channel.getProductVariants().remove(this);
        return this;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public Set<Collection> getProductVariants() {
        return this.productVariants;
    }

    public ProductVariant productVariants(Set<Collection> collections) {
        this.setProductVariants(collections);
        return this;
    }

    public ProductVariant addProductVariants(Collection collection) {
        this.productVariants.add(collection);
        collection.getProductvariants().add(this);
        return this;
    }

    public ProductVariant removeProductVariants(Collection collection) {
        this.productVariants.remove(collection);
        collection.getProductvariants().remove(this);
        return this;
    }

    public void setProductVariants(Set<Collection> collections) {
        this.productVariants = collections;
    }

    public Set<FacetValue> getFacetValues() {
        return this.facetValues;
    }

    public ProductVariant facetValues(Set<FacetValue> facetValues) {
        this.setFacetValues(facetValues);
        return this;
    }

    public ProductVariant addFacetValue(FacetValue facetValue) {
        this.facetValues.add(facetValue);
        facetValue.getProductVariants().add(this);
        return this;
    }

    public ProductVariant removeFacetValue(FacetValue facetValue) {
        this.facetValues.remove(facetValue);
        facetValue.getProductVariants().remove(this);
        return this;
    }

    public void setFacetValues(Set<FacetValue> facetValues) {
        this.facetValues = facetValues;
    }

    public Set<ProductOption> getProductOptions() {
        return this.productOptions;
    }

    public ProductVariant productOptions(Set<ProductOption> productOptions) {
        this.setProductOptions(productOptions);
        return this;
    }

    public ProductVariant addProductOption(ProductOption productOption) {
        this.productOptions.add(productOption);
        productOption.getProductVariants().add(this);
        return this;
    }

    public ProductVariant removeProductOption(ProductOption productOption) {
        this.productOptions.remove(productOption);
        productOption.getProductVariants().remove(this);
        return this;
    }

    public void setProductOptions(Set<ProductOption> productOptions) {
        this.productOptions = productOptions;
    }

    public Set<ProductVariantAsset> getProductVariantAssets() {
        return this.productVariantAssets;
    }

    public ProductVariant productVariantAssets(Set<ProductVariantAsset> productVariantAssets) {
        this.setProductVariantAssets(productVariantAssets);
        return this;
    }

    public ProductVariant addProductVariantAsset(ProductVariantAsset productVariantAsset) {
        this.productVariantAssets.add(productVariantAsset);
        productVariantAsset.setProductvariant(this);
        return this;
    }

    public ProductVariant removeProductVariantAsset(ProductVariantAsset productVariantAsset) {
        this.productVariantAssets.remove(productVariantAsset);
        productVariantAsset.setProductvariant(null);
        return this;
    }

    public void setProductVariantAssets(Set<ProductVariantAsset> productVariantAssets) {
        if (this.productVariantAssets != null) {
            this.productVariantAssets.forEach(i -> i.setProductvariant(null));
        }
        if (productVariantAssets != null) {
            productVariantAssets.forEach(i -> i.setProductvariant(this));
        }
        this.productVariantAssets = productVariantAssets;
    }

    public Set<ProductVariantPrice> getProductVariantPrices() {
        return this.productVariantPrices;
    }

    public ProductVariant productVariantPrices(Set<ProductVariantPrice> productVariantPrices) {
        this.setProductVariantPrices(productVariantPrices);
        return this;
    }

    public ProductVariant addProductVariantPrice(ProductVariantPrice productVariantPrice) {
        this.productVariantPrices.add(productVariantPrice);
        productVariantPrice.setVariant(this);
        return this;
    }

    public ProductVariant removeProductVariantPrice(ProductVariantPrice productVariantPrice) {
        this.productVariantPrices.remove(productVariantPrice);
        productVariantPrice.setVariant(null);
        return this;
    }

    public void setProductVariantPrices(Set<ProductVariantPrice> productVariantPrices) {
        if (this.productVariantPrices != null) {
            this.productVariantPrices.forEach(i -> i.setVariant(null));
        }
        if (productVariantPrices != null) {
            productVariantPrices.forEach(i -> i.setVariant(this));
        }
        this.productVariantPrices = productVariantPrices;
    }

    public Set<ProductVariantTranslation> getProductVariantTranslations() {
        return this.productVariantTranslations;
    }

    public ProductVariant productVariantTranslations(Set<ProductVariantTranslation> productVariantTranslations) {
        this.setProductVariantTranslations(productVariantTranslations);
        return this;
    }

    public ProductVariant addProductVariantTranslation(ProductVariantTranslation productVariantTranslation) {
        this.productVariantTranslations.add(productVariantTranslation);
        productVariantTranslation.setBase(this);
        return this;
    }

    public ProductVariant removeProductVariantTranslation(ProductVariantTranslation productVariantTranslation) {
        this.productVariantTranslations.remove(productVariantTranslation);
        productVariantTranslation.setBase(null);
        return this;
    }

    public void setProductVariantTranslations(Set<ProductVariantTranslation> productVariantTranslations) {
        if (this.productVariantTranslations != null) {
            this.productVariantTranslations.forEach(i -> i.setBase(null));
        }
        if (productVariantTranslations != null) {
            productVariantTranslations.forEach(i -> i.setBase(this));
        }
        this.productVariantTranslations = productVariantTranslations;
    }

    public Set<StockMovement> getStockMovements() {
        return this.stockMovements;
    }

    public ProductVariant stockMovements(Set<StockMovement> stockMovements) {
        this.setStockMovements(stockMovements);
        return this;
    }

    public ProductVariant addStockMovement(StockMovement stockMovement) {
        this.stockMovements.add(stockMovement);
        stockMovement.setProductvariant(this);
        return this;
    }

    public ProductVariant removeStockMovement(StockMovement stockMovement) {
        this.stockMovements.remove(stockMovement);
        stockMovement.setProductvariant(null);
        return this;
    }

    public void setStockMovements(Set<StockMovement> stockMovements) {
        if (this.stockMovements != null) {
            this.stockMovements.forEach(i -> i.setProductvariant(null));
        }
        if (stockMovements != null) {
            stockMovements.forEach(i -> i.setProductvariant(this));
        }
        this.stockMovements = stockMovements;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVariant)) {
            return false;
        }
        return id != null && id.equals(((ProductVariant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariant{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", sku='" + getSku() + "'" +
            ", stockonhand=" + getStockonhand() +
            ", stockallocated=" + getStockallocated() +
            ", outofstockthreshold=" + getOutofstockthreshold() +
            ", useglobaloutofstockthreshold='" + getUseglobaloutofstockthreshold() + "'" +
            ", trackinventory='" + getTrackinventory() + "'" +
            "}";
    }
}
