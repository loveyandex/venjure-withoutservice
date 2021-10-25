package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CollectionAsset.
 */
@Entity
@Table(name = "collection_asset")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CollectionAsset implements Serializable {

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
    @JsonIgnoreProperties(value = { "featuredasset", "parent", "collectionTranslations", "productvariants" }, allowSetters = true)
    private Collection collection;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CollectionAsset id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public CollectionAsset createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public CollectionAsset updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Integer getPosition() {
        return this.position;
    }

    public CollectionAsset position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Asset getAsset() {
        return this.asset;
    }

    public CollectionAsset asset(Asset asset) {
        this.setAsset(asset);
        return this;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Collection getCollection() {
        return this.collection;
    }

    public CollectionAsset collection(Collection collection) {
        this.setCollection(collection);
        return this;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectionAsset)) {
            return false;
        }
        return id != null && id.equals(((CollectionAsset) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollectionAsset{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", position=" + getPosition() +
            "}";
    }
}
