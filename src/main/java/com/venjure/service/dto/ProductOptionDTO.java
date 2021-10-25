package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.ProductOption} entity.
 */
public class ProductOptionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    private Instant deletedat;

    @NotNull
    @Size(max = 255)
    private String code;

    private ProductOptionGroupDTO group;

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

    public ProductOptionGroupDTO getGroup() {
        return group;
    }

    public void setGroup(ProductOptionGroupDTO group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOptionDTO)) {
            return false;
        }

        ProductOptionDTO productOptionDTO = (ProductOptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productOptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOptionDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", code='" + getCode() + "'" +
            ", group=" + getGroup() +
            "}";
    }
}
