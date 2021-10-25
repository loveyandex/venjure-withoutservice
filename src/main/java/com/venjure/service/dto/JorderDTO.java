package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Jorder} entity.
 */
public class JorderDTO implements Serializable {

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
    private String state;

    @NotNull
    private Boolean active;

    private Instant orderplacedat;

    @NotNull
    @Size(max = 255)
    private String couponcodes;

    @NotNull
    @Size(max = 255)
    private String shippingaddress;

    @NotNull
    @Size(max = 255)
    private String billingaddress;

    @NotNull
    @Size(max = 255)
    private String currencycode;

    @NotNull
    private Integer subtotal;

    @NotNull
    private Integer subtotalwithtax;

    @NotNull
    private Integer shipping;

    @NotNull
    private Integer shippingwithtax;

    private Integer taxzoneid;

    private CustomerDTO customer;

    private Set<ChannelDTO> channels = new HashSet<>();

    private Set<PromotionDTO> promotions = new HashSet<>();

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getOrderplacedat() {
        return orderplacedat;
    }

    public void setOrderplacedat(Instant orderplacedat) {
        this.orderplacedat = orderplacedat;
    }

    public String getCouponcodes() {
        return couponcodes;
    }

    public void setCouponcodes(String couponcodes) {
        this.couponcodes = couponcodes;
    }

    public String getShippingaddress() {
        return shippingaddress;
    }

    public void setShippingaddress(String shippingaddress) {
        this.shippingaddress = shippingaddress;
    }

    public String getBillingaddress() {
        return billingaddress;
    }

    public void setBillingaddress(String billingaddress) {
        this.billingaddress = billingaddress;
    }

    public String getCurrencycode() {
        return currencycode;
    }

    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getSubtotalwithtax() {
        return subtotalwithtax;
    }

    public void setSubtotalwithtax(Integer subtotalwithtax) {
        this.subtotalwithtax = subtotalwithtax;
    }

    public Integer getShipping() {
        return shipping;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public Integer getShippingwithtax() {
        return shippingwithtax;
    }

    public void setShippingwithtax(Integer shippingwithtax) {
        this.shippingwithtax = shippingwithtax;
    }

    public Integer getTaxzoneid() {
        return taxzoneid;
    }

    public void setTaxzoneid(Integer taxzoneid) {
        this.taxzoneid = taxzoneid;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public Set<ChannelDTO> getChannels() {
        return channels;
    }

    public void setChannels(Set<ChannelDTO> channels) {
        this.channels = channels;
    }

    public Set<PromotionDTO> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<PromotionDTO> promotions) {
        this.promotions = promotions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JorderDTO)) {
            return false;
        }

        JorderDTO jorderDTO = (JorderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jorderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JorderDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", code='" + getCode() + "'" +
            ", state='" + getState() + "'" +
            ", active='" + getActive() + "'" +
            ", orderplacedat='" + getOrderplacedat() + "'" +
            ", couponcodes='" + getCouponcodes() + "'" +
            ", shippingaddress='" + getShippingaddress() + "'" +
            ", billingaddress='" + getBillingaddress() + "'" +
            ", currencycode='" + getCurrencycode() + "'" +
            ", subtotal=" + getSubtotal() +
            ", subtotalwithtax=" + getSubtotalwithtax() +
            ", shipping=" + getShipping() +
            ", shippingwithtax=" + getShippingwithtax() +
            ", taxzoneid=" + getTaxzoneid() +
            ", customer=" + getCustomer() +
            ", channels=" + getChannels() +
            ", promotions=" + getPromotions() +
            "}";
    }
}
