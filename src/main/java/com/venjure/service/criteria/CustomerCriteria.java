package com.venjure.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.venjure.domain.Customer} entity. This class is used
 * in {@link com.venjure.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private InstantFilter deletedat;

    private StringFilter title;

    private StringFilter firstname;

    private StringFilter lastname;

    private StringFilter phonenumber;

    private StringFilter emailaddress;

    private LongFilter userId;

    private LongFilter avatarId;

    private LongFilter channelId;

    private LongFilter customerGroupId;

    private LongFilter addressId;

    private LongFilter historyEntryId;

    private LongFilter jorderId;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.deletedat = other.deletedat == null ? null : other.deletedat.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.firstname = other.firstname == null ? null : other.firstname.copy();
        this.lastname = other.lastname == null ? null : other.lastname.copy();
        this.phonenumber = other.phonenumber == null ? null : other.phonenumber.copy();
        this.emailaddress = other.emailaddress == null ? null : other.emailaddress.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.avatarId = other.avatarId == null ? null : other.avatarId.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.customerGroupId = other.customerGroupId == null ? null : other.customerGroupId.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.historyEntryId = other.historyEntryId == null ? null : other.historyEntryId.copy();
        this.jorderId = other.jorderId == null ? null : other.jorderId.copy();
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreatedat() {
        return createdat;
    }

    public InstantFilter createdat() {
        if (createdat == null) {
            createdat = new InstantFilter();
        }
        return createdat;
    }

    public void setCreatedat(InstantFilter createdat) {
        this.createdat = createdat;
    }

    public InstantFilter getUpdatedat() {
        return updatedat;
    }

    public InstantFilter updatedat() {
        if (updatedat == null) {
            updatedat = new InstantFilter();
        }
        return updatedat;
    }

    public void setUpdatedat(InstantFilter updatedat) {
        this.updatedat = updatedat;
    }

    public InstantFilter getDeletedat() {
        return deletedat;
    }

    public InstantFilter deletedat() {
        if (deletedat == null) {
            deletedat = new InstantFilter();
        }
        return deletedat;
    }

    public void setDeletedat(InstantFilter deletedat) {
        this.deletedat = deletedat;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getFirstname() {
        return firstname;
    }

    public StringFilter firstname() {
        if (firstname == null) {
            firstname = new StringFilter();
        }
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public StringFilter getLastname() {
        return lastname;
    }

    public StringFilter lastname() {
        if (lastname == null) {
            lastname = new StringFilter();
        }
        return lastname;
    }

    public void setLastname(StringFilter lastname) {
        this.lastname = lastname;
    }

    public StringFilter getPhonenumber() {
        return phonenumber;
    }

    public StringFilter phonenumber() {
        if (phonenumber == null) {
            phonenumber = new StringFilter();
        }
        return phonenumber;
    }

    public void setPhonenumber(StringFilter phonenumber) {
        this.phonenumber = phonenumber;
    }

    public StringFilter getEmailaddress() {
        return emailaddress;
    }

    public StringFilter emailaddress() {
        if (emailaddress == null) {
            emailaddress = new StringFilter();
        }
        return emailaddress;
    }

    public void setEmailaddress(StringFilter emailaddress) {
        this.emailaddress = emailaddress;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getAvatarId() {
        return avatarId;
    }

    public LongFilter avatarId() {
        if (avatarId == null) {
            avatarId = new LongFilter();
        }
        return avatarId;
    }

    public void setAvatarId(LongFilter avatarId) {
        this.avatarId = avatarId;
    }

    public LongFilter getChannelId() {
        return channelId;
    }

    public LongFilter channelId() {
        if (channelId == null) {
            channelId = new LongFilter();
        }
        return channelId;
    }

    public void setChannelId(LongFilter channelId) {
        this.channelId = channelId;
    }

    public LongFilter getCustomerGroupId() {
        return customerGroupId;
    }

    public LongFilter customerGroupId() {
        if (customerGroupId == null) {
            customerGroupId = new LongFilter();
        }
        return customerGroupId;
    }

    public void setCustomerGroupId(LongFilter customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getHistoryEntryId() {
        return historyEntryId;
    }

    public LongFilter historyEntryId() {
        if (historyEntryId == null) {
            historyEntryId = new LongFilter();
        }
        return historyEntryId;
    }

    public void setHistoryEntryId(LongFilter historyEntryId) {
        this.historyEntryId = historyEntryId;
    }

    public LongFilter getJorderId() {
        return jorderId;
    }

    public LongFilter jorderId() {
        if (jorderId == null) {
            jorderId = new LongFilter();
        }
        return jorderId;
    }

    public void setJorderId(LongFilter jorderId) {
        this.jorderId = jorderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(deletedat, that.deletedat) &&
            Objects.equals(title, that.title) &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(lastname, that.lastname) &&
            Objects.equals(phonenumber, that.phonenumber) &&
            Objects.equals(emailaddress, that.emailaddress) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(avatarId, that.avatarId) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(customerGroupId, that.customerGroupId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(historyEntryId, that.historyEntryId) &&
            Objects.equals(jorderId, that.jorderId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            deletedat,
            title,
            firstname,
            lastname,
            phonenumber,
            emailaddress,
            userId,
            avatarId,
            channelId,
            customerGroupId,
            addressId,
            historyEntryId,
            jorderId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (deletedat != null ? "deletedat=" + deletedat + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (firstname != null ? "firstname=" + firstname + ", " : "") +
            (lastname != null ? "lastname=" + lastname + ", " : "") +
            (phonenumber != null ? "phonenumber=" + phonenumber + ", " : "") +
            (emailaddress != null ? "emailaddress=" + emailaddress + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (avatarId != null ? "avatarId=" + avatarId + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            (customerGroupId != null ? "customerGroupId=" + customerGroupId + ", " : "") +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (historyEntryId != null ? "historyEntryId=" + historyEntryId + ", " : "") +
            (jorderId != null ? "jorderId=" + jorderId + ", " : "") +
            "}";
    }
}
