package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.HistoryEntry} entity.
 */
public class HistoryEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String type;

    @NotNull
    private Boolean ispublic;

    @NotNull
    @Size(max = 255)
    private String data;

    @NotNull
    @Size(max = 255)
    private String discriminator;

    private AdministratorDTO administrator;

    private CustomerDTO customer;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIspublic() {
        return ispublic;
    }

    public void setIspublic(Boolean ispublic) {
        this.ispublic = ispublic;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public AdministratorDTO getAdministrator() {
        return administrator;
    }

    public void setAdministrator(AdministratorDTO administrator) {
        this.administrator = administrator;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
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
        if (!(o instanceof HistoryEntryDTO)) {
            return false;
        }

        HistoryEntryDTO historyEntryDTO = (HistoryEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historyEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryEntryDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", type='" + getType() + "'" +
            ", ispublic='" + getIspublic() + "'" +
            ", data='" + getData() + "'" +
            ", discriminator='" + getDiscriminator() + "'" +
            ", administrator=" + getAdministrator() +
            ", customer=" + getCustomer() +
            ", jorder=" + getJorder() +
            "}";
    }
}
