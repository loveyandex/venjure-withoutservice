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
 * Criteria class for the {@link com.venjure.domain.ShippingLine} entity. This class is used
 * in {@link com.venjure.web.rest.ShippingLineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shipping-lines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShippingLineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createdat;

    private InstantFilter updatedat;

    private IntegerFilter listprice;

    private BooleanFilter listpriceincludestax;

    private StringFilter adjustments;

    private StringFilter taxlines;

    private LongFilter shippingmethodId;

    private LongFilter jorderId;

    public ShippingLineCriteria() {}

    public ShippingLineCriteria(ShippingLineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdat = other.createdat == null ? null : other.createdat.copy();
        this.updatedat = other.updatedat == null ? null : other.updatedat.copy();
        this.listprice = other.listprice == null ? null : other.listprice.copy();
        this.listpriceincludestax = other.listpriceincludestax == null ? null : other.listpriceincludestax.copy();
        this.adjustments = other.adjustments == null ? null : other.adjustments.copy();
        this.taxlines = other.taxlines == null ? null : other.taxlines.copy();
        this.shippingmethodId = other.shippingmethodId == null ? null : other.shippingmethodId.copy();
        this.jorderId = other.jorderId == null ? null : other.jorderId.copy();
    }

    @Override
    public ShippingLineCriteria copy() {
        return new ShippingLineCriteria(this);
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

    public StringFilter getAdjustments() {
        return adjustments;
    }

    public StringFilter adjustments() {
        if (adjustments == null) {
            adjustments = new StringFilter();
        }
        return adjustments;
    }

    public void setAdjustments(StringFilter adjustments) {
        this.adjustments = adjustments;
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

    public LongFilter getShippingmethodId() {
        return shippingmethodId;
    }

    public LongFilter shippingmethodId() {
        if (shippingmethodId == null) {
            shippingmethodId = new LongFilter();
        }
        return shippingmethodId;
    }

    public void setShippingmethodId(LongFilter shippingmethodId) {
        this.shippingmethodId = shippingmethodId;
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
        final ShippingLineCriteria that = (ShippingLineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdat, that.createdat) &&
            Objects.equals(updatedat, that.updatedat) &&
            Objects.equals(listprice, that.listprice) &&
            Objects.equals(listpriceincludestax, that.listpriceincludestax) &&
            Objects.equals(adjustments, that.adjustments) &&
            Objects.equals(taxlines, that.taxlines) &&
            Objects.equals(shippingmethodId, that.shippingmethodId) &&
            Objects.equals(jorderId, that.jorderId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdat, updatedat, listprice, listpriceincludestax, adjustments, taxlines, shippingmethodId, jorderId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingLineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (createdat != null ? "createdat=" + createdat + ", " : "") +
            (updatedat != null ? "updatedat=" + updatedat + ", " : "") +
            (listprice != null ? "listprice=" + listprice + ", " : "") +
            (listpriceincludestax != null ? "listpriceincludestax=" + listpriceincludestax + ", " : "") +
            (adjustments != null ? "adjustments=" + adjustments + ", " : "") +
            (taxlines != null ? "taxlines=" + taxlines + ", " : "") +
            (shippingmethodId != null ? "shippingmethodId=" + shippingmethodId + ", " : "") +
            (jorderId != null ? "jorderId=" + jorderId + ", " : "") +
            "}";
    }
}
