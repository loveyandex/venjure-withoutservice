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
 * A ShippingMethod.
 */
@Entity
@Table(name = "shipping_method")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShippingMethod implements Serializable {

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
    @Column(name = "code", length = 255, nullable = false)
    private String code;

    @NotNull
    @Size(max = 255)
    @Column(name = "checker", length = 255, nullable = false)
    private String checker;

    @NotNull
    @Size(max = 255)
    @Column(name = "calculator", length = 255, nullable = false)
    private String calculator;

    @NotNull
    @Size(max = 255)
    @Column(name = "fulfillmenthandlercode", length = 255, nullable = false)
    private String fulfillmenthandlercode;

    @OneToMany(mappedBy = "base")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "base" }, allowSetters = true)
    private Set<ShippingMethodTranslation> shippingMethodTranslations = new HashSet<>();

    @ManyToMany(mappedBy = "shippingMethods")
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

    public ShippingMethod id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public ShippingMethod createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public ShippingMethod updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public Instant getDeletedat() {
        return this.deletedat;
    }

    public ShippingMethod deletedat(Instant deletedat) {
        this.deletedat = deletedat;
        return this;
    }

    public void setDeletedat(Instant deletedat) {
        this.deletedat = deletedat;
    }

    public String getCode() {
        return this.code;
    }

    public ShippingMethod code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChecker() {
        return this.checker;
    }

    public ShippingMethod checker(String checker) {
        this.checker = checker;
        return this;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getCalculator() {
        return this.calculator;
    }

    public ShippingMethod calculator(String calculator) {
        this.calculator = calculator;
        return this;
    }

    public void setCalculator(String calculator) {
        this.calculator = calculator;
    }

    public String getFulfillmenthandlercode() {
        return this.fulfillmenthandlercode;
    }

    public ShippingMethod fulfillmenthandlercode(String fulfillmenthandlercode) {
        this.fulfillmenthandlercode = fulfillmenthandlercode;
        return this;
    }

    public void setFulfillmenthandlercode(String fulfillmenthandlercode) {
        this.fulfillmenthandlercode = fulfillmenthandlercode;
    }

    public Set<ShippingMethodTranslation> getShippingMethodTranslations() {
        return this.shippingMethodTranslations;
    }

    public ShippingMethod shippingMethodTranslations(Set<ShippingMethodTranslation> shippingMethodTranslations) {
        this.setShippingMethodTranslations(shippingMethodTranslations);
        return this;
    }

    public ShippingMethod addShippingMethodTranslation(ShippingMethodTranslation shippingMethodTranslation) {
        this.shippingMethodTranslations.add(shippingMethodTranslation);
        shippingMethodTranslation.setBase(this);
        return this;
    }

    public ShippingMethod removeShippingMethodTranslation(ShippingMethodTranslation shippingMethodTranslation) {
        this.shippingMethodTranslations.remove(shippingMethodTranslation);
        shippingMethodTranslation.setBase(null);
        return this;
    }

    public void setShippingMethodTranslations(Set<ShippingMethodTranslation> shippingMethodTranslations) {
        if (this.shippingMethodTranslations != null) {
            this.shippingMethodTranslations.forEach(i -> i.setBase(null));
        }
        if (shippingMethodTranslations != null) {
            shippingMethodTranslations.forEach(i -> i.setBase(this));
        }
        this.shippingMethodTranslations = shippingMethodTranslations;
    }

    public Set<Channel> getChannels() {
        return this.channels;
    }

    public ShippingMethod channels(Set<Channel> channels) {
        this.setChannels(channels);
        return this;
    }

    public ShippingMethod addChannel(Channel channel) {
        this.channels.add(channel);
        channel.getShippingMethods().add(this);
        return this;
    }

    public ShippingMethod removeChannel(Channel channel) {
        this.channels.remove(channel);
        channel.getShippingMethods().remove(this);
        return this;
    }

    public void setChannels(Set<Channel> channels) {
        if (this.channels != null) {
            this.channels.forEach(i -> i.removeShippingMethod(this));
        }
        if (channels != null) {
            channels.forEach(i -> i.addShippingMethod(this));
        }
        this.channels = channels;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShippingMethod)) {
            return false;
        }
        return id != null && id.equals(((ShippingMethod) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingMethod{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", deletedat='" + getDeletedat() + "'" +
            ", code='" + getCode() + "'" +
            ", checker='" + getChecker() + "'" +
            ", calculator='" + getCalculator() + "'" +
            ", fulfillmenthandlercode='" + getFulfillmenthandlercode() + "'" +
            "}";
    }
}
