package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Fulfillment} entity.
 */
public class FulfillmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String state;

    @NotNull
    @Size(max = 255)
    private String trackingcode;

    @NotNull
    @Size(max = 255)
    private String method;

    @NotNull
    @Size(max = 255)
    private String handlercode;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTrackingcode() {
        return trackingcode;
    }

    public void setTrackingcode(String trackingcode) {
        this.trackingcode = trackingcode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHandlercode() {
        return handlercode;
    }

    public void setHandlercode(String handlercode) {
        this.handlercode = handlercode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FulfillmentDTO)) {
            return false;
        }

        FulfillmentDTO fulfillmentDTO = (FulfillmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fulfillmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FulfillmentDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", state='" + getState() + "'" +
            ", trackingcode='" + getTrackingcode() + "'" +
            ", method='" + getMethod() + "'" +
            ", handlercode='" + getHandlercode() + "'" +
            "}";
    }
}
