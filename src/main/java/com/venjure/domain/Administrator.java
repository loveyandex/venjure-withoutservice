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
 * A Administrator.
 */
@Entity
@Table(name = "administrator")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Administrator implements Serializable {

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

    @NotNull
    @Size(max = 255)
    @Column(name = "firstname", length = 255, nullable = false)
    private String firstname;

    @NotNull
    @Size(max = 255)
    @Column(name = "lastname", length = 255, nullable = false)
    private String lastname;

    @NotNull
    @Size(max = 255)
    @Column(name = "emailaddress", length = 255, nullable = false, unique = true)
    private String emailaddress;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "administrator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "administrator", "customer", "jorder" }, allowSetters = true)
    private Set<HistoryEntry> historyEntries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Administrator id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Administrator createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Administrator updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Instant getDeletedat() {
        return this.deletedat;
    }

    public Administrator deletedat(Instant deletedat) {
        this.deletedat = deletedat;
        return this;
    }

    public void setDeletedat(Instant deletedat) {
        this.deletedat = deletedat;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Administrator firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Administrator lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailaddress() {
        return this.emailaddress;
    }

    public Administrator emailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
        return this;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public User getUser() {
        return this.user;
    }

    public Administrator user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<HistoryEntry> getHistoryEntries() {
        return this.historyEntries;
    }

    public Administrator historyEntries(Set<HistoryEntry> historyEntries) {
        this.setHistoryEntries(historyEntries);
        return this;
    }

    public Administrator addHistoryEntry(HistoryEntry historyEntry) {
        this.historyEntries.add(historyEntry);
        historyEntry.setAdministrator(this);
        return this;
    }

    public Administrator removeHistoryEntry(HistoryEntry historyEntry) {
        this.historyEntries.remove(historyEntry);
        historyEntry.setAdministrator(null);
        return this;
    }

    public void setHistoryEntries(Set<HistoryEntry> historyEntries) {
        if (this.historyEntries != null) {
            this.historyEntries.forEach(i -> i.setAdministrator(null));
        }
        if (historyEntries != null) {
            historyEntries.forEach(i -> i.setAdministrator(this));
        }
        this.historyEntries = historyEntries;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Administrator)) {
            return false;
        }
        return id != null && id.equals(((Administrator) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Administrator{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", emailaddress='" + getEmailaddress() + "'" +
            "}";
    }
}
