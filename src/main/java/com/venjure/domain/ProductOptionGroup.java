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
 * A ProductOptionGroup.
 */
@Entity
@Table(name = "product_option_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductOptionGroup implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "featuredasset", "productVariants", "channels", "facetValues" }, allowSetters = true)
    private Product product;

    @OneToMany(mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "group", "productOptionTranslations", "productVariants" }, allowSetters = true)
    private Set<ProductOption> productOptions = new HashSet<>();

    @OneToMany(mappedBy = "base")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Set<Pogt> productOptionGroupTranslations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductOptionGroup id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public ProductOptionGroup createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public ProductOptionGroup updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Instant getDeletedat() {
        return this.deletedat;
    }

    public ProductOptionGroup deletedat(Instant deletedat) {
        this.deletedat = deletedat;
        return this;
    }

    public void setDeletedat(Instant deletedat) {
        this.deletedat = deletedat;
    }

    public String getCode() {
        return this.code;
    }

    public ProductOptionGroup code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Product getProduct() {
        return this.product;
    }

    public ProductOptionGroup product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<ProductOption> getProductOptions() {
        return this.productOptions;
    }

    public ProductOptionGroup productOptions(Set<ProductOption> productOptions) {
        this.setProductOptions(productOptions);
        return this;
    }

    public ProductOptionGroup addProductOption(ProductOption productOption) {
        this.productOptions.add(productOption);
        productOption.setGroup(this);
        return this;
    }

    public ProductOptionGroup removeProductOption(ProductOption productOption) {
        this.productOptions.remove(productOption);
        productOption.setGroup(null);
        return this;
    }

    public void setProductOptions(Set<ProductOption> productOptions) {
        if (this.productOptions != null) {
            this.productOptions.forEach(i -> i.setGroup(null));
        }
        if (productOptions != null) {
            productOptions.forEach(i -> i.setGroup(this));
        }
        this.productOptions = productOptions;
    }

    public Set<Pogt> getProductOptionGroupTranslations() {
        return this.productOptionGroupTranslations;
    }

    public ProductOptionGroup productOptionGroupTranslations(Set<Pogt> pogts) {
        this.setProductOptionGroupTranslations(pogts);
        return this;
    }

    public ProductOptionGroup addProductOptionGroupTranslation(Pogt pogt) {
        this.productOptionGroupTranslations.add(pogt);
        pogt.setBase(this);
        return this;
    }

    public ProductOptionGroup removeProductOptionGroupTranslation(Pogt pogt) {
        this.productOptionGroupTranslations.remove(pogt);
        pogt.setBase(null);
        return this;
    }

    public void setProductOptionGroupTranslations(Set<Pogt> pogts) {
        if (this.productOptionGroupTranslations != null) {
            this.productOptionGroupTranslations.forEach(i -> i.setBase(null));
        }
        if (pogts != null) {
            pogts.forEach(i -> i.setBase(this));
        }
        this.productOptionGroupTranslations = pogts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOptionGroup)) {
            return false;
        }
        return id != null && id.equals(((ProductOptionGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOptionGroup{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
