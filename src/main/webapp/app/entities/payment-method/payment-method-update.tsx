import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IChannel } from 'app/shared/model/channel.model';
import { getEntities as getChannels } from 'app/entities/channel/channel.reducer';
import { getEntity, updateEntity, createEntity, reset } from './payment-method.reducer';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPaymentMethodUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PaymentMethodUpdate = (props: IPaymentMethodUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { paymentMethodEntity, channels, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/payment-method' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getChannels();
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
        ...paymentMethodEntity,
        ...values,
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
          <h2 id="venjureApp.paymentMethod.home.createOrEditLabel" data-cy="PaymentMethodCreateUpdateHeading">
            <Translate contentKey="venjureApp.paymentMethod.home.createOrEditLabel">Create or edit a PaymentMethod</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : paymentMethodEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="payment-method-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="payment-method-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="payment-method-createdat">
                  <Translate contentKey="venjureApp.paymentMethod.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="payment-method-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.paymentMethodEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="payment-method-updatedat">
                  <Translate contentKey="venjureApp.paymentMethod.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="payment-method-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.paymentMethodEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="payment-method-name">
                  <Translate contentKey="venjureApp.paymentMethod.name">Name</Translate>
                </Label>
                <AvField
                  id="payment-method-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="codeLabel" for="payment-method-code">
                  <Translate contentKey="venjureApp.paymentMethod.code">Code</Translate>
                </Label>
                <AvField
                  id="payment-method-code"
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
                <Label id="descriptionLabel" for="payment-method-description">
                  <Translate contentKey="venjureApp.paymentMethod.description">Description</Translate>
                </Label>
                <AvField
                  id="payment-method-description"
                  data-cy="description"
                  type="text"
                  name="description"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="enabledLabel">
                  <AvInput id="payment-method-enabled" data-cy="enabled" type="checkbox" className="form-check-input" name="enabled" />
                  <Translate contentKey="venjureApp.paymentMethod.enabled">Enabled</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="checkerLabel" for="payment-method-checker">
                  <Translate contentKey="venjureApp.paymentMethod.checker">Checker</Translate>
                </Label>
                <AvField
                  id="payment-method-checker"
                  data-cy="checker"
                  type="text"
                  name="checker"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="handlerLabel" for="payment-method-handler">
                  <Translate contentKey="venjureApp.paymentMethod.handler">Handler</Translate>
                </Label>
                <AvField
                  id="payment-method-handler"
                  data-cy="handler"
                  type="text"
                  name="handler"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/payment-method" replace color="info">
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
  channels: storeState.channel.entities,
  paymentMethodEntity: storeState.paymentMethod.entity,
  loading: storeState.paymentMethod.loading,
  updating: storeState.paymentMethod.updating,
  updateSuccess: storeState.paymentMethod.updateSuccess,
});

const mapDispatchToProps = {
  getChannels,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentMethodUpdate);
