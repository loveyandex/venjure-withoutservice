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
 * Criteria class for the {@link com.venjure.domain.Surcharge} entity. This class is used
 * in {@link com.venjure.web.rest.SurchargeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /surcharges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SurchargeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private StringFilter description;

    private IntegerFilter listprice;

    private BooleanFilter listpriceincludestax;

    private StringFilter sku;

    private StringFilter taxlines;

    private LongFilter jorderId;

    private LongFilter ordermodificationId;

    public SurchargeCriteria() {}

    public SurchargeCriteria(SurchargeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.listprice = other.listprice == null ? null : other.listprice.copy();
        this.listpriceincludestax = other.listpriceincludestax == null ? null : other.listpriceincludestax.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.taxlines = other.taxlines == null ? null : other.taxlines.copy();
        this.jorderId = other.jorderId == null ? null : other.jorderId.copy();
        this.ordermodificationId = other.ordermodificationId == null ? null : other.ordermodificationId.copy();
    }

    @Override
    public SurchargeCriteria copy() {
        return new SurchargeCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getListprice() {
        return listprice;
    }

    public IntegerFilter listprice() {
        if (listprice == null) {
            listprice = new IntegerFilter();
        }
        return listprice;
    }

    public void setListprice(IntegerFilter listprice) {
        this.listprice = listprice;
    }

    public BooleanFilter getListpriceincludestax() {
        return listpriceincludestax;
    }

    public BooleanFilter listpriceincludestax() {
        if (listpriceincludestax == null) {
            listpriceincludestax = new BooleanFilter();
        }
        return listpriceincludestax;
    }

    public void setListpriceincludestax(BooleanFilter listpriceincludestax) {
        this.listpriceincludestax = listpriceincludestax;
    }

    public StringFilter getSku() {
        return sku;
    }

    public StringFilter sku() {
        if (sku == null) {
            sku = new StringFilter();
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public StringFilter getTaxlines() {
        return taxlines;
    }

    public StringFilter taxlines() {
        if (taxlines == null) {
            taxlines = new StringFilter();
        }
        return taxlines;
    }

    public void setTaxlines(StringFilter taxlines) {
        this.taxlines = taxlines;
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

    public LongFilter getOrdermodificationId() {
        return ordermodificationId;
    }

    public LongFilter ordermodificationId() {
        if (ordermodificationId == null) {
            ordermodificationId = new LongFilter();
        }
        return ordermodificationId;
    }

    public void setOrdermodificationId(LongFilter ordermodificationId) {
        this.ordermodificationId = ordermodificationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SurchargeCriteria that = (SurchargeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(description, that.description) &&
            Objects.equals(listprice, that.listprice) &&
            Objects.equals(listpriceincludestax, that.listpriceincludestax) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(taxlines, that.taxlines) &&
            Objects.equals(jorderId, that.jorderId) &&
            Objects.equals(ordermodificationId, that.ordermodificationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            createdat,
            updatedat,
            description,
            listprice,
            listpriceincludestax,
            sku,
            taxlines,
            jorderId,
            ordermodificationId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SurchargeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (listprice != null ? "listprice=" + listprice + ", " : "") +
            (listpriceincludestax != null ? "listpriceincludestax=" + listpriceincludestax + ", " : "") +
            (sku != null ? "sku=" + sku + ", " : "") +
            (taxlines != null ? "taxlines=" + taxlines + ", " : "") +
            (jorderId != null ? "jorderId=" + jorderId + ", " : "") +
            (ordermodificationId != null ? "ordermodificationId=" + ordermodificationId + ", " : "") +
            "}";
    }
}
