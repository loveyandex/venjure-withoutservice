package com.venjure.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.venjure.domain.ProductVariant} entity. This class is used
 * in {@link com.venjure.web.rest.ProductVariantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-variants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductVariantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private InstantFilter deletedat;

    private BooleanFilter enabled;

    private StringFilter sku;

    private IntegerFilter stockonhand;

    private IntegerFilter stockallocated;

    private IntegerFilter outofstockthreshold;

    private BooleanFilter useglobaloutofstockthreshold;

    private StringFilter trackinventory;

    private LongFilter productId;

    private LongFilter featuredassetId;

    private LongFilter taxcategoryId;

    private LongFilter channelId;

    private LongFilter productVariantsId;

    private LongFilter facetValueId;

    private LongFilter productOptionId;

    private LongFilter productVariantAssetId;

    private LongFilter productVariantPriceId;

    private LongFilter productVariantTranslationId;

    private LongFilter stockMovementId;

    public ProductVariantCriteria() {}

    public ProductVariantCriteria(ProductVariantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.deletedat = other.deletedat == null ? null : other.deletedat.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.stockonhand = other.stockonhand == null ? null : other.stockonhand.copy();
        this.stockallocated = other.stockallocated == null ? null : other.stockallocated.copy();
        this.outofstockthreshold = other.outofstockthreshold == null ? null : other.outofstockthreshold.copy();
        this.useglobaloutofstockthreshold = other.useglobaloutofstockthreshold == null ? null : other.useglobaloutofstockthreshold.copy();
        this.trackinventory = other.trackinventory == null ? null : other.trackinventory.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.featuredassetId = other.featuredassetId == null ? null : other.featuredassetId.copy();
        this.taxcategoryId = other.taxcategoryId == null ? null : other.taxcategoryId.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.productVariantsId = other.productVariantsId == null ? null : other.productVariantsId.copy();
        this.facetValueId = other.facetValueId == null ? null : other.facetValueId.copy();
        this.productOptionId = other.productOptionId == null ? null : other.productOptionId.copy();
        this.productVariantAssetId = other.productVariantAssetId == null ? null : other.productVariantAssetId.copy();
        this.productVariantPriceId = other.productVariantPriceId == null ? null : other.productVariantPriceId.copy();
        this.productVariantTranslationId = other.productVariantTranslationId == null ? null : other.productVariantTranslationId.copy();
        this.stockMovementId = other.stockMovementId == null ? null : other.stockMovementId.copy();
    }

    @Override
    public ProductVariantCriteria copy() {
        return new ProductVariantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreatedat() {
        return createdat;
    }

    public InstantFilter createdat() {
        if (createdat == null) {
            createdat = new InstantFilter();
        }
        return createdat;
    }

    public void setCreatedat(InstantFilter createdat) {
        this.createdat = createdat;
    }

    public InstantFilter getUpdatedat() {
        return updatedat;
    }

    public InstantFilter updatedat() {
        if (updatedat == null) {
            updatedat = new InstantFilter();
        }
        return updatedat;
    }

    public void setUpdatedat(InstantFilter updatedat) {
        this.updatedat = updatedat;
    }

    public InstantFilter getDeletedat() {
        return deletedat;
    }

    public InstantFilter deletedat() {
        if (deletedat == null) {
            deletedat = new InstantFilter();
        }
        return deletedat;
    }

    public void setDeletedat(InstantFilter deletedat) {
        this.deletedat = deletedat;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public BooleanFilter enabled() {
        if (enabled == null) {
            enabled = new BooleanFilter();
        }
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public StringFilter getSku() {
        return sku;
    }

    public StringFilter sku() {
        if (sku == null) {
            sku = new StringFilter();
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public IntegerFilter getStockonhand() {
        return stockonhand;
    }

    public IntegerFilter stockonhand() {
        if (stockonhand == null) {
            stockonhand = new IntegerFilter();
        }
        return stockonhand;
    }

    public void setStockonhand(IntegerFilter stockonhand) {
        this.stockonhand = stockonhand;
    }

    public IntegerFilter getStockallocated() {
        return stockallocated;
    }

    public IntegerFilter stockallocated() {
        if (stockallocated == null) {
            stockallocated = new IntegerFilter();
        }
        return stockallocated;
    }

    public void setStockallocated(IntegerFilter stockallocated) {
        this.stockallocated = stockallocated;
    }

    public IntegerFilter getOutofstockthreshold() {
        return outofstockthreshold;
    }

    public IntegerFilter outofstockthreshold() {
        if (outofstockthreshold == null) {
            outofstockthreshold = new IntegerFilter();
        }
        return outofstockthreshold;
    }

    public void setOutofstockthreshold(IntegerFilter outofstockthreshold) {
        this.outofstockthreshold = outofstockthreshold;
    }

    public BooleanFilter getUseglobaloutofstockthreshold() {
        return useglobaloutofstockthreshold;
    }

    public BooleanFilter useglobaloutofstockthreshold() {
        if (useglobaloutofstockthreshold == null) {
            useglobaloutofstockthreshold = new BooleanFilter();
        }
        return useglobaloutofstockthreshold;
    }

    public void setUseglobaloutofstockthreshold(BooleanFilter useglobaloutofstockthreshold) {
        this.useglobaloutofstockthreshold = useglobaloutofstockthreshold;
    }

    public StringFilter getTrackinventory() {
        return trackinventory;
    }

    public StringFilter trackinventory() {
        if (trackinventory == null) {
            trackinventory = new StringFilter();
        }
        return trackinventory;
    }

    public void setTrackinventory(StringFilter trackinventory) {
        this.trackinventory = trackinventory;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getFeaturedassetId() {
        return featuredassetId;
    }

    public LongFilter featuredassetId() {
        if (featuredassetId == null) {
            featuredassetId = new LongFilter();
        }
        return featuredassetId;
    }

    public void setFeaturedassetId(LongFilter featuredassetId) {
        this.featuredassetId = featuredassetId;
    }

    public LongFilter getTaxcategoryId() {
        return taxcategoryId;
    }

    public LongFilter taxcategoryId() {
        if (taxcategoryId == null) {
            taxcategoryId = new LongFilter();
        }
        return taxcategoryId;
    }

    public void setTaxcategoryId(LongFilter taxcategoryId) {
        this.taxcategoryId = taxcategoryId;
    }

    public LongFilter getChannelId() {
        return channelId;
    }

    public LongFilter channelId() {
        if (channelId == null) {
            channelId = new LongFilter();
        }
        return channelId;
    }

    public void setChannelId(LongFilter channelId) {
        this.channelId = channelId;
    }

    public LongFilter getProductVariantsId() {
        return productVariantsId;
    }

    public LongFilter productVariantsId() {
        if (productVariantsId == null) {
            productVariantsId = new LongFilter();
        }
        return productVariantsId;
    }

    public void setProductVariantsId(LongFilter productVariantsId) {
        this.productVariantsId = productVariantsId;
    }

    public LongFilter getFacetValueId() {
        return facetValueId;
    }

    public LongFilter facetValueId() {
        if (facetValueId == null) {
            facetValueId = new LongFilter();
        }
        return facetValueId;
    }

    public void setFacetValueId(LongFilter facetValueId) {
        this.facetValueId = facetValueId;
    }

    public LongFilter getProductOptionId() {
        return productOptionId;
    }

    public LongFilter productOptionId() {
        if (productOptionId == null) {
            productOptionId = new LongFilter();
        }
        return productOptionId;
    }

    public void setProductOptionId(LongFilter productOptionId) {
        this.productOptionId = productOptionId;
    }

    public LongFilter getProductVariantAssetId() {
        return productVariantAssetId;
    }

    public LongFilter productVariantAssetId() {
        if (productVariantAssetId == null) {
            productVariantAssetId = new LongFilter();
        }
        return productVariantAssetId;
    }

    public void setProductVariantAssetId(LongFilter productVariantAssetId) {
        this.productVariantAssetId = productVariantAssetId;
    }

    public LongFilter getProductVariantPriceId() {
        return productVariantPriceId;
    }

    public LongFilter productVariantPriceId() {
        if (productVariantPriceId == null) {
            productVariantPriceId = new LongFilter();
        }
        return productVariantPriceId;
    }

    public void setProductVariantPriceId(LongFilter productVariantPriceId) {
        this.productVariantPriceId = productVariantPriceId;
    }

    public LongFilter getProductVariantTranslationId() {
        return productVariantTranslationId;
    }

    public LongFilter productVariantTranslationId() {
        if (productVariantTranslationId == null) {
            productVariantTranslationId = new LongFilter();
        }
        return productVariantTranslationId;
    }

    public void setProductVariantTranslationId(LongFilter productVariantTranslationId) {
        this.productVariantTranslationId = productVariantTranslationId;
    }

    public LongFilter getStockMovementId() {
        return stockMovementId;
    }

    public LongFilter stockMovementId() {
        if (stockMovementId == null) {
            stockMovementId = new LongFilter();
        }
        return stockMovementId;
    }

    public void setStockMovementId(LongFilter stockMovementId) {
        this.stockMovementId = stockMovementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductVariantCriteria that = (ProductVariantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(deletedat, that.deletedat) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(stockonhand, that.stockonhand) &&
            Objects.equals(stockallocated, that.stockallocated) &&
            Objects.equals(outofstockthreshold, that.outofstockthreshold) &&
            Objects.equals(useglobaloutofstockthreshold, that.useglobaloutofstockthreshold) &&
            Objects.equals(trackinventory, that.trackinventory) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(featuredassetId, that.featuredassetId) &&
            Objects.equals(taxcategoryId, that.taxcategoryId) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(productVariantsId, that.productVariantsId) &&
            Objects.equals(facetValueId, that.facetValueId) &&
            Objects.equals(productOptionId, that.productOptionId) &&
            Objects.equals(productVariantAssetId, that.productVariantAssetId) &&
            Objects.equals(productVariantPriceId, that.productVariantPriceId) &&
            Objects.equals(productVariantTranslationId, that.productVariantTranslationId) &&
            Objects.equals(stockMovementId, that.stockMovementId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            deletedat,
            enabled,
            sku,
            stockonhand,
            stockallocated,
            outofstockthreshold,
            useglobaloutofstockthreshold,
            trackinventory,
            productId,
            featuredassetId,
            taxcategoryId,
            channelId,
            productVariantsId,
            facetValueId,
            productOptionId,
            productVariantAssetId,
            productVariantPriceId,
            productVariantTranslationId,
            stockMovementId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (deletedat != null ? "deletedat=" + deletedat + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (sku != null ? "sku=" + sku + ", " : "") +
            (stockonhand != null ? "stockonhand=" + stockonhand + ", " : "") +
            (stockallocated != null ? "stockallocated=" + stockallocated + ", " : "") +
            (outofstockthreshold != null ? "outofstockthreshold=" + outofstockthreshold + ", " : "") +
            (useglobaloutofstockthreshold != null ? "useglobaloutofstockthreshold=" + useglobaloutofstockthreshold + ", " : "") +
            (trackinventory != null ? "trackinventory=" + trackinventory + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (featuredassetId != null ? "featuredassetId=" + featuredassetId + ", " : "") +
            (taxcategoryId != null ? "taxcategoryId=" + taxcategoryId + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            (productVariantsId != null ? "productVariantsId=" + productVariantsId + ", " : "") +
            (facetValueId != null ? "facetValueId=" + facetValueId + ", " : "") +
            (productOptionId != null ? "productOptionId=" + productOptionId + ", " : "") +
            (productVariantAssetId != null ? "productVariantAssetId=" + productVariantAssetId + ", " : "") +
            (productVariantPriceId != null ? "productVariantPriceId=" + productVariantPriceId + ", " : "") +
            (productVariantTranslationId != null ? "productVariantTranslationId=" + productVariantTranslationId + ", " : "") +
            (stockMovementId != null ? "stockMovementId=" + stockMovementId + ", " : "") +
            "}";
    }
}
