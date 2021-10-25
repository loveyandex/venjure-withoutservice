package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "createdat", nullable = false)
    private Instant createdat;

    @NotNull
    @Column(name = "updatedat", nullable = false)
    private Instant updatedat;

    @NotNull
    @Size(max = 255)
    @Column(name = "fullname", length = 255, nullable = false)
    private String fullname;

    @NotNull
    @Size(max = 255)
    @Column(name = "company", length = 255, nullable = false)
    private String company;

    @NotNull
    @Size(max = 255)
    @Column(name = "streetline_1", length = 255, nullable = false)
    private String streetline1;

    @NotNull
    @Size(max = 255)
    @Column(name = "streetline_2", length = 255, nullable = false)
    private String streetline2;

    @NotNull
    @Size(max = 255)
    @Column(name = "city", length = 255, nullable = false)
    private String city;

    @NotNull
    @Size(max = 255)
    @Column(name = "province", length = 255, nullable = false)
    private String province;

    @NotNull
    @Size(max = 255)
    @Column(name = "postalcode", length = 255, nullable = false)
    private String postalcode;

    @NotNull
    @Size(max = 255)
    @Column(name = "phonenumber", length = 255, nullable = false)
    private String phonenumber;

    @NotNull
    @Column(name = "defaultshippingaddress", nullable = false)
    private Boolean defaultshippingaddress;

    @NotNull
    @Column(name = "defaultbillingaddress", nullable = false)
    private Boolean defaultbillingaddress;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "avatar", "channels", "customerGroups", "addresses", "historyEntries", "jorders" },
        allowSetters = true
    )
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "countryTranslations", "zones" }, allowSetters = true)
    private Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Address createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Address updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getFullname() {
        return this.fullname;
    }

    public Address fullname(String fullname) {
        this.fullname = fullname;
        return this;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCompany() {
        return this.company;
    }

    public Address company(String company) {
        this.company = company;
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStreetline1() {
        return this.streetline1;
    }

    public Address streetline1(String streetline1) {
        this.streetline1 = streetline1;
        return this;
    }

    public void setStreetline1(String streetline1) {
        this.streetline1 = streetline1;
    }

    public String getStreetline2() {
        return this.streetline2;
    }

    public Address streetline2(String streetline2) {
        this.streetline2 = streetline2;
        return this;
    }

    public void setStreetline2(String streetline2) {
        this.streetline2 = streetline2;
    }

    public String getCity() {
        return this.city;
    }

    public Address city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    public Address province(String province) {
        this.province = province;
        return this;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalcode() {
        return this.postalcode;
    }

    public Address postalcode(String postalcode) {
        this.postalcode = postalcode;
        return this;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public Address phonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Boolean getDefaultshippingaddress() {
        return this.defaultshippingaddress;
    }

    public Address defaultshippingaddress(Boolean defaultshippingaddress) {
        this.defaultshippingaddress = defaultshippingaddress;
        return this;
    }

    public void setDefaultshippingaddress(Boolean defaultshippingaddress) {
        this.defaultshippingaddress = defaultshippingaddress;
    }

    public Boolean getDefaultbillingaddress() {
        return this.defaultbillingaddress;
    }

    public Address defaultbillingaddress(Boolean defaultbillingaddress) {
        this.defaultbillingaddress = defaultbillingaddress;
        return this;
    }

    public void setDefaultbillingaddress(Boolean defaultbillingaddress) {
        this.defaultbillingaddress = defaultbillingaddress;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Address customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Country getCountry() {
        return this.country;
    }

    public Address country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
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
            "}";
    }
}
