package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Collection} entity.
 */
public class CollectionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    private Boolean isroot;

    @NotNull
    private Integer position;

    @NotNull
    private Boolean isprivate;

    @NotNull
    @Size(max = 255)
    private String filters;

    private AssetDTO featuredasset;

    private CollectionDTO parent;

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

    public Boolean getIsroot() {
        return isroot;
    }

    public void setIsroot(Boolean isroot) {
        this.isroot = isroot;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean getIsprivate() {
        return isprivate;
    }

    public void setIsprivate(Boolean isprivate) {
        this.isprivate = isprivate;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public AssetDTO getFeaturedasset() {
        return featuredasset;
    }

    public void setFeaturedasset(AssetDTO featuredasset) {
        this.featuredasset = featuredasset;
    }

    public CollectionDTO getParent() {
        return parent;
    }

    public void setParent(CollectionDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectionDTO)) {
            return false;
        }

        CollectionDTO collectionDTO = (CollectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, collectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollectionDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", isroot='" + getIsroot() + "'" +
            ", position=" + getPosition() +
            ", isprivate='" + getIsprivate() + "'" +
            ", filters='" + getFilters() + "'" +
            ", featuredasset=" + getFeaturedasset() +
            ", parent=" + getParent() +
            "}";
    }
}
