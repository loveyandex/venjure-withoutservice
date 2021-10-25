package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Promotion} entity.
 */
public class PromotionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    private Instant deletedat;

    private Instant startsat;

    private Instant endsat;

    @Size(max = 255)
    private String couponcode;

    private Integer percustomerusagelimit;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Boolean enabled;

    @NotNull
    @Size(max = 255)
    private String conditions;

    @NotNull
    @Size(max = 255)
    private String actions;

    @NotNull
    private Integer priorityscore;

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

    public Instant getStartsat() {
        return startsat;
    }

    public void setStartsat(Instant startsat) {
        this.startsat = startsat;
    }

    public Instant getEndsat() {
        return endsat;
    }

    public void setEndsat(Instant endsat) {
        this.endsat = endsat;
    }

    public String getCouponcode() {
        return couponcode;
    }

    public void setCouponcode(String couponcode) {
        this.couponcode = couponcode;
    }

    public Integer getPercustomerusagelimit() {
        return percustomerusagelimit;
    }

    public void setPercustomerusagelimit(Integer percustomerusagelimit) {
        this.percustomerusagelimit = percustomerusagelimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Integer getPriorityscore() {
        return priorityscore;
    }

    public void setPriorityscore(Integer priorityscore) {
        this.priorityscore = priorityscore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionDTO)) {
            return false;
        }

        PromotionDTO promotionDTO = (PromotionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promotionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromotionDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", startsat='" + getStartsat() + "'" +
            ", endsat='" + getEndsat() + "'" +
            ", couponcode='" + getCouponcode() + "'" +
            ", percustomerusagelimit=" + getPercustomerusagelimit() +
            ", name='" + getName() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", actions='" + getActions() + "'" +
            ", priorityscore=" + getPriorityscore() +
            "}";
    }
}
