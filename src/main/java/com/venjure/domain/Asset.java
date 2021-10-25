package com.venjure.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Asset.
 */
@Entity
@Table(name = "asset")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Asset implements Serializable {

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
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "type", length = 255, nullable = false)
    private String type;

    @NotNull
    @Size(max = 255)
    @Column(name = "mimetype", length = 255, nullable = false)
    private String mimetype;

    @NotNull
    @Column(name = "width", nullable = false)
    private Integer width;

    @NotNull
    @Column(name = "height", nullable = false)
    private Integer height;

    @NotNull
    @Column(name = "filesize", nullable = false)
    private Integer filesize;

    @NotNull
    @Size(max = 255)
    @Column(name = "source", length = 255, nullable = false)
    private String source;

    @NotNull
    @Size(max = 255)
    @Column(name = "preview", length = 255, nullable = false)
    private String preview;

    @Size(max = 255)
    @Column(name = "focalpoint", length = 255)
    private String focalpoint;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asset id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedat() {
        return this.createdat;
    }

    public Asset createdat(Instant createdat) {
        this.createdat = createdat;
        return this;
    }

    public void setCreatedat(Instant createdat) {
        this.createdat = createdat;
    }

    public Instant getUpdatedat() {
        return this.updatedat;
    }

    public Asset updatedat(Instant updatedat) {
        this.updatedat = updatedat;
        return this;
    }

    public void setUpdatedat(Instant updatedat) {
        this.updatedat = updatedat;
    }

    public String getName() {
        return this.name;
    }

    public Asset name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public Asset type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMimetype() {
        return this.mimetype;
    }

    public Asset mimetype(String mimetype) {
        this.mimetype = mimetype;
        return this;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Asset width(Integer width) {
        this.width = width;
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Asset height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getFilesize() {
        return this.filesize;
    }

    public Asset filesize(Integer filesize) {
        this.filesize = filesize;
        return this;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public String getSource() {
        return this.source;
    }

    public Asset source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPreview() {
        return this.preview;
    }

    public Asset preview(String preview) {
        this.preview = preview;
        return this;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getFocalpoint() {
        return this.focalpoint;
    }

    public Asset focalpoint(String focalpoint) {
        this.focalpoint = focalpoint;
        return this;
    }

    public void setFocalpoint(String focalpoint) {
        this.focalpoint = focalpoint;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asset)) {
            return false;
        }
        return id != null && id.equals(((Asset) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Asset{" +
            "id=" + getId() +
            ", createdat='" + getCreatedat() + "'" +
            ", updatedat='" + getUpdatedat() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", mimetype='" + getMimetype() + "'" +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", filesize=" + getFilesize() +
            ", source='" + getSource() + "'" +
            ", preview='" + getPreview() + "'" +
            ", focalpoint='" + getFocalpoint() + "'" +
            "}";
    }
}
