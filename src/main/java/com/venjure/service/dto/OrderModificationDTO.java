package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.OrderModification} entity.
 */
public class OrderModificationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String note;

    @NotNull
    private Integer pricechange;

    @Size(max = 255)
    private String shippingaddresschange;

    @Size(max = 255)
    private String billingaddresschange;

    private PaymentDTO payment;

    private RefundDTO refund;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getPricechange() {
        return pricechange;
    }

    public void setPricechange(Integer pricechange) {
        this.pricechange = pricechange;
    }

    public String getShippingaddresschange() {
        return shippingaddresschange;
    }

    public void setShippingaddresschange(String shippingaddresschange) {
        this.shippingaddresschange = shippingaddresschange;
    }

    public String getBillingaddresschange() {
        return billingaddresschange;
    }

    public void setBillingaddresschange(String billingaddresschange) {
        this.billingaddresschange = billingaddresschange;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public RefundDTO getRefund() {
        return refund;
    }

    public void setRefund(RefundDTO refund) {
        this.refund = refund;
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
        if (!(o instanceof OrderModificationDTO)) {
            return false;
        }

        OrderModificationDTO orderModificationDTO = (OrderModificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderModificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderModificationDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", note='" + getNote() + "'" +
            ", pricechange=" + getPricechange() +
            ", shippingaddresschange='" + getShippingaddresschange() + "'" +
            ", billingaddresschange='" + getBillingaddresschange() + "'" +
            ", payment=" + getPayment() +
            ", refund=" + getRefund() +
            ", jorder=" + getJorder() +
            "}";
    }
}
