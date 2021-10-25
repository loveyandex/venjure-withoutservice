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
 * A Collection.
 */
@Entity
@Table(name = "collection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Collection implements Serializable {

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
    @Column(name = "isroot", nullable = false)
    private Boolean isroot;

    @NotNull
    @Column(name = "position", nullable = false)
    private Integer position;

    @NotNull
    @Column(name = "isprivate", nullable = false)
    private Boolean isprivate;

    @NotNull
    @Size(max = 255)
    @Column(name = "filters", length = 255, nullable = false)
    private String filters;

    @ManyToOne
    private Asset featuredasset;

    @ManyToOne
    @JsonIgnoreProperties(value = { "featuredasset", "parent", "collectionTranslations", "productvariants" }, allowSetters = true)
    private Collection parent;

    @OneToMany(mappedBy = "base")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Set<CollectionTranslation> collectionTranslations = new HashSet<>();

    @ManyToMany(mappedBy = "productVariants")
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
    private Set<ProductVariant> productvariants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Collection createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Collection updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Boolean getIsroot() {
        return this.isroot;
    }

    public Collection isroot(Boolean isroot) {
        this.isroot = isroot;
        return this;
    }

    public void setIsroot(Boolean isroot) {
        this.isroot = isroot;
    }

    public Integer getPosition() {
        return this.position;
    }

    public Collection position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean getIsprivate() {
        return this.isprivate;
    }

    public Collection isprivate(Boolean isprivate) {
        this.isprivate = isprivate;
        return this;
    }

    public void setIsprivate(Boolean isprivate) {
        this.isprivate = isprivate;
    }

    public String getFilters() {
        return this.filters;
    }

    public Collection filters(String filters) {
        this.filters = filters;
        return this;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Asset getFeaturedasset() {
        return this.featuredasset;
    }

    public Collection featuredasset(Asset asset) {
        this.setFeaturedasset(asset);
        return this;
    }

    public void setFeaturedasset(Asset asset) {
        this.featuredasset = asset;
    }

    public Collection getParent() {
        return this.parent;
    }

    public Collection parent(Collection collection) {
        this.setParent(collection);
        return this;
    }

    public void setParent(Collection collection) {
        this.parent = collection;
    }

    public Set<CollectionTranslation> getCollectionTranslations() {
        return this.collectionTranslations;
    }

    public Collection collectionTranslations(Set<CollectionTranslation> collectionTranslations) {
        this.setCollectionTranslations(collectionTranslations);
        return this;
    }

    public Collection addCollectionTranslation(CollectionTranslation collectionTranslation) {
        this.collectionTranslations.add(collectionTranslation);
        collectionTranslation.setBase(this);
        return this;
    }

    public Collection removeCollectionTranslation(CollectionTranslation collectionTranslation) {
        this.collectionTranslations.remove(collectionTranslation);
        collectionTranslation.setBase(null);
        return this;
    }

    public void setCollectionTranslations(Set<CollectionTranslation> collectionTranslations) {
        if (this.collectionTranslations != null) {
            this.collectionTranslations.forEach(i -> i.setBase(null));
        }
        if (collectionTranslations != null) {
            collectionTranslations.forEach(i -> i.setBase(this));
        }
        this.collectionTranslations = collectionTranslations;
    }

    public Set<ProductVariant> getProductvariants() {
        return this.productvariants;
    }

    public Collection productvariants(Set<ProductVariant> productVariants) {
        this.setProductvariants(productVariants);
        return this;
    }

    public Collection addProductvariant(ProductVariant productVariant) {
        this.productvariants.add(productVariant);
        productVariant.getProductVariants().add(this);
        return this;
    }

    public Collection removeProductvariant(ProductVariant productVariant) {
        this.productvariants.remove(productVariant);
        productVariant.getProductVariants().remove(this);
        return this;
    }

    public void setProductvariants(Set<ProductVariant> productVariants) {
        if (this.productvariants != null) {
            this.productvariants.forEach(i -> i.removeProductVariants(this));
        }
        if (productVariants != null) {
            productVariants.forEach(i -> i.addProductVariants(this));
        }
        this.productvariants = productVariants;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Collection)) {
            return false;
        }
        return id != null && id.equals(((Collection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Collection{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", isroot='" + getIsroot() + "'" +
            ", position=" + getPosition() +
            ", isprivate='" + getIsprivate() + "'" +
            ", filters='" + getFilters() + "'" +
            "}";
    }
}
