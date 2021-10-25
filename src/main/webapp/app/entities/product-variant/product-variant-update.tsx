import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IAsset } from 'app/shared/model/asset.model';
import { getEntities as getAssets } from 'app/entities/asset/asset.reducer';
import { ITaxCategory } from 'app/shared/model/tax-category.model';
import { getEntities as getTaxCategories } from 'app/entities/tax-category/tax-category.reducer';
import { IChannel } from 'app/shared/model/channel.model';
import { getEntities as getChannels } from 'app/entities/channel/channel.reducer';
import { ICollection } from 'app/shared/model/collection.model';
import { getEntities as getCollections } from 'app/entities/collection/collection.reducer';
import { IFacetValue } from 'app/shared/model/facet-value.model';
import { getEntities as getFacetValues } from 'app/entities/facet-value/facet-value.reducer';
import { IProductOption } from 'app/shared/model/product-option.model';
import { getEntities as getProductOptions } from 'app/entities/product-option/product-option.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product-variant.reducer';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductVariantUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductVariantUpdate = (props: IProductVariantUpdateProps) => {
  const [idschannel, setIdschannel] = useState([]);
  const [idsproductVariants, setIdsproductVariants] = useState([]);
  const [idsfacetValue, setIdsfacetValue] = useState([]);
  const [idsproductOption, setIdsproductOption] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const {
    productVariantEntity,
    products,
    assets,
    taxCategories,
    channels,
    collections,
    facetValues,
    productOptions,
    loading,
    updating,
  } = props;

  const handleClose = () => {
    props.history.push('/product-variant' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getProducts();
    props.getAssets();
    props.getTaxCategories();
    props.getChannels();
    props.getCollections();
    props.getFacetValues();
    props.getProductOptions();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdat = convertDateTimeToServer(values.createdat);
    values.updatedat = convertDateTimeToServer(values.updatedat);
    values.deletedat = convertDateTimeToServer(values.deletedat);

    if (errors.length === 0) {
      const entity = {
        ...productVariantEntity,
        ...values,
        channels: mapIdList(values.channels),
        productVariants: mapIdList(values.productVariants),
        facetValues: mapIdList(values.facetValues),
        productOptions: mapIdList(values.productOptions),
        product: products.find(it => it.id.toString() === values.productId.toString()),
        featuredasset: assets.find(it => it.id.toString() === values.featuredassetId.toString()),
        taxcategory: taxCategories.find(it => it.id.toString() === values.taxcategoryId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="venjureApp.productVariant.home.createOrEditLabel" data-cy="ProductVariantCreateUpdateHeading">
            <Translate contentKey="venjureApp.productVariant.home.createOrEditLabel">Create or edit a ProductVariant</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productVariantEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-variant-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="product-variant-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="product-variant-createdat">
                  <Translate contentKey="venjureApp.productVariant.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="product-variant-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productVariantEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="product-variant-updatedat">
                  <Translate contentKey="venjureApp.productVariant.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="product-variant-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productVariantEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="deletedatLabel" for="product-variant-deletedat">
                  <Translate contentKey="venjureApp.productVariant.deletedat">Deletedat</Translate>
                </Label>
                <AvInput
                  id="product-variant-deletedat"
                  data-cy="deletedat"
                  type="datetime-local"
                  className="form-control"
                  name="deletedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productVariantEntity.deletedat)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="enabledLabel">
                  <AvInput id="product-variant-enabled" data-cy="enabled" type="checkbox" className="form-check-input" name="enabled" />
                  <Translate contentKey="venjureApp.productVariant.enabled">Enabled</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="skuLabel" for="product-variant-sku">
                  <Translate contentKey="venjureApp.productVariant.sku">Sku</Translate>
                </Label>
                <AvField
                  id="product-variant-sku"
                  data-cy="sku"
                  type="text"
                  name="sku"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="stockonhandLabel" for="product-variant-stockonhand">
                  <Translate contentKey="venjureApp.productVariant.stockonhand">Stockonhand</Translate>
                </Label>
                <AvField
                  id="product-variant-stockonhand"
                  data-cy="stockonhand"
                  type="string"
                  className="form-control"
                  name="stockonhand"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="stockallocatedLabel" for="product-variant-stockallocated">
                  <Translate contentKey="venjureApp.productVariant.stockallocated">Stockallocated</Translate>
                </Label>
                <AvField
                  id="product-variant-stockallocated"
                  data-cy="stockallocated"
                  type="string"
                  className="form-control"
                  name="stockallocated"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="outofstockthresholdLabel" for="product-variant-outofstockthreshold">
                  <Translate contentKey="venjureApp.productVariant.outofstockthreshold">Outofstockthreshold</Translate>
                </Label>
                <AvField
                  id="product-variant-outofstockthreshold"
                  data-cy="outofstockthreshold"
                  type="string"
                  className="form-control"
                  name="outofstockthreshold"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="useglobaloutofstockthresholdLabel">
                  <AvInput
                    id="product-variant-useglobaloutofstockthreshold"
                    data-cy="useglobaloutofstockthreshold"
                    type="checkbox"
                    className="form-check-input"
                    name="useglobaloutofstockthreshold"
                  />
                  <Translate contentKey="venjureApp.productVariant.useglobaloutofstockthreshold">Useglobaloutofstockthreshold</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="trackinventoryLabel" for="product-variant-trackinventory">
                  <Translate contentKey="venjureApp.productVariant.trackinventory">Trackinventory</Translate>
                </Label>
                <AvField
                  id="product-variant-trackinventory"
                  data-cy="trackinventory"
                  type="text"
                  name="trackinventory"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="product-variant-product">
                  <Translate contentKey="venjureApp.productVariant.product">Product</Translate>
                </Label>
                <AvInput id="product-variant-product" data-cy="product" type="select" className="form-control" name="productId">
                  <option value="" key="0" />
                  {products
                    ? products.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="product-variant-featuredasset">
                  <Translate contentKey="venjureApp.productVariant.featuredasset">Featuredasset</Translate>
                </Label>
                <AvInput
                  id="product-variant-featuredasset"
                  data-cy="featuredasset"
                  type="select"
                  className="form-control"
                  name="featuredassetId"
                >
                  <option value="" key="0" />
                  {assets
                    ? assets.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="product-variant-taxcategory">
                  <Translate contentKey="venjureApp.productVariant.taxcategory">Taxcategory</Translate>
                </Label>
                <AvInput id="product-variant-taxcategory" data-cy="taxcategory" type="select" className="form-control" name="taxcategoryId">
                  <option value="" key="0" />
                  {taxCategories
                    ? taxCategories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="product-variant-channel">
                  <Translate contentKey="venjureApp.productVariant.channel">Channel</Translate>
                </Label>
                <AvInput
                  id="product-variant-channel"
                  data-cy="channel"
                  type="select"
                  multiple
                  className="form-control"
                  name="channels"
                  value={!isNew && productVariantEntity.channels && productVariantEntity.channels.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {channels
                    ? channels.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="product-variant-productVariants">
                  <Translate contentKey="venjureApp.productVariant.productVariants">Product Variants</Translate>
                </Label>
                <AvInput
                  id="product-variant-productVariants"
                  data-cy="productVariants"
                  type="select"
                  multiple
                  className="form-control"
                  name="productVariants"
                  value={!isNew && productVariantEntity.productVariants && productVariantEntity.productVariants.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {collections
                    ? collections.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="product-variant-facetValue">
                  <Translate contentKey="venjureApp.productVariant.facetValue">Facet Value</Translate>
                </Label>
                <AvInput
                  id="product-variant-facetValue"
                  data-cy="facetValue"
                  type="select"
                  multiple
                  className="form-control"
                  name="facetValues"
                  value={!isNew && productVariantEntity.facetValues && productVariantEntity.facetValues.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {facetValues
                    ? facetValues.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="product-variant-productOption">
                  <Translate contentKey="venjureApp.productVariant.productOption">Product Option</Translate>
                </Label>
                <AvInput
                  id="product-variant-productOption"
                  data-cy="productOption"
                  type="select"
                  multiple
                  className="form-control"
                  name="productOptions"
                  value={!isNew && productVariantEntity.productOptions && productVariantEntity.productOptions.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {productOptions
                    ? productOptions.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/product-variant" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  products: storeState.product.entities,
  assets: storeState.asset.entities,
  taxCategories: storeState.taxCategory.entities,
  channels: storeState.channel.entities,
  collections: storeState.collection.entities,
  facetValues: storeState.facetValue.entities,
  productOptions: storeState.productOption.entities,
  productVariantEntity: storeState.productVariant.entity,
  loading: storeState.productVariant.loading,
  updating: storeState.productVariant.updating,
  updateSuccess: storeState.productVariant.updateSuccess,
});

const mapDispatchToProps = {
  getProducts,
  getAssets,
  getTaxCategories,
  getChannels,
  getCollections,
  getFacetValues,
  getProductOptions,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantUpdate);
