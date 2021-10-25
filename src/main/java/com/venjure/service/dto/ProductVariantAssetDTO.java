package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.ProductVariantAsset} entity.
 */
public class ProductVariantAssetDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    private Integer position;

    private AssetDTO asset;

    private ProductVariantDTO productvariant;

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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public AssetDTO getAsset() {
        return asset;
    }

    public void setAsset(AssetDTO asset) {
        this.asset = asset;
    }

    public ProductVariantDTO getProductvariant() {
        return productvariant;
    }

    public void setProductvariant(ProductVariantDTO productvariant) {
        this.productvariant = productvariant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVariantAssetDTO)) {
            return false;
        }

        ProductVariantAssetDTO productVariantAssetDTO = (ProductVariantAssetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productVariantAssetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantAssetDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", position=" + getPosition() +
            ", asset=" + getAsset() +
            ", productvariant=" + getProductvariant() +
            "}";
    }
}
