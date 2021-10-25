package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.OrderLine} entity.
 */
public class OrderLineDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    private ProductVariantDTO productvariant;

    private TaxCategoryDTO taxcategory;

    private AssetDTO featuredAsset;

    private JorderDTO jorder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public ProductVariantDTO getProductvariant() {
        return productvariant;
    }

    public void setProductvariant(ProductVariantDTO productvariant) {
        this.productvariant = productvariant;
    }

    public TaxCategoryDTO getTaxcategory() {
        return taxcategory;
    }

    public void setTaxcategory(TaxCategoryDTO taxcategory) {
        this.taxcategory = taxcategory;
    }

    public AssetDTO getFeaturedAsset() {
        return featuredAsset;
    }

    public void setFeaturedAsset(AssetDTO featuredAsset) {
        this.featuredAsset = featuredAsset;
    }

    public JorderDTO getJorder() {
        return jorder;
    }

    public void setJorder(JorderDTO jorder) {
        this.jorder = jorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderLineDTO)) {
            return false;
        }

        OrderLineDTO orderLineDTO = (OrderLineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderLineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderLineDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", productvariant=" + getProductvariant() +
            ", taxcategory=" + getTaxcategory() +
            ", featuredAsset=" + getFeaturedAsset() +
            ", jorder=" + getJorder() +
            "}";
    }
}
