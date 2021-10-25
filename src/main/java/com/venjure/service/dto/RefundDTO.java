package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Refund} entity.
 */
public class RefundDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    private Integer items;

    @NotNull
    private Integer shipping;

    @NotNull
    private Integer adjustment;

    @NotNull
    private Integer total;

    @NotNull
    @Size(max = 255)
    private String method;

    @Size(max = 255)
    private String reason;

    @NotNull
    @Size(max = 255)
    private String state;

    @Size(max = 255)
    private String transactionid;

    @NotNull
    @Size(max = 255)
    private String metadata;

    private PaymentDTO payment;

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

    public Integer getItems() {
        return items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }

    public Integer getShipping() {
        return shipping;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public Integer getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Integer adjustment) {
        this.adjustment = adjustment;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefundDTO)) {
            return false;
        }

        RefundDTO refundDTO = (RefundDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, refundDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefundDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", items=" + getItems() +
            ", shipping=" + getShipping() +
            ", adjustment=" + getAdjustment() +
            ", total=" + getTotal() +
            ", method='" + getMethod() + "'" +
            ", reason='" + getReason() + "'" +
            ", state='" + getState() + "'" +
            ", transactionid='" + getTransactionid() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", payment=" + getPayment() +
            "}";
    }
}
