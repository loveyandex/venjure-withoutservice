import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { IChannel } from 'app/shared/model/channel.model';
import { getEntities as getChannels } from 'app/entities/channel/channel.reducer';
import { IPromotion } from 'app/shared/model/promotion.model';
import { getEntities as getPromotions } from 'app/entities/promotion/promotion.reducer';
import { getEntity, updateEntity, createEntity, reset } from './jorder.reducer';
import { IJorder } from 'app/shared/model/jorder.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IJorderUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const JorderUpdate = (props: IJorderUpdateProps) => {
  const [idschannel, setIdschannel] = useState([]);
  const [idspromotion, setIdspromotion] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { jorderEntity, customers, channels, promotions, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/jorder' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCustomers();
    props.getChannels();
    props.getPromotions();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdat = convertDateTimeToServer(values.createdat);
    values.updatedat = convertDateTimeToServer(values.updatedat);
    values.orderplacedat = convertDateTimeToServer(values.orderplacedat);

    if (errors.length === 0) {
      const entity = {
        ...jorderEntity,
        ...values,
        channels: mapIdList(values.channels),
        promotions: mapIdList(values.promotions),
        customer: customers.find(it => it.id.toString() === values.customerId.toString()),
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
          <h2 id="venjureApp.jorder.home.createOrEditLabel" data-cy="JorderCreateUpdateHeading">
            <Translate contentKey="venjureApp.jorder.home.createOrEditLabel">Create or edit a Jorder</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : jorderEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="jorder-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="jorder-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="jorder-createdat">
                  <Translate contentKey="venjureApp.jorder.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="jorder-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.jorderEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="jorder-updatedat">
                  <Translate contentKey="venjureApp.jorder.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="jorder-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.jorderEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="codeLabel" for="jorder-code">
                  <Translate contentKey="venjureApp.jorder.code">Code</Translate>
                </Label>
                <AvField
                  id="jorder-code"
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
                <Label id="stateLabel" for="jorder-state">
                  <Translate contentKey="venjureApp.jorder.state">State</Translate>
                </Label>
                <AvField
                  id="jorder-state"
                  data-cy="state"
                  type="text"
                  name="state"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="activeLabel">
                  <AvInput id="jorder-active" data-cy="active" type="checkbox" className="form-check-input" name="active" />
                  <Translate contentKey="venjureApp.jorder.active">Active</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="orderplacedatLabel" for="jorder-orderplacedat">
                  <Translate contentKey="venjureApp.jorder.orderplacedat">Orderplacedat</Translate>
                </Label>
                <AvInput
                  id="jorder-orderplacedat"
                  data-cy="orderplacedat"
                  type="datetime-local"
                  className="form-control"
                  name="orderplacedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.jorderEntity.orderplacedat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="couponcodesLabel" for="jorder-couponcodes">
                  <Translate contentKey="venjureApp.jorder.couponcodes">Couponcodes</Translate>
                </Label>
                <AvField
                  id="jorder-couponcodes"
                  data-cy="couponcodes"
                  type="text"
                  name="couponcodes"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="shippingaddressLabel" for="jorder-shippingaddress">
                  <Translate contentKey="venjureApp.jorder.shippingaddress">Shippingaddress</Translate>
                </Label>
                <AvField
                  id="jorder-shippingaddress"
                  data-cy="shippingaddress"
                  type="text"
                  name="shippingaddress"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="billingaddressLabel" for="jorder-billingaddress">
                  <Translate contentKey="venjureApp.jorder.billingaddress">Billingaddress</Translate>
                </Label>
                <AvField
                  id="jorder-billingaddress"
                  data-cy="billingaddress"
                  type="text"
                  name="billingaddress"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="currencycodeLabel" for="jorder-currencycode">
                  <Translate contentKey="venjureApp.jorder.currencycode">Currencycode</Translate>
                </Label>
                <AvField
                  id="jorder-currencycode"
                  data-cy="currencycode"
                  type="text"
                  name="currencycode"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="subtotalLabel" for="jorder-subtotal">
                  <Translate contentKey="venjureApp.jorder.subtotal">Subtotal</Translate>
                </Label>
                <AvField
                  id="jorder-subtotal"
                  data-cy="subtotal"
                  type="string"
                  className="form-control"
                  name="subtotal"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="subtotalwithtaxLabel" for="jorder-subtotalwithtax">
                  <Translate contentKey="venjureApp.jorder.subtotalwithtax">Subtotalwithtax</Translate>
                </Label>
                <AvField
                  id="jorder-subtotalwithtax"
                  data-cy="subtotalwithtax"
                  type="string"
                  className="form-control"
                  name="subtotalwithtax"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="shippingLabel" for="jorder-shipping">
                  <Translate contentKey="venjureApp.jorder.shipping">Shipping</Translate>
                </Label>
                <AvField
                  id="jorder-shipping"
                  data-cy="shipping"
                  type="string"
                  className="form-control"
                  name="shipping"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="shippingwithtaxLabel" for="jorder-shippingwithtax">
                  <Translate contentKey="venjureApp.jorder.shippingwithtax">Shippingwithtax</Translate>
                </Label>
                <AvField
                  id="jorder-shippingwithtax"
                  data-cy="shippingwithtax"
                  type="string"
                  className="form-control"
                  name="shippingwithtax"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="taxzoneidLabel" for="jorder-taxzoneid">
                  <Translate contentKey="venjureApp.jorder.taxzoneid">Taxzoneid</Translate>
                </Label>
                <AvField id="jorder-taxzoneid" data-cy="taxzoneid" type="string" className="form-control" name="taxzoneid" />
              </AvGroup>
              <AvGroup>
                <Label for="jorder-customer">
                  <Translate contentKey="venjureApp.jorder.customer">Customer</Translate>
                </Label>
                <AvInput id="jorder-customer" data-cy="customer" type="select" className="form-control" name="customerId">
                  <option value="" key="0" />
                  {customers
                    ? customers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="jorder-channel">
                  <Translate contentKey="venjureApp.jorder.channel">Channel</Translate>
                </Label>
                <AvInput
                  id="jorder-channel"
                  data-cy="channel"
                  type="select"
                  multiple
                  className="form-control"
                  name="channels"
                  value={!isNew && jorderEntity.channels && jorderEntity.channels.map(e => e.id)}
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
                <Label for="jorder-promotion">
                  <Translate contentKey="venjureApp.jorder.promotion">Promotion</Translate>
                </Label>
                <AvInput
                  id="jorder-promotion"
                  data-cy="promotion"
                  type="select"
                  multiple
                  className="form-control"
                  name="promotions"
                  value={!isNew && jorderEntity.promotions && jorderEntity.promotions.map(e => e.id)}
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
              <Button tag={Link} id="cancel-save" to="/jorder" replace color="info">
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
  customers: storeState.customer.entities,
  channels: storeState.channel.entities,
  promotions: storeState.promotion.entities,
  jorderEntity: storeState.jorder.entity,
  loading: storeState.jorder.loading,
  updating: storeState.jorder.updating,
  updateSuccess: storeState.jorder.updateSuccess,
});

const mapDispatchToProps = {
  getCustomers,
  getChannels,
  getPromotions,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(JorderUpdate);
