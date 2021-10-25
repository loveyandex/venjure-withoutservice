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
 * Criteria class for the {@link com.venjure.domain.Jorder} entity. This class is used
 * in {@link com.venjure.web.rest.JorderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /jorders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JorderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter code;

    private StringFilter state;

    private BooleanFilter active;

    private InstantFilter orderplacedat;

    private StringFilter couponcodes;

    private StringFilter shippingaddress;

    private StringFilter billingaddress;

    private StringFilter currencycode;

    private IntegerFilter subtotal;

    private IntegerFilter subtotalwithtax;

    private IntegerFilter shipping;

    private IntegerFilter shippingwithtax;

    private IntegerFilter taxzoneid;

    private LongFilter customerId;

    private LongFilter channelId;

    private LongFilter promotionId;

    private LongFilter historyEntryId;

    private LongFilter orderLineId;

    private LongFilter orderModificationId;

    private LongFilter paymentId;

    private LongFilter shippingLineId;

    private LongFilter surchargeId;

    public JorderCriteria() {}

    public JorderCriteria(JorderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.orderplacedat = other.orderplacedat == null ? null : other.orderplacedat.copy();
        this.couponcodes = other.couponcodes == null ? null : other.couponcodes.copy();
        this.shippingaddress = other.shippingaddress == null ? null : other.shippingaddress.copy();
        this.billingaddress = other.billingaddress == null ? null : other.billingaddress.copy();
        this.currencycode = other.currencycode == null ? null : other.currencycode.copy();
        this.subtotal = other.subtotal == null ? null : other.subtotal.copy();
        this.subtotalwithtax = other.subtotalwithtax == null ? null : other.subtotalwithtax.copy();
        this.shipping = other.shipping == null ? null : other.shipping.copy();
        this.shippingwithtax = other.shippingwithtax == null ? null : other.shippingwithtax.copy();
        this.taxzoneid = other.taxzoneid == null ? null : other.taxzoneid.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.promotionId = other.promotionId == null ? null : other.promotionId.copy();
        this.historyEntryId = other.historyEntryId == null ? null : other.historyEntryId.copy();
        this.orderLineId = other.orderLineId == null ? null : other.orderLineId.copy();
        this.orderModificationId = other.orderModificationId == null ? null : other.orderModificationId.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.shippingLineId = other.shippingLineId == null ? null : other.shippingLineId.copy();
        this.surchargeId = other.surchargeId == null ? null : other.surchargeId.copy();
    }

    @Override
    public JorderCriteria copy() {
        return new JorderCriteria(this);
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

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public InstantFilter getOrderplacedat() {
        return orderplacedat;
    }

    public InstantFilter orderplacedat() {
        if (orderplacedat == null) {
            orderplacedat = new InstantFilter();
        }
        return orderplacedat;
    }

    public void setOrderplacedat(InstantFilter orderplacedat) {
        this.orderplacedat = orderplacedat;
    }

    public StringFilter getCouponcodes() {
        return couponcodes;
    }

    public StringFilter couponcodes() {
        if (couponcodes == null) {
            couponcodes = new StringFilter();
        }
        return couponcodes;
    }

    public void setCouponcodes(StringFilter couponcodes) {
        this.couponcodes = couponcodes;
    }

    public StringFilter getShippingaddress() {
        return shippingaddress;
    }

    public StringFilter shippingaddress() {
        if (shippingaddress == null) {
            shippingaddress = new StringFilter();
        }
        return shippingaddress;
    }

    public void setShippingaddress(StringFilter shippingaddress) {
        this.shippingaddress = shippingaddress;
    }

    public StringFilter getBillingaddress() {
        return billingaddress;
    }

    public StringFilter billingaddress() {
        if (billingaddress == null) {
            billingaddress = new StringFilter();
        }
        return billingaddress;
    }

    public void setBillingaddress(StringFilter billingaddress) {
        this.billingaddress = billingaddress;
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

    public IntegerFilter getSubtotal() {
        return subtotal;
    }

    public IntegerFilter subtotal() {
        if (subtotal == null) {
            subtotal = new IntegerFilter();
        }
        return subtotal;
    }

    public void setSubtotal(IntegerFilter subtotal) {
        this.subtotal = subtotal;
    }

    public IntegerFilter getSubtotalwithtax() {
        return subtotalwithtax;
    }

    public IntegerFilter subtotalwithtax() {
        if (subtotalwithtax == null) {
            subtotalwithtax = new IntegerFilter();
        }
        return subtotalwithtax;
    }

    public void setSubtotalwithtax(IntegerFilter subtotalwithtax) {
        this.subtotalwithtax = subtotalwithtax;
    }

    public IntegerFilter getShipping() {
        return shipping;
    }

    public IntegerFilter shipping() {
        if (shipping == null) {
            shipping = new IntegerFilter();
        }
        return shipping;
    }

    public void setShipping(IntegerFilter shipping) {
        this.shipping = shipping;
    }

    public IntegerFilter getShippingwithtax() {
        return shippingwithtax;
    }

    public IntegerFilter shippingwithtax() {
        if (shippingwithtax == null) {
            shippingwithtax = new IntegerFilter();
        }
        return shippingwithtax;
    }

    public void setShippingwithtax(IntegerFilter shippingwithtax) {
        this.shippingwithtax = shippingwithtax;
    }

    public IntegerFilter getTaxzoneid() {
        return taxzoneid;
    }

    public IntegerFilter taxzoneid() {
        if (taxzoneid == null) {
            taxzoneid = new IntegerFilter();
        }
        return taxzoneid;
    }

    public void setTaxzoneid(IntegerFilter taxzoneid) {
        this.taxzoneid = taxzoneid;
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

    public LongFilter getHistoryEntryId() {
        return historyEntryId;
    }

    public LongFilter historyEntryId() {
        if (historyEntryId == null) {
            historyEntryId = new LongFilter();
        }
        return historyEntryId;
    }

    public void setHistoryEntryId(LongFilter historyEntryId) {
        this.historyEntryId = historyEntryId;
    }

    public LongFilter getOrderLineId() {
        return orderLineId;
    }

    public LongFilter orderLineId() {
        if (orderLineId == null) {
            orderLineId = new LongFilter();
        }
        return orderLineId;
    }

    public void setOrderLineId(LongFilter orderLineId) {
        this.orderLineId = orderLineId;
    }

    public LongFilter getOrderModificationId() {
        return orderModificationId;
    }

    public LongFilter orderModificationId() {
        if (orderModificationId == null) {
            orderModificationId = new LongFilter();
        }
        return orderModificationId;
    }

    public void setOrderModificationId(LongFilter orderModificationId) {
        this.orderModificationId = orderModificationId;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getShippingLineId() {
        return shippingLineId;
    }

    public LongFilter shippingLineId() {
        if (shippingLineId == null) {
            shippingLineId = new LongFilter();
        }
        return shippingLineId;
    }

    public void setShippingLineId(LongFilter shippingLineId) {
        this.shippingLineId = shippingLineId;
    }

    public LongFilter getSurchargeId() {
        return surchargeId;
    }

    public LongFilter surchargeId() {
        if (surchargeId == null) {
            surchargeId = new LongFilter();
        }
        return surchargeId;
    }

    public void setSurchargeId(LongFilter surchargeId) {
        this.surchargeId = surchargeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JorderCriteria that = (JorderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(code, that.code) &&
            Objects.equals(state, that.state) &&
            Objects.equals(active, that.active) &&
            Objects.equals(orderplacedat, that.orderplacedat) &&
            Objects.equals(couponcodes, that.couponcodes) &&
            Objects.equals(shippingaddress, that.shippingaddress) &&
            Objects.equals(billingaddress, that.billingaddress) &&
            Objects.equals(currencycode, that.currencycode) &&
            Objects.equals(subtotal, that.subtotal) &&
            Objects.equals(subtotalwithtax, that.subtotalwithtax) &&
            Objects.equals(shipping, that.shipping) &&
            Objects.equals(shippingwithtax, that.shippingwithtax) &&
            Objects.equals(taxzoneid, that.taxzoneid) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(promotionId, that.promotionId) &&
            Objects.equals(historyEntryId, that.historyEntryId) &&
            Objects.equals(orderLineId, that.orderLineId) &&
            Objects.equals(orderModificationId, that.orderModificationId) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(shippingLineId, that.shippingLineId) &&
            Objects.equals(surchargeId, that.surchargeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            code,
            state,
            active,
            orderplacedat,
            couponcodes,
            shippingaddress,
            billingaddress,
            currencycode,
            subtotal,
            subtotalwithtax,
            shipping,
            shippingwithtax,
            taxzoneid,
            customerId,
            channelId,
            promotionId,
            historyEntryId,
            orderLineId,
            orderModificationId,
            paymentId,
            shippingLineId,
            surchargeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JorderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (orderplacedat != null ? "orderplacedat=" + orderplacedat + ", " : "") +
            (couponcodes != null ? "couponcodes=" + couponcodes + ", " : "") +
            (shippingaddress != null ? "shippingaddress=" + shippingaddress + ", " : "") +
            (billingaddress != null ? "billingaddress=" + billingaddress + ", " : "") +
            (currencycode != null ? "currencycode=" + currencycode + ", " : "") +
            (subtotal != null ? "subtotal=" + subtotal + ", " : "") +
            (subtotalwithtax != null ? "subtotalwithtax=" + subtotalwithtax + ", " : "") +
            (shipping != null ? "shipping=" + shipping + ", " : "") +
            (shippingwithtax != null ? "shippingwithtax=" + shippingwithtax + ", " : "") +
            (taxzoneid != null ? "taxzoneid=" + taxzoneid + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            (promotionId != null ? "promotionId=" + promotionId + ", " : "") +
            (historyEntryId != null ? "historyEntryId=" + historyEntryId + ", " : "") +
            (orderLineId != null ? "orderLineId=" + orderLineId + ", " : "") +
            (orderModificationId != null ? "orderModificationId=" + orderModificationId + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (shippingLineId != null ? "shippingLineId=" + shippingLineId + ", " : "") +
            (surchargeId != null ? "surchargeId=" + surchargeId + ", " : "") +
            "}";
    }
}
