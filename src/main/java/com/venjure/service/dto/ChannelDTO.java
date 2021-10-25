package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Channel} entity.
 */
public class ChannelDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String code;

    @NotNull
    @Size(max = 255)
    private String token;

    @NotNull
    @Size(max = 255)
    private String defaultlanguagecode;

    @NotNull
    @Size(max = 255)
    private String currencycode;

    @NotNull
    private Boolean pricesincludetax;

    private ZoneDTO defaulttaxzone;

    private ZoneDTO defaultshippingzone;

    private Set<PaymentMethodDTO> paymentMethods = new HashSet<>();

    private Set<ProductDTO> products = new HashSet<>();

    private Set<PromotionDTO> promotions = new HashSet<>();

    private Set<ShippingMethodDTO> shippingMethods = new HashSet<>();

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDefaultlanguagecode() {
        return defaultlanguagecode;
    }

    public void setDefaultlanguagecode(String defaultlanguagecode) {
        this.defaultlanguagecode = defaultlanguagecode;
    }

    public String getCurrencycode() {
        return currencycode;
    }

    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode;
    }

    public Boolean getPricesincludetax() {
        return pricesincludetax;
    }

    public void setPricesincludetax(Boolean pricesincludetax) {
        this.pricesincludetax = pricesincludetax;
    }

    public ZoneDTO getDefaulttaxzone() {
        return defaulttaxzone;
    }

    public void setDefaulttaxzone(ZoneDTO defaulttaxzone) {
        this.defaulttaxzone = defaulttaxzone;
    }

    public ZoneDTO getDefaultshippingzone() {
        return defaultshippingzone;
    }

    public void setDefaultshippingzone(ZoneDTO defaultshippingzone) {
        this.defaultshippingzone = defaultshippingzone;
    }

    public Set<PaymentMethodDTO> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(Set<PaymentMethodDTO> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    public Set<PromotionDTO> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<PromotionDTO> promotions) {
        this.promotions = promotions;
    }

    public Set<ShippingMethodDTO> getShippingMethods() {
        return shippingMethods;
    }

    public void setShippingMethods(Set<ShippingMethodDTO> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChannelDTO)) {
            return false;
        }

        ChannelDTO channelDTO = (ChannelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, channelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChannelDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", code='" + getCode() + "'" +
            ", token='" + getToken() + "'" +
            ", defaultlanguagecode='" + getDefaultlanguagecode() + "'" +
            ", currencycode='" + getCurrencycode() + "'" +
            ", pricesincludetax='" + getPricesincludetax() + "'" +
            ", defaulttaxzone=" + getDefaulttaxzone() +
            ", defaultshippingzone=" + getDefaultshippingzone() +
            ", paymentMethods=" + getPaymentMethods() +
            ", products=" + getProducts() +
            ", promotions=" + getPromotions() +
            ", shippingMethods=" + getShippingMethods() +
            "}";
    }
}
