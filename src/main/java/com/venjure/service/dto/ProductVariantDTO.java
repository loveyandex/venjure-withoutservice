package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.ProductVariant} entity.
 */
public class ProductVariantDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    private Instant deletedat;

    @NotNull
    private Boolean enabled;

    @NotNull
    @Size(max = 255)
    private String sku;

    @NotNull
    private Integer stockonhand;

    @NotNull
    private Integer stockallocated;

    @NotNull
    private Integer outofstockthreshold;

    @NotNull
    private Boolean useglobaloutofstockthreshold;

    @NotNull
    @Size(max = 255)
    private String trackinventory;

    private ProductDTO product;

    private AssetDTO featuredasset;

    private TaxCategoryDTO taxcategory;

    private Set<ChannelDTO> channels = new HashSet<>();

    private Set<CollectionDTO> productVariants = new HashSet<>();

    private Set<FacetValueDTO> facetValues = new HashSet<>();

    private Set<ProductOptionDTO> productOptions = new HashSet<>();

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

    public Instant getDeletedat() {
        return deletedat;
    }

    public void setDeletedat(Instant deletedat) {
        this.deletedat = deletedat;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getStockonhand() {
        return stockonhand;
    }

    public void setStockonhand(Integer stockonhand) {
        this.stockonhand = stockonhand;
    }

    public Integer getStockallocated() {
        return stockallocated;
    }

    public void setStockallocated(Integer stockallocated) {
        this.stockallocated = stockallocated;
    }

    public Integer getOutofstockthreshold() {
        return outofstockthreshold;
    }

    public void setOutofstockthreshold(Integer outofstockthreshold) {
        this.outofstockthreshold = outofstockthreshold;
    }

    public Boolean getUseglobaloutofstockthreshold() {
        return useglobaloutofstockthreshold;
    }

    public void setUseglobaloutofstockthreshold(Boolean useglobaloutofstockthreshold) {
        this.useglobaloutofstockthreshold = useglobaloutofstockthreshold;
    }

    public String getTrackinventory() {
        return trackinventory;
    }

    public void setTrackinventory(String trackinventory) {
        this.trackinventory = trackinventory;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public AssetDTO getFeaturedasset() {
        return featuredasset;
    }

    public void setFeaturedasset(AssetDTO featuredasset) {
        this.featuredasset = featuredasset;
    }

    public TaxCategoryDTO getTaxcategory() {
        return taxcategory;
    }

    public void setTaxcategory(TaxCategoryDTO taxcategory) {
        this.taxcategory = taxcategory;
    }

    public Set<ChannelDTO> getChannels() {
        return channels;
    }

    public void setChannels(Set<ChannelDTO> channels) {
        this.channels = channels;
    }

    public Set<CollectionDTO> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(Set<CollectionDTO> productVariants) {
        this.productVariants = productVariants;
    }

    public Set<FacetValueDTO> getFacetValues() {
        return facetValues;
    }

    public void setFacetValues(Set<FacetValueDTO> facetValues) {
        this.facetValues = facetValues;
    }

    public Set<ProductOptionDTO> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(Set<ProductOptionDTO> productOptions) {
        this.productOptions = productOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVariantDTO)) {
            return false;
        }

        ProductVariantDTO productVariantDTO = (ProductVariantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productVariantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantDTO{" +
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
            ", product=" + getProduct() +
            ", featuredasset=" + getFeaturedasset() +
            ", taxcategory=" + getTaxcategory() +
            ", channels=" + getChannels() +
            ", productVariants=" + getProductVariants() +
            ", facetValues=" + getFacetValues() +
            ", productOptions=" + getProductOptions() +
            "}";
    }
}
