import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IJorder } from 'app/shared/model/jorder.model';
import { getEntities as getJorders } from 'app/entities/jorder/jorder.reducer';
import { getEntity, updateEntity, createEntity, reset } from './payment.reducer';
import { IPayment } from 'app/shared/model/payment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPaymentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PaymentUpdate = (props: IPaymentUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { paymentEntity, jorders, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/payment' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getJorders();
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
        ...paymentEntity,
        ...values,
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
          <h2 id="venjureApp.payment.home.createOrEditLabel" data-cy="PaymentCreateUpdateHeading">
            <Translate contentKey="venjureApp.payment.home.createOrEditLabel">Create or edit a Payment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : paymentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="payment-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="payment-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="payment-createdat">
                  <Translate contentKey="venjureApp.payment.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="payment-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.paymentEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="payment-updatedat">
                  <Translate contentKey="venjureApp.payment.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="payment-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.paymentEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="methodLabel" for="payment-method">
                  <Translate contentKey="venjureApp.payment.method">Method</Translate>
                </Label>
                <AvField
                  id="payment-method"
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
                <Label id="amountLabel" for="payment-amount">
                  <Translate contentKey="venjureApp.payment.amount">Amount</Translate>
                </Label>
                <AvField
                  id="payment-amount"
                  data-cy="amount"
                  type="string"
                  className="form-control"
                  name="amount"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="stateLabel" for="payment-state">
                  <Translate contentKey="venjureApp.payment.state">State</Translate>
                </Label>
                <AvField
                  id="payment-state"
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
                <Label id="errormessageLabel" for="payment-errormessage">
                  <Translate contentKey="venjureApp.payment.errormessage">Errormessage</Translate>
                </Label>
                <AvField
                  id="payment-errormessage"
                  data-cy="errormessage"
                  type="text"
                  name="errormessage"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="transactionidLabel" for="payment-transactionid">
                  <Translate contentKey="venjureApp.payment.transactionid">Transactionid</Translate>
                </Label>
                <AvField
                  id="payment-transactionid"
                  data-cy="transactionid"
                  type="text"
                  name="transactionid"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="metadataLabel" for="payment-metadata">
                  <Translate contentKey="venjureApp.payment.metadata">Metadata</Translate>
                </Label>
                <AvField
                  id="payment-metadata"
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
                <Label for="payment-jorder">
                  <Translate contentKey="venjureApp.payment.jorder">Jorder</Translate>
                </Label>
                <AvInput id="payment-jorder" data-cy="jorder" type="select" className="form-control" name="jorderId">
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
              <Button tag={Link} id="cancel-save" to="/payment" replace color="info">
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
  jorders: storeState.jorder.entities,
  paymentEntity: storeState.payment.entity,
  loading: storeState.payment.loading,
  updating: storeState.payment.updating,
  updateSuccess: storeState.payment.updateSuccess,
});

const mapDispatchToProps = {
  getJorders,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentUpdate);
