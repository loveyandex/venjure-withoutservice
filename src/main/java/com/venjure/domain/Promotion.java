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
 * A Promotion.
 */
@Entity
@Table(name = "promotion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Promotion implements Serializable {

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

    @Column(name = "startsat")
    private Instant startsat;

    @Column(name = "endsat")
    private Instant endsat;

    @Size(max = 255)
    @Column(name = "couponcode", length = 255)
    private String couponcode;

    @Column(name = "percustomerusagelimit")
    private Integer percustomerusagelimit;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NotNull
    @Size(max = 255)
    @Column(name = "conditions", length = 255, nullable = false)
    private String conditions;

    @NotNull
    @Size(max = 255)
    @Column(name = "actions", length = 255, nullable = false)
    private String actions;

    @NotNull
    @Column(name = "priorityscore", nullable = false)
    private Integer priorityscore;

    @ManyToMany(mappedBy = "promotions")
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

    @ManyToMany(mappedBy = "promotions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Promotion id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Promotion createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Promotion updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Instant getDeletedat() {
        return this.deletedat;
    }

    public Promotion deletedat(Instant deletedat) {
        this.deletedat = deletedat;
        return this;
    }

    public void setDeletedat(Instant deletedat) {
        this.deletedat = deletedat;
    }

    public Instant getStartsat() {
        return this.startsat;
    }

    public Promotion startsat(Instant startsat) {
        this.startsat = startsat;
        return this;
    }

    public void setStartsat(Instant startsat) {
        this.startsat = startsat;
    }

    public Instant getEndsat() {
        return this.endsat;
    }

    public Promotion endsat(Instant endsat) {
        this.endsat = endsat;
        return this;
    }

    public void setEndsat(Instant endsat) {
        this.endsat = endsat;
    }

    public String getCouponcode() {
        return this.couponcode;
    }

    public Promotion couponcode(String couponcode) {
        this.couponcode = couponcode;
        return this;
    }

    public void setCouponcode(String couponcode) {
        this.couponcode = couponcode;
    }

    public Integer getPercustomerusagelimit() {
        return this.percustomerusagelimit;
    }

    public Promotion percustomerusagelimit(Integer percustomerusagelimit) {
        this.percustomerusagelimit = percustomerusagelimit;
        return this;
    }

    public void setPercustomerusagelimit(Integer percustomerusagelimit) {
        this.percustomerusagelimit = percustomerusagelimit;
    }

    public String getName() {
        return this.name;
    }

    public Promotion name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public Promotion enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getConditions() {
        return this.conditions;
    }

    public Promotion conditions(String conditions) {
        this.conditions = conditions;
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getActions() {
        return this.actions;
    }

    public Promotion actions(String actions) {
        this.actions = actions;
        return this;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Integer getPriorityscore() {
        return this.priorityscore;
    }

    public Promotion priorityscore(Integer priorityscore) {
        this.priorityscore = priorityscore;
        return this;
    }

    public void setPriorityscore(Integer priorityscore) {
        this.priorityscore = priorityscore;
    }

    public Set<Jorder> getJorders() {
        return this.jorders;
    }

    public Promotion jorders(Set<Jorder> jorders) {
        this.setJorders(jorders);
        return this;
    }

    public Promotion addJorder(Jorder jorder) {
        this.jorders.add(jorder);
        jorder.getPromotions().add(this);
        return this;
    }

    public Promotion removeJorder(Jorder jorder) {
        this.jorders.remove(jorder);
        jorder.getPromotions().remove(this);
        return this;
    }

    public void setJorders(Set<Jorder> jorders) {
        if (this.jorders != null) {
            this.jorders.forEach(i -> i.removePromotion(this));
        }
        if (jorders != null) {
            jorders.forEach(i -> i.addPromotion(this));
        }
        this.jorders = jorders;
    }

    public Set<Channel> getChannels() {
        return this.channels;
    }

    public Promotion channels(Set<Channel> channels) {
        this.setChannels(channels);
        return this;
    }

    public Promotion addChannel(Channel channel) {
        this.channels.add(channel);
        channel.getPromotions().add(this);
        return this;
    }

    public Promotion removeChannel(Channel channel) {
        this.channels.remove(channel);
        channel.getPromotions().remove(this);
        return this;
    }

    public void setChannels(Set<Channel> channels) {
        if (this.channels != null) {
            this.channels.forEach(i -> i.removePromotion(this));
        }
        if (channels != null) {
            channels.forEach(i -> i.addPromotion(this));
        }
        this.channels = channels;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Promotion)) {
            return false;
        }
        return id != null && id.equals(((Promotion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Promotion{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", startsat='" + getStartsat() + "'" +
            ", endsat='" + getEndsat() + "'" +
            ", couponcode='" + getCouponcode() + "'" +
            ", percustomerusagelimit=" + getPercustomerusagelimit() +
            ", name='" + getName() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", actions='" + getActions() + "'" +
            ", priorityscore=" + getPriorityscore() +
            "}";
    }
}
