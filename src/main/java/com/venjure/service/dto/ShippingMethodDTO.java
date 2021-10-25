package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.ShippingMethod} entity.
 */
public class ShippingMethodDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    private Instant deletedat;

    @NotNull
    @Size(max = 255)
    private String code;

    @NotNull
    @Size(max = 255)
    private String checker;

    @NotNull
    @Size(max = 255)
    private String calculator;

    @NotNull
    @Size(max = 255)
    private String fulfillmenthandlercode;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getCalculator() {
        return calculator;
    }

    public void setCalculator(String calculator) {
        this.calculator = calculator;
    }

    public String getFulfillmenthandlercode() {
        return fulfillmenthandlercode;
    }

    public void setFulfillmenthandlercode(String fulfillmenthandlercode) {
        this.fulfillmenthandlercode = fulfillmenthandlercode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShippingMethodDTO)) {
            return false;
        }

        ShippingMethodDTO shippingMethodDTO = (ShippingMethodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shippingMethodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingMethodDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", code='" + getCode() + "'" +
            ", checker='" + getChecker() + "'" +
            ", calculator='" + getCalculator() + "'" +
            ", fulfillmenthandlercode='" + getFulfillmenthandlercode() + "'" +
            "}";
    }
}
