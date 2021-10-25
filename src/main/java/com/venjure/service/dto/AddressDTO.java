package com.venjure.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.venjure.domain.Address} entity.
 */
public class AddressDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdat;

    @NotNull
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    private String fullname;

    @NotNull
    @Size(max = 255)
    private String company;

    @NotNull
    @Size(max = 255)
    private String streetline1;

    @NotNull
    @Size(max = 255)
    private String streetline2;

    @NotNull
    @Size(max = 255)
    private String city;

    @NotNull
    @Size(max = 255)
    private String province;

    @NotNull
    @Size(max = 255)
    private String postalcode;

    @NotNull
    @Size(max = 255)
    private String phonenumber;

    @NotNull
    private Boolean defaultshippingaddress;

    @NotNull
    private Boolean defaultbillingaddress;

    private CustomerDTO customer;

    private CountryDTO country;

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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStreetline1() {
        return streetline1;
    }

    public void setStreetline1(String streetline1) {
        this.streetline1 = streetline1;
    }

    public String getStreetline2() {
        return streetline2;
    }

    public void setStreetline2(String streetline2) {
        this.streetline2 = streetline2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Boolean getDefaultshippingaddress() {
        return defaultshippingaddress;
    }

    public void setDefaultshippingaddress(Boolean defaultshippingaddress) {
        this.defaultshippingaddress = defaultshippingaddress;
    }

    public Boolean getDefaultbillingaddress() {
        return defaultbillingaddress;
    }

    public void setDefaultbillingaddress(Boolean defaultbillingaddress) {
        this.defaultbillingaddress = defaultbillingaddress;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressDTO)) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, addressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressDTO{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", fullname='" + getFullname() + "'" +
            ", company='" + getCompany() + "'" +
            ", streetline1='" + getStreetline1() + "'" +
            ", streetline2='" + getStreetline2() + "'" +
            ", city='" + getCity() + "'" +
            ", province='" + getProvince() + "'" +
            ", postalcode='" + getPostalcode() + "'" +
            ", phonenumber='" + getPhonenumber() + "'" +
            ", defaultshippingaddress='" + getDefaultshippingaddress() + "'" +
            ", defaultbillingaddress='" + getDefaultbillingaddress() + "'" +
            ", customer=" + getCustomer() +
            ", country=" + getCountry() +
            "}";
    }
}
