package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductOptionTranslation.
 */
@Entity
@Table(name = "product_option_translation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductOptionTranslation implements Serializable {

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
    @Column(name = "languagecode", length = 255, nullable = false)
    private String languagecode;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = { "group", "productOptionTranslations", "productVariants" }, allowSetters = true)
    private ProductOption base;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductOptionTranslation id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public ProductOptionTranslation createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public ProductOptionTranslation updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getLanguagecode() {
        return this.languagecode;
    }

    public ProductOptionTranslation languagecode(String languagecode) {
        this.languagecode = languagecode;
        return this;
    }

    public void setLanguagecode(String languagecode) {
        this.languagecode = languagecode;
    }

    public String getName() {
        return this.name;
    }

    public ProductOptionTranslation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductOption getBase() {
        return this.base;
    }

    public ProductOptionTranslation base(ProductOption productOption) {
        this.setBase(productOption);
        return this;
    }

    public void setBase(ProductOption productOption) {
        this.base = productOption;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOptionTranslation)) {
            return false;
        }
        return id != null && id.equals(((ProductOptionTranslation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOptionTranslation{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", languagecode='" + getLanguagecode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
