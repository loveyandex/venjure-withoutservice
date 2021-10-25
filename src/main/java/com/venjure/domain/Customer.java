package com.venjure.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Customer implements Serializable {

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

    @Column(name = "deletedat")
    private Instant deletedat;

    @Size(max = 255)
    @Column(name = "title", length = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    @Column(name = "firstname", length = 255, nullable = false)
    private String firstname;

    @NotNull
    @Size(max = 255)
    @Column(name = "lastname", length = 255, nullable = false)
    private String lastname;

    @Size(max = 255)
    @Column(name = "phonenumber", length = 255)
    private String phonenumber;

    @NotNull
    @Size(max = 255)
    @Column(name = "emailaddress", length = 255, nullable = false)
    private String emailaddress;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    private Asset avatar;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_customer__channel",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    @JsonIgnoreProperties(
        value = {
            "defaulttaxzone",
            "defaultshippingzone",
            "paymentMethods",
            "products",
            "promotions",
            "shippingMethods",
            "customers",
            "facets",
            "facetValues",
            "jorders",
            "productVariants",
        },
        allowSetters = true
    )
    private Set<Channel> channels = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_customer__customer_group",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "customer_group_id")
    )
    @JsonIgnoreProperties(value = { "customers" }, allowSetters = true)
    private Set<CustomerGroup> customerGroups = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer", "country" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "administrator", "customer", "jorder" }, allowSetters = true)
    private Set<HistoryEntry> historyEntries = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "customer",
            "channels",
            "promotions",
            "historyEntries",
            "orderLines",
            "orderModifications",
            "payments",
            "shippingLines",
            "surcharges",
        },
        allowSetters = true
    )
    private Set<Jorder> jorders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Customer createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Customer updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Instant getDeletedat() {
        return this.deletedat;
    }

    public Customer deletedat(Instant deletedat) {
        this.deletedat = deletedat;
        return this;
    }

    public void setDeletedat(Instant deletedat) {
        this.deletedat = deletedat;
    }

    public String getTitle() {
        return this.title;
    }

    public Customer title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Customer firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Customer lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public Customer phonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmailaddress() {
        return this.emailaddress;
    }

    public Customer emailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
        return this;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public User getUser() {
        return this.user;
    }

    public Customer user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Asset getAvatar() {
        return this.avatar;
    }

    public Customer avatar(Asset asset) {
        this.setAvatar(asset);
        return this;
    }

    public void setAvatar(Asset asset) {
        this.avatar = asset;
    }

    public Set<Channel> getChannels() {
        return this.channels;
    }

    public Customer channels(Set<Channel> channels) {
        this.setChannels(channels);
        return this;
    }

    public Customer addChannel(Channel channel) {
        this.channels.add(channel);
        channel.getCustomers().add(this);
        return this;
    }

    public Customer removeChannel(Channel channel) {
        this.channels.remove(channel);
        channel.getCustomers().remove(this);
        return this;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public Set<CustomerGroup> getCustomerGroups() {
        return this.customerGroups;
    }

    public Customer customerGroups(Set<CustomerGroup> customerGroups) {
        this.setCustomerGroups(customerGroups);
        return this;
    }

    public Customer addCustomerGroup(CustomerGroup customerGroup) {
        this.customerGroups.add(customerGroup);
        customerGroup.getCustomers().add(this);
        return this;
    }

    public Customer removeCustomerGroup(CustomerGroup customerGroup) {
        this.customerGroups.remove(customerGroup);
        customerGroup.getCustomers().remove(this);
        return this;
    }

    public void setCustomerGroups(Set<CustomerGroup> customerGroups) {
        this.customerGroups = customerGroups;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public Customer addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Customer addAddress(Address address) {
        this.addresses.add(address);
        address.setCustomer(this);
        return this;
    }

    public Customer removeAddress(Address address) {
        this.addresses.remove(address);
        address.setCustomer(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCustomer(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setCustomer(this));
        }
        this.addresses = addresses;
    }

    public Set<HistoryEntry> getHistoryEntries() {
        return this.historyEntries;
    }

    public Customer historyEntries(Set<HistoryEntry> historyEntries) {
        this.setHistoryEntries(historyEntries);
        return this;
    }

    public Customer addHistoryEntry(HistoryEntry historyEntry) {
        this.historyEntries.add(historyEntry);
        historyEntry.setCustomer(this);
        return this;
    }

    public Customer removeHistoryEntry(HistoryEntry historyEntry) {
        this.historyEntries.remove(historyEntry);
        historyEntry.setCustomer(null);
        return this;
    }

    public void setHistoryEntries(Set<HistoryEntry> historyEntries) {
        if (this.historyEntries != null) {
            this.historyEntries.forEach(i -> i.setCustomer(null));
        }
        if (historyEntries != null) {
            historyEntries.forEach(i -> i.setCustomer(this));
        }
        this.historyEntries = historyEntries;
    }

    public Set<Jorder> getJorders() {
        return this.jorders;
    }

    public Customer jorders(Set<Jorder> jorders) {
        this.setJorders(jorders);
        return this;
    }

    public Customer addJorder(Jorder jorder) {
        this.jorders.add(jorder);
        jorder.setCustomer(this);
        return this;
    }

    public Customer removeJorder(Jorder jorder) {
        this.jorders.remove(jorder);
        jorder.setCustomer(null);
        return this;
    }

    public void setJorders(Set<Jorder> jorders) {
        if (this.jorders != null) {
            this.jorders.forEach(i -> i.setCustomer(null));
        }
        if (jorders != null) {
            jorders.forEach(i -> i.setCustomer(this));
        }
        this.jorders = jorders;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", title='" + getTitle() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", phonenumber='" + getPhonenumber() + "'" +
            ", emailaddress='" + getEmailaddress() + "'" +
            "}";
    }
}
