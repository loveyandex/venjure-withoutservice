package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductVariantAsset.
 */
@Entity
@Table(name = "product_variant_asset")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductVariantAsset implements Serializable {

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
    @Column(name = "position", nullable = false)
    private Integer position;

    @ManyToOne(optional = false)
    @NotNull
    private Asset asset;

    @ManyToOne(optional = false)
    @NotNull
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
    private ProductVariant productvariant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductVariantAsset id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public ProductVariantAsset createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public ProductVariantAsset updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Integer getPosition() {
        return this.position;
    }

    public ProductVariantAsset position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Asset getAsset() {
        return this.asset;
    }

    public ProductVariantAsset asset(Asset asset) {
        this.setAsset(asset);
        return this;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public ProductVariant getProductvariant() {
        return this.productvariant;
    }

    public ProductVariantAsset productvariant(ProductVariant productVariant) {
        this.setProductvariant(productVariant);
        return this;
    }

    public void setProductvariant(ProductVariant productVariant) {
        this.productvariant = productVariant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVariantAsset)) {
            return false;
        }
        return id != null && id.equals(((ProductVariantAsset) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantAsset{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", position=" + getPosition() +
            "}";
    }
}
