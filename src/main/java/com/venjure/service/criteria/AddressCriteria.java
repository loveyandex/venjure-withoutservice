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
 * Criteria class for the {@link com.venjure.domain.Address} entity. This class is used
 * in {@link com.venjure.web.rest.AddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter fullname;

    private StringFilter company;

    private StringFilter streetline1;

    private StringFilter streetline2;

    private StringFilter city;

    private StringFilter province;

    private StringFilter postalcode;

    private StringFilter phonenumber;

    private BooleanFilter defaultshippingaddress;

    private BooleanFilter defaultbillingaddress;

    private LongFilter customerId;

    private LongFilter countryId;

    public AddressCriteria() {}

    public AddressCriteria(AddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.fullname = other.fullname == null ? null : other.fullname.copy();
        this.company = other.company == null ? null : other.company.copy();
        this.streetline1 = other.streetline1 == null ? null : other.streetline1.copy();
        this.streetline2 = other.streetline2 == null ? null : other.streetline2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.province = other.province == null ? null : other.province.copy();
        this.postalcode = other.postalcode == null ? null : other.postalcode.copy();
        this.phonenumber = other.phonenumber == null ? null : other.phonenumber.copy();
        this.defaultshippingaddress = other.defaultshippingaddress == null ? null : other.defaultshippingaddress.copy();
        this.defaultbillingaddress = other.defaultbillingaddress == null ? null : other.defaultbillingaddress.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.countryId = other.countryId == null ? null : other.countryId.copy();
    }

    @Override
    public AddressCriteria copy() {
        return new AddressCriteria(this);
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

    public StringFilter getFullname() {
        return fullname;
    }

    public StringFilter fullname() {
        if (fullname == null) {
            fullname = new StringFilter();
        }
        return fullname;
    }

    public void setFullname(StringFilter fullname) {
        this.fullname = fullname;
    }

    public StringFilter getCompany() {
        return company;
    }

    public StringFilter company() {
        if (company == null) {
            company = new StringFilter();
        }
        return company;
    }

    public void setCompany(StringFilter company) {
        this.company = company;
    }

    public StringFilter getStreetline1() {
        return streetline1;
    }

    public StringFilter streetline1() {
        if (streetline1 == null) {
            streetline1 = new StringFilter();
        }
        return streetline1;
    }

    public void setStreetline1(StringFilter streetline1) {
        this.streetline1 = streetline1;
    }

    public StringFilter getStreetline2() {
        return streetline2;
    }

    public StringFilter streetline2() {
        if (streetline2 == null) {
            streetline2 = new StringFilter();
        }
        return streetline2;
    }

    public void setStreetline2(StringFilter streetline2) {
        this.streetline2 = streetline2;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getProvince() {
        return province;
    }

    public StringFilter province() {
        if (province == null) {
            province = new StringFilter();
        }
        return province;
    }

    public void setProvince(StringFilter province) {
        this.province = province;
    }

    public StringFilter getPostalcode() {
        return postalcode;
    }

    public StringFilter postalcode() {
        if (postalcode == null) {
            postalcode = new StringFilter();
        }
        return postalcode;
    }

    public void setPostalcode(StringFilter postalcode) {
        this.postalcode = postalcode;
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

    public BooleanFilter getDefaultshippingaddress() {
        return defaultshippingaddress;
    }

    public BooleanFilter defaultshippingaddress() {
        if (defaultshippingaddress == null) {
            defaultshippingaddress = new BooleanFilter();
        }
        return defaultshippingaddress;
    }

    public void setDefaultshippingaddress(BooleanFilter defaultshippingaddress) {
        this.defaultshippingaddress = defaultshippingaddress;
    }

    public BooleanFilter getDefaultbillingaddress() {
        return defaultbillingaddress;
    }

    public BooleanFilter defaultbillingaddress() {
        if (defaultbillingaddress == null) {
            defaultbillingaddress = new BooleanFilter();
        }
        return defaultbillingaddress;
    }

    public void setDefaultbillingaddress(BooleanFilter defaultbillingaddress) {
        this.defaultbillingaddress = defaultbillingaddress;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getCountryId() {
        return countryId;
    }

    public LongFilter countryId() {
        if (countryId == null) {
            countryId = new LongFilter();
        }
        return countryId;
    }

    public void setCountryId(LongFilter countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AddressCriteria that = (AddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(fullname, that.fullname) &&
            Objects.equals(company, that.company) &&
            Objects.equals(streetline1, that.streetline1) &&
            Objects.equals(streetline2, that.streetline2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(province, that.province) &&
            Objects.equals(postalcode, that.postalcode) &&
            Objects.equals(phonenumber, that.phonenumber) &&
            Objects.equals(defaultshippingaddress, that.defaultshippingaddress) &&
            Objects.equals(defaultbillingaddress, that.defaultbillingaddress) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(countryId, that.countryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            fullname,
            company,
            streetline1,
            streetline2,
            city,
            province,
            postalcode,
            phonenumber,
            defaultshippingaddress,
            defaultbillingaddress,
            customerId,
            countryId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (fullname != null ? "fullname=" + fullname + ", " : "") +
            (company != null ? "company=" + company + ", " : "") +
            (streetline1 != null ? "streetline1=" + streetline1 + ", " : "") +
            (streetline2 != null ? "streetline2=" + streetline2 + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (province != null ? "province=" + province + ", " : "") +
            (postalcode != null ? "postalcode=" + postalcode + ", " : "") +
            (phonenumber != null ? "phonenumber=" + phonenumber + ", " : "") +
            (defaultshippingaddress != null ? "defaultshippingaddress=" + defaultshippingaddress + ", " : "") +
            (defaultbillingaddress != null ? "defaultbillingaddress=" + defaultbillingaddress + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (countryId != null ? "countryId=" + countryId + ", " : "") +
            "}";
    }
}
