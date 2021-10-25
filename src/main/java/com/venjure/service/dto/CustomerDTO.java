package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Customer} entity.
 */
public class CustomerDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    private Instant deletedat;

    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    private String firstname;

    @NotNull
    @Size(max = 255)
    private String lastname;

    @Size(max = 255)
    private String phonenumber;

    @NotNull
    @Size(max = 255)
    private String emailaddress;

    private UserDTO user;

    private AssetDTO avatar;

    private Set<ChannelDTO> channels = new HashSet<>();

    private Set<CustomerGroupDTO> customerGroups = new HashSet<>();

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AssetDTO getAvatar() {
        return avatar;
    }

    public void setAvatar(AssetDTO avatar) {
        this.avatar = avatar;
    }

    public Set<ChannelDTO> getChannels() {
        return channels;
    }

    public void setChannels(Set<ChannelDTO> channels) {
        this.channels = channels;
    }

    public Set<CustomerGroupDTO> getCustomerGroups() {
        return customerGroups;
    }

    public void setCustomerGroups(Set<CustomerGroupDTO> customerGroups) {
        this.customerGroups = customerGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerDTO)) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", title='" + getTitle() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", phonenumber='" + getPhonenumber() + "'" +
            ", emailaddress='" + getEmailaddress() + "'" +
            ", user=" + getUser() +
            ", avatar=" + getAvatar() +
            ", channels=" + getChannels() +
            ", customerGroups=" + getCustomerGroups() +
            "}";
    }
}
