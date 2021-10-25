import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IZone } from 'app/shared/model/zone.model';
import { getEntities as getZones } from 'app/entities/zone/zone.reducer';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { getEntities as getPaymentMethods } from 'app/entities/payment-method/payment-method.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IPromotion } from 'app/shared/model/promotion.model';
import { getEntities as getPromotions } from 'app/entities/promotion/promotion.reducer';
import { IShippingMethod } from 'app/shared/model/shipping-method.model';
import { getEntities as getShippingMethods } from 'app/entities/shipping-method/shipping-method.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { IFacet } from 'app/shared/model/facet.model';
import { getEntities as getFacets } from 'app/entities/facet/facet.reducer';
import { IFacetValue } from 'app/shared/model/facet-value.model';
import { getEntities as getFacetValues } from 'app/entities/facet-value/facet-value.reducer';
import { IJorder } from 'app/shared/model/jorder.model';
import { getEntities as getJorders } from 'app/entities/jorder/jorder.reducer';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { getEntities as getProductVariants } from 'app/entities/product-variant/product-variant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './channel.reducer';
import { IChannel } from 'app/shared/model/channel.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IChannelUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ChannelUpdate = (props: IChannelUpdateProps) => {
  const [idspaymentMethod, setIdspaymentMethod] = useState([]);
  const [idsproduct, setIdsproduct] = useState([]);
  const [idspromotion, setIdspromotion] = useState([]);
  const [idsshippingMethod, setIdsshippingMethod] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const {
    channelEntity,
    zones,
    paymentMethods,
    products,
    promotions,
    shippingMethods,
    customers,
    facets,
    facetValues,
    jorders,
    productVariants,
    loading,
    updating,
  } = props;

  const handleClose = () => {
    props.history.push('/channel' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getZones();
    props.getPaymentMethods();
    props.getProducts();
    props.getPromotions();
    props.getShippingMethods();
    props.getCustomers();
    props.getFacets();
    props.getFacetValues();
    props.getJorders();
    props.getProductVariants();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdat = convertDateTimeToServer(values.createdat);
    values.updatedat = convertDateTimeToServer(values.updatedat);

    if (errors.length === 0) {
      const entity = {
        ...channelEntity,
        ...values,
        paymentMethods: mapIdList(values.paymentMethods),
        products: mapIdList(values.products),
        promotions: mapIdList(values.promotions),
        shippingMethods: mapIdList(values.shippingMethods),
        defaulttaxzone: zones.find(it => it.id.toString() === values.defaulttaxzoneId.toString()),
        defaultshippingzone: zones.find(it => it.id.toString() === values.defaultshippingzoneId.toString()),
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
          <h2 id="venjureApp.channel.home.createOrEditLabel" data-cy="ChannelCreateUpdateHeading">
            <Translate contentKey="venjureApp.channel.home.createOrEditLabel">Create or edit a Channel</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : channelEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="channel-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="channel-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="channel-createdat">
                  <Translate contentKey="venjureApp.channel.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="channel-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.channelEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="channel-updatedat">
                  <Translate contentKey="venjureApp.channel.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="channel-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.channelEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="codeLabel" for="channel-code">
                  <Translate contentKey="venjureApp.channel.code">Code</Translate>
                </Label>
                <AvField
                  id="channel-code"
                  data-cy="code"
                  type="text"
                  name="code"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="tokenLabel" for="channel-token">
                  <Translate contentKey="venjureApp.channel.token">Token</Translate>
                </Label>
                <AvField
                  id="channel-token"
                  data-cy="token"
                  type="text"
                  name="token"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="defaultlanguagecodeLabel" for="channel-defaultlanguagecode">
                  <Translate contentKey="venjureApp.channel.defaultlanguagecode">Defaultlanguagecode</Translate>
                </Label>
                <AvField
                  id="channel-defaultlanguagecode"
                  data-cy="defaultlanguagecode"
                  type="text"
                  name="defaultlanguagecode"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="currencycodeLabel" for="channel-currencycode">
                  <Translate contentKey="venjureApp.channel.currencycode">Currencycode</Translate>
                </Label>
                <AvField
                  id="channel-currencycode"
                  data-cy="currencycode"
                  type="text"
                  name="currencycode"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="pricesincludetaxLabel">
                  <AvInput
                    id="channel-pricesincludetax"
                    data-cy="pricesincludetax"
                    type="checkbox"
                    className="form-check-input"
                    name="pricesincludetax"
                  />
                  <Translate contentKey="venjureApp.channel.pricesincludetax">Pricesincludetax</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="channel-defaulttaxzone">
                  <Translate contentKey="venjureApp.channel.defaulttaxzone">Defaulttaxzone</Translate>
                </Label>
                <AvInput
                  id="channel-defaulttaxzone"
                  data-cy="defaulttaxzone"
                  type="select"
                  className="form-control"
                  name="defaulttaxzoneId"
                >
                  <option value="" key="0" />
                  {zones
                    ? zones.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="channel-defaultshippingzone">
                  <Translate contentKey="venjureApp.channel.defaultshippingzone">Defaultshippingzone</Translate>
                </Label>
                <AvInput
                  id="channel-defaultshippingzone"
                  data-cy="defaultshippingzone"
                  type="select"
                  className="form-control"
                  name="defaultshippingzoneId"
                >
                  <option value="" key="0" />
                  {zones
                    ? zones.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="channel-paymentMethod">
                  <Translate contentKey="venjureApp.channel.paymentMethod">Payment Method</Translate>
                </Label>
                <AvInput
                  id="channel-paymentMethod"
                  data-cy="paymentMethod"
                  type="select"
                  multiple
                  className="form-control"
                  name="paymentMethods"
                  value={!isNew && channelEntity.paymentMethods && channelEntity.paymentMethods.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {paymentMethods
                    ? paymentMethods.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="channel-product">
                  <Translate contentKey="venjureApp.channel.product">Product</Translate>
                </Label>
                <AvInput
                  id="channel-product"
                  data-cy="product"
                  type="select"
                  multiple
                  className="form-control"
                  name="products"
                  value={!isNew && channelEntity.products && channelEntity.products.map(e => e.id)}
                >
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
                <Label for="channel-promotion">
                  <Translate contentKey="venjureApp.channel.promotion">Promotion</Translate>
                </Label>
                <AvInput
                  id="channel-promotion"
                  data-cy="promotion"
                  type="select"
                  multiple
                  className="form-control"
                  name="promotions"
                  value={!isNew && channelEntity.promotions && channelEntity.promotions.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {promotions
                    ? promotions.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="channel-shippingMethod">
                  <Translate contentKey="venjureApp.channel.shippingMethod">Shipping Method</Translate>
                </Label>
                <AvInput
                  id="channel-shippingMethod"
                  data-cy="shippingMethod"
                  type="select"
                  multiple
                  className="form-control"
                  name="shippingMethods"
                  value={!isNew && channelEntity.shippingMethods && channelEntity.shippingMethods.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {shippingMethods
                    ? shippingMethods.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/channel" replace color="info">
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
  zones: storeState.zone.entities,
  paymentMethods: storeState.paymentMethod.entities,
  products: storeState.product.entities,
  promotions: storeState.promotion.entities,
  shippingMethods: storeState.shippingMethod.entities,
  customers: storeState.customer.entities,
  facets: storeState.facet.entities,
  facetValues: storeState.facetValue.entities,
  jorders: storeState.jorder.entities,
  productVariants: storeState.productVariant.entities,
  channelEntity: storeState.channel.entity,
  loading: storeState.channel.loading,
  updating: storeState.channel.updating,
  updateSuccess: storeState.channel.updateSuccess,
});

const mapDispatchToProps = {
  getZones,
  getPaymentMethods,
  getProducts,
  getPromotions,
  getShippingMethods,
  getCustomers,
  getFacets,
  getFacetValues,
  getJorders,
  getProductVariants,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ChannelUpdate);
