import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPayment } from 'app/shared/model/payment.model';
import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { IRefund } from 'app/shared/model/refund.model';
import { getEntities as getRefunds } from 'app/entities/refund/refund.reducer';
import { IJorder } from 'app/shared/model/jorder.model';
import { getEntities as getJorders } from 'app/entities/jorder/jorder.reducer';
import { IOrderItem } from 'app/shared/model/order-item.model';
import { getEntities as getOrderItems } from 'app/entities/order-item/order-item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './order-modification.reducer';
import { IOrderModification } from 'app/shared/model/order-modification.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrderModificationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderModificationUpdate = (props: IOrderModificationUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { orderModificationEntity, payments, refunds, jorders, orderItems, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/order-modification' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getPayments();
    props.getRefunds();
    props.getJorders();
    props.getOrderItems();
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
        ...orderModificationEntity,
        ...values,
        payment: payments.find(it => it.id.toString() === values.paymentId.toString()),
        refund: refunds.find(it => it.id.toString() === values.refundId.toString()),
        jorder: jorders.find(it => it.id.toString() === values.jorderId.toString()),
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
          <h2 id="venjureApp.orderModification.home.createOrEditLabel" data-cy="OrderModificationCreateUpdateHeading">
            <Translate contentKey="venjureApp.orderModification.home.createOrEditLabel">Create or edit a OrderModification</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : orderModificationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="order-modification-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="order-modification-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="order-modification-createdat">
                  <Translate contentKey="venjureApp.orderModification.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="order-modification-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.orderModificationEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="order-modification-updatedat">
                  <Translate contentKey="venjureApp.orderModification.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="order-modification-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.orderModificationEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="noteLabel" for="order-modification-note">
                  <Translate contentKey="venjureApp.orderModification.note">Note</Translate>
                </Label>
                <AvField
                  id="order-modification-note"
                  data-cy="note"
                  type="text"
                  name="note"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="pricechangeLabel" for="order-modification-pricechange">
                  <Translate contentKey="venjureApp.orderModification.pricechange">Pricechange</Translate>
                </Label>
                <AvField
                  id="order-modification-pricechange"
                  data-cy="pricechange"
                  type="string"
                  className="form-control"
                  name="pricechange"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="shippingaddresschangeLabel" for="order-modification-shippingaddresschange">
                  <Translate contentKey="venjureApp.orderModification.shippingaddresschange">Shippingaddresschange</Translate>
                </Label>
                <AvField
                  id="order-modification-shippingaddresschange"
                  data-cy="shippingaddresschange"
                  type="text"
                  name="shippingaddresschange"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="billingaddresschangeLabel" for="order-modification-billingaddresschange">
                  <Translate contentKey="venjureApp.orderModification.billingaddresschange">Billingaddresschange</Translate>
                </Label>
                <AvField
                  id="order-modification-billingaddresschange"
                  data-cy="billingaddresschange"
                  type="text"
                  name="billingaddresschange"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="order-modification-payment">
                  <Translate contentKey="venjureApp.orderModification.payment">Payment</Translate>
                </Label>
                <AvInput id="order-modification-payment" data-cy="payment" type="select" className="form-control" name="paymentId">
                  <option value="" key="0" />
                  {payments
                    ? payments.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="order-modification-refund">
                  <Translate contentKey="venjureApp.orderModification.refund">Refund</Translate>
                </Label>
                <AvInput id="order-modification-refund" data-cy="refund" type="select" className="form-control" name="refundId">
                  <option value="" key="0" />
                  {refunds
                    ? refunds.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="order-modification-jorder">
                  <Translate contentKey="venjureApp.orderModification.jorder">Jorder</Translate>
                </Label>
                <AvInput id="order-modification-jorder" data-cy="jorder" type="select" className="form-control" name="jorderId">
                  <option value="" key="0" />
                  {jorders
                    ? jorders.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/order-modification" replace color="info">
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
  payments: storeState.payment.entities,
  refunds: storeState.refund.entities,
  jorders: storeState.jorder.entities,
  orderItems: storeState.orderItem.entities,
  orderModificationEntity: storeState.orderModification.entity,
  loading: storeState.orderModification.loading,
  updating: storeState.orderModification.updating,
  updateSuccess: storeState.orderModification.updateSuccess,
});

const mapDispatchToProps = {
  getPayments,
  getRefunds,
  getJorders,
  getOrderItems,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderModificationUpdate);
