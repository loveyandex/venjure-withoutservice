package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.OrderItem} entity.
 */
public class OrderItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    private Integer initiallistprice;

    @NotNull
    private Integer listprice;

    @NotNull
    private Boolean listpriceincludestax;

    @NotNull
    @Size(max = 255)
    private String adjustments;

    @NotNull
    @Size(max = 255)
    private String taxlines;

    @NotNull
    private Boolean cancelled;

    private OrderLineDTO line;

    private RefundDTO refund;

    private Set<FulfillmentDTO> fulfillments = new HashSet<>();

    private Set<OrderModificationDTO> orderModifications = new HashSet<>();

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

    public Integer getInitiallistprice() {
        return initiallistprice;
    }

    public void setInitiallistprice(Integer initiallistprice) {
        this.initiallistprice = initiallistprice;
    }

    public Integer getListprice() {
        return listprice;
    }

    public void setListprice(Integer listprice) {
        this.listprice = listprice;
    }

    public Boolean getListpriceincludestax() {
        return listpriceincludestax;
    }

    public void setListpriceincludestax(Boolean listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
    }

    public String getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(String adjustments) {
        this.adjustments = adjustments;
    }

    public String getTaxlines() {
        return taxlines;
    }

    public void setTaxlines(String taxlines) {
        this.taxlines = taxlines;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public OrderLineDTO getLine() {
        return line;
    }

    public void setLine(OrderLineDTO line) {
        this.line = line;
    }

    public RefundDTO getRefund() {
        return refund;
    }

    public void setRefund(RefundDTO refund) {
        this.refund = refund;
    }

    public Set<FulfillmentDTO> getFulfillments() {
        return fulfillments;
    }

    public void setFulfillments(Set<FulfillmentDTO> fulfillments) {
        this.fulfillments = fulfillments;
    }

    public Set<OrderModificationDTO> getOrderModifications() {
        return orderModifications;
    }

    public void setOrderModifications(Set<OrderModificationDTO> orderModifications) {
        this.orderModifications = orderModifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemDTO)) {
            return false;
        }

        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", initiallistprice=" + getInitiallistprice() +
            ", listprice=" + getListprice() +
            ", listpriceincludestax='" + getListpriceincludestax() + "'" +
            ", adjustments='" + getAdjustments() + "'" +
            ", taxlines='" + getTaxlines() + "'" +
            ", cancelled='" + getCancelled() + "'" +
            ", line=" + getLine() +
            ", refund=" + getRefund() +
            ", fulfillments=" + getFulfillments() +
            ", orderModifications=" + getOrderModifications() +
            "}";
    }
}
