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
 * A ProductOption.
 */
@Entity
@Table(name = "product_option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductOption implements Serializable {

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
    @Size(max = 255)
    @Column(name = "code", length = 255, nullable = false)
    private String code;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "product", "productOptions", "productOptionGroupTranslations" }, allowSetters = true)
    private ProductOptionGroup group;

    @OneToMany(mappedBy = "base")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Set<ProductOptionTranslation> productOptionTranslations = new HashSet<>();

    @ManyToMany(mappedBy = "productOptions")
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

    public ProductOption id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public ProductOption createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public ProductOption updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Instant getDeletedat() {
        return this.deletedat;
    }

    public ProductOption deletedat(Instant deletedat) {
        this.deletedat = deletedat;
        return this;
    }

    public void setDeletedat(Instant deletedat) {
        this.deletedat = deletedat;
    }

    public String getCode() {
        return this.code;
    }

    public ProductOption code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ProductOptionGroup getGroup() {
        return this.group;
    }

    public ProductOption group(ProductOptionGroup productOptionGroup) {
        this.setGroup(productOptionGroup);
        return this;
    }

    public void setGroup(ProductOptionGroup productOptionGroup) {
        this.group = productOptionGroup;
    }

    public Set<ProductOptionTranslation> getProductOptionTranslations() {
        return this.productOptionTranslations;
    }

    public ProductOption productOptionTranslations(Set<ProductOptionTranslation> productOptionTranslations) {
        this.setProductOptionTranslations(productOptionTranslations);
        return this;
    }

    public ProductOption addProductOptionTranslation(ProductOptionTranslation productOptionTranslation) {
        this.productOptionTranslations.add(productOptionTranslation);
        productOptionTranslation.setBase(this);
        return this;
    }

    public ProductOption removeProductOptionTranslation(ProductOptionTranslation productOptionTranslation) {
        this.productOptionTranslations.remove(productOptionTranslation);
        productOptionTranslation.setBase(null);
        return this;
    }

    public void setProductOptionTranslations(Set<ProductOptionTranslation> productOptionTranslations) {
        if (this.productOptionTranslations != null) {
            this.productOptionTranslations.forEach(i -> i.setBase(null));
        }
        if (productOptionTranslations != null) {
            productOptionTranslations.forEach(i -> i.setBase(this));
        }
        this.productOptionTranslations = productOptionTranslations;
    }

    public Set<ProductVariant> getProductVariants() {
        return this.productVariants;
    }

    public ProductOption productVariants(Set<ProductVariant> productVariants) {
        this.setProductVariants(productVariants);
        return this;
    }

    public ProductOption addProductVariant(ProductVariant productVariant) {
        this.productVariants.add(productVariant);
        productVariant.getProductOptions().add(this);
        return this;
    }

    public ProductOption removeProductVariant(ProductVariant productVariant) {
        this.productVariants.remove(productVariant);
        productVariant.getProductOptions().remove(this);
        return this;
    }

    public void setProductVariants(Set<ProductVariant> productVariants) {
        if (this.productVariants != null) {
            this.productVariants.forEach(i -> i.removeProductOption(this));
        }
        if (productVariants != null) {
            productVariants.forEach(i -> i.addProductOption(this));
        }
        this.productVariants = productVariants;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOption)) {
            return false;
        }
        return id != null && id.equals(((ProductOption) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOption{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
