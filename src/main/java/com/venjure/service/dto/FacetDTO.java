package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Facet} entity.
 */
public class FacetDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    private Boolean isprivate;

    @NotNull
    @Size(max = 255)
    private String code;

    private Set<ChannelDTO> channels = new HashSet<>();

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

    public Boolean getIsprivate() {
        return isprivate;
    }

    public void setIsprivate(Boolean isprivate) {
        this.isprivate = isprivate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<ChannelDTO> getChannels() {
        return channels;
    }

    public void setChannels(Set<ChannelDTO> channels) {
        this.channels = channels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacetDTO)) {
            return false;
        }

        FacetDTO facetDTO = (FacetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacetDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", isprivate='" + getIsprivate() + "'" +
            ", code='" + getCode() + "'" +
            ", channels=" + getChannels() +
            "}";
    }
}
