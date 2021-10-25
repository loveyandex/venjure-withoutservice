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
 * Criteria class for the {@link com.venjure.domain.Channel} entity. This class is used
 * in {@link com.venjure.web.rest.ChannelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /channels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChannelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter code;

    private StringFilter token;

    private StringFilter defaultlanguagecode;

    private StringFilter currencycode;

    private BooleanFilter pricesincludetax;

    private LongFilter defaulttaxzoneId;

    private LongFilter defaultshippingzoneId;

    private LongFilter paymentMethodId;

    private LongFilter productId;

    private LongFilter promotionId;

    private LongFilter shippingMethodId;

    private LongFilter customerId;

    private LongFilter facetId;

    private LongFilter facetValueId;

    private LongFilter jorderId;

    private LongFilter productVariantId;

    public ChannelCriteria() {}

    public ChannelCriteria(ChannelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.token = other.token == null ? null : other.token.copy();
        this.defaultlanguagecode = other.defaultlanguagecode == null ? null : other.defaultlanguagecode.copy();
        this.currencycode = other.currencycode == null ? null : other.currencycode.copy();
        this.pricesincludetax = other.pricesincludetax == null ? null : other.pricesincludetax.copy();
        this.defaulttaxzoneId = other.defaulttaxzoneId == null ? null : other.defaulttaxzoneId.copy();
        this.defaultshippingzoneId = other.defaultshippingzoneId == null ? null : other.defaultshippingzoneId.copy();
        this.paymentMethodId = other.paymentMethodId == null ? null : other.paymentMethodId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.promotionId = other.promotionId == null ? null : other.promotionId.copy();
        this.shippingMethodId = other.shippingMethodId == null ? null : other.shippingMethodId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.facetId = other.facetId == null ? null : other.facetId.copy();
        this.facetValueId = other.facetValueId == null ? null : other.facetValueId.copy();
        this.jorderId = other.jorderId == null ? null : other.jorderId.copy();
        this.productVariantId = other.productVariantId == null ? null : other.productVariantId.copy();
    }

    @Override
    public ChannelCriteria copy() {
        return new ChannelCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getToken() {
        return token;
    }

    public StringFilter token() {
        if (token == null) {
            token = new StringFilter();
        }
        return token;
    }

    public void setToken(StringFilter token) {
        this.token = token;
    }

    public StringFilter getDefaultlanguagecode() {
        return defaultlanguagecode;
    }

    public StringFilter defaultlanguagecode() {
        if (defaultlanguagecode == null) {
            defaultlanguagecode = new StringFilter();
        }
        return defaultlanguagecode;
    }

    public void setDefaultlanguagecode(StringFilter defaultlanguagecode) {
        this.defaultlanguagecode = defaultlanguagecode;
    }

    public StringFilter getCurrencycode() {
        return currencycode;
    }

    public StringFilter currencycode() {
        if (currencycode == null) {
            currencycode = new StringFilter();
        }
        return currencycode;
    }

    public void setCurrencycode(StringFilter currencycode) {
        this.currencycode = currencycode;
    }

    public BooleanFilter getPricesincludetax() {
        return pricesincludetax;
    }

    public BooleanFilter pricesincludetax() {
        if (pricesincludetax == null) {
            pricesincludetax = new BooleanFilter();
        }
        return pricesincludetax;
    }

    public void setPricesincludetax(BooleanFilter pricesincludetax) {
        this.pricesincludetax = pricesincludetax;
    }

    public LongFilter getDefaulttaxzoneId() {
        return defaulttaxzoneId;
    }

    public LongFilter defaulttaxzoneId() {
        if (defaulttaxzoneId == null) {
            defaulttaxzoneId = new LongFilter();
        }
        return defaulttaxzoneId;
    }

    public void setDefaulttaxzoneId(LongFilter defaulttaxzoneId) {
        this.defaulttaxzoneId = defaulttaxzoneId;
    }

    public LongFilter getDefaultshippingzoneId() {
        return defaultshippingzoneId;
    }

    public LongFilter defaultshippingzoneId() {
        if (defaultshippingzoneId == null) {
            defaultshippingzoneId = new LongFilter();
        }
        return defaultshippingzoneId;
    }

    public void setDefaultshippingzoneId(LongFilter defaultshippingzoneId) {
        this.defaultshippingzoneId = defaultshippingzoneId;
    }

    public LongFilter getPaymentMethodId() {
        return paymentMethodId;
    }

    public LongFilter paymentMethodId() {
        if (paymentMethodId == null) {
            paymentMethodId = new LongFilter();
        }
        return paymentMethodId;
    }

    public void setPaymentMethodId(LongFilter paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
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

    public LongFilter getPromotionId() {
        return promotionId;
    }

    public LongFilter promotionId() {
        if (promotionId == null) {
            promotionId = new LongFilter();
        }
        return promotionId;
    }

    public void setPromotionId(LongFilter promotionId) {
        this.promotionId = promotionId;
    }

    public LongFilter getShippingMethodId() {
        return shippingMethodId;
    }

    public LongFilter shippingMethodId() {
        if (shippingMethodId == null) {
            shippingMethodId = new LongFilter();
        }
        return shippingMethodId;
    }

    public void setShippingMethodId(LongFilter shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getFacetId() {
        return facetId;
    }

    public LongFilter facetId() {
        if (facetId == null) {
            facetId = new LongFilter();
        }
        return facetId;
    }

    public void setFacetId(LongFilter facetId) {
        this.facetId = facetId;
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

    public LongFilter getJorderId() {
        return jorderId;
    }

    public LongFilter jorderId() {
        if (jorderId == null) {
            jorderId = new LongFilter();
        }
        return jorderId;
    }

    public void setJorderId(LongFilter jorderId) {
        this.jorderId = jorderId;
    }

    public LongFilter getProductVariantId() {
        return productVariantId;
    }

    public LongFilter productVariantId() {
        if (productVariantId == null) {
            productVariantId = new LongFilter();
        }
        return productVariantId;
    }

    public void setProductVariantId(LongFilter productVariantId) {
        this.productVariantId = productVariantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChannelCriteria that = (ChannelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(code, that.code) &&
            Objects.equals(token, that.token) &&
            Objects.equals(defaultlanguagecode, that.defaultlanguagecode) &&
            Objects.equals(currencycode, that.currencycode) &&
            Objects.equals(pricesincludetax, that.pricesincludetax) &&
            Objects.equals(defaulttaxzoneId, that.defaulttaxzoneId) &&
            Objects.equals(defaultshippingzoneId, that.defaultshippingzoneId) &&
            Objects.equals(paymentMethodId, that.paymentMethodId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(promotionId, that.promotionId) &&
            Objects.equals(shippingMethodId, that.shippingMethodId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(facetId, that.facetId) &&
            Objects.equals(facetValueId, that.facetValueId) &&
            Objects.equals(jorderId, that.jorderId) &&
            Objects.equals(productVariantId, that.productVariantId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            code,
            token,
            defaultlanguagecode,
            currencycode,
            pricesincludetax,
            defaulttaxzoneId,
            defaultshippingzoneId,
            paymentMethodId,
            productId,
            promotionId,
            shippingMethodId,
            customerId,
            facetId,
            facetValueId,
            jorderId,
            productVariantId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChannelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (token != null ? "token=" + token + ", " : "") +
            (defaultlanguagecode != null ? "defaultlanguagecode=" + defaultlanguagecode + ", " : "") +
            (currencycode != null ? "currencycode=" + currencycode + ", " : "") +
            (pricesincludetax != null ? "pricesincludetax=" + pricesincludetax + ", " : "") +
            (defaulttaxzoneId != null ? "defaulttaxzoneId=" + defaulttaxzoneId + ", " : "") +
            (defaultshippingzoneId != null ? "defaultshippingzoneId=" + defaultshippingzoneId + ", " : "") +
            (paymentMethodId != null ? "paymentMethodId=" + paymentMethodId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (promotionId != null ? "promotionId=" + promotionId + ", " : "") +
            (shippingMethodId != null ? "shippingMethodId=" + shippingMethodId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (facetId != null ? "facetId=" + facetId + ", " : "") +
            (facetValueId != null ? "facetValueId=" + facetValueId + ", " : "") +
            (jorderId != null ? "jorderId=" + jorderId + ", " : "") +
            (productVariantId != null ? "productVariantId=" + productVariantId + ", " : "") +
            "}";
    }
}
