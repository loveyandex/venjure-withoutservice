package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.FacetValue} entity.
 */
public class FacetValueDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String code;

    private FacetDTO facet;

    private Set<ChannelDTO> channels = new HashSet<>();

    private Set<ProductDTO> products = new HashSet<>();

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

    public FacetDTO getFacet() {
        return facet;
    }

    public void setFacet(FacetDTO facet) {
        this.facet = facet;
    }

    public Set<ChannelDTO> getChannels() {
        return channels;
    }

    public void setChannels(Set<ChannelDTO> channels) {
        this.channels = channels;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacetValueDTO)) {
            return false;
        }

        FacetValueDTO facetValueDTO = (FacetValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facetValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacetValueDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", code='" + getCode() + "'" +
            ", facet=" + getFacet() +
            ", channels=" + getChannels() +
            ", products=" + getProducts() +
            "}";
    }
}
