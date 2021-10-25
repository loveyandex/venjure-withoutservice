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
import { getEntity, updateEntity, createEntity, reset } from './refund.reducer';
import { IRefund } from 'app/shared/model/refund.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRefundUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RefundUpdate = (props: IRefundUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { refundEntity, payments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/refund' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getPayments();
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
        ...refundEntity,
        ...values,
        payment: payments.find(it => it.id.toString() === values.paymentId.toString()),
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
          <h2 id="venjureApp.refund.home.createOrEditLabel" data-cy="RefundCreateUpdateHeading">
            <Translate contentKey="venjureApp.refund.home.createOrEditLabel">Create or edit a Refund</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : refundEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="refund-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="refund-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="refund-createdat">
                  <Translate contentKey="venjureApp.refund.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="refund-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.refundEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="refund-updatedat">
                  <Translate contentKey="venjureApp.refund.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="refund-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.refundEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="itemsLabel" for="refund-items">
                  <Translate contentKey="venjureApp.refund.items">Items</Translate>
                </Label>
                <AvField
                  id="refund-items"
                  data-cy="items"
                  type="string"
                  className="form-control"
                  name="items"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="shippingLabel" for="refund-shipping">
                  <Translate contentKey="venjureApp.refund.shipping">Shipping</Translate>
                </Label>
                <AvField
                  id="refund-shipping"
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
                <Label id="adjustmentLabel" for="refund-adjustment">
                  <Translate contentKey="venjureApp.refund.adjustment">Adjustment</Translate>
                </Label>
                <AvField
                  id="refund-adjustment"
                  data-cy="adjustment"
                  type="string"
                  className="form-control"
                  name="adjustment"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="totalLabel" for="refund-total">
                  <Translate contentKey="venjureApp.refund.total">Total</Translate>
                </Label>
                <AvField
                  id="refund-total"
                  data-cy="total"
                  type="string"
                  className="form-control"
                  name="total"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="methodLabel" for="refund-method">
                  <Translate contentKey="venjureApp.refund.method">Method</Translate>
                </Label>
                <AvField
                  id="refund-method"
                  data-cy="method"
                  type="text"
                  name="method"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="reasonLabel" for="refund-reason">
                  <Translate contentKey="venjureApp.refund.reason">Reason</Translate>
                </Label>
                <AvField
                  id="refund-reason"
                  data-cy="reason"
                  type="text"
                  name="reason"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="stateLabel" for="refund-state">
                  <Translate contentKey="venjureApp.refund.state">State</Translate>
                </Label>
                <AvField
                  id="refund-state"
                  data-cy="state"
                  type="text"
                  name="state"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="transactionidLabel" for="refund-transactionid">
                  <Translate contentKey="venjureApp.refund.transactionid">Transactionid</Translate>
                </Label>
                <AvField
                  id="refund-transactionid"
                  data-cy="transactionid"
                  type="text"
                  name="transactionid"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="metadataLabel" for="refund-metadata">
                  <Translate contentKey="venjureApp.refund.metadata">Metadata</Translate>
                </Label>
                <AvField
                  id="refund-metadata"
                  data-cy="metadata"
                  type="text"
                  name="metadata"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="refund-payment">
                  <Translate contentKey="venjureApp.refund.payment">Payment</Translate>
                </Label>
                <AvInput id="refund-payment" data-cy="payment" type="select" className="form-control" name="paymentId" required>
                  <option value="" key="0" />
                  {payments
                    ? payments.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/refund" replace color="info">
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
  refundEntity: storeState.refund.entity,
  loading: storeState.refund.loading,
  updating: storeState.refund.updating,
  updateSuccess: storeState.refund.updateSuccess,
});

const mapDispatchToProps = {
  getPayments,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RefundUpdate);
