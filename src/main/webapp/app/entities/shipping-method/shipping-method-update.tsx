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
import { getEntity, updateEntity, createEntity, reset } from './shipping-method.reducer';
import { IShippingMethod } from 'app/shared/model/shipping-method.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShippingMethodUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShippingMethodUpdate = (props: IShippingMethodUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { shippingMethodEntity, channels, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/shipping-method' + props.location.search);
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
    values.deletedat = convertDateTimeToServer(values.deletedat);

    if (errors.length === 0) {
      const entity = {
        ...shippingMethodEntity,
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
          <h2 id="venjureApp.shippingMethod.home.createOrEditLabel" data-cy="ShippingMethodCreateUpdateHeading">
            <Translate contentKey="venjureApp.shippingMethod.home.createOrEditLabel">Create or edit a ShippingMethod</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : shippingMethodEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="shipping-method-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="shipping-method-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="shipping-method-createdat">
                  <Translate contentKey="venjureApp.shippingMethod.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="shipping-method-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.shippingMethodEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="shipping-method-updatedat">
                  <Translate contentKey="venjureApp.shippingMethod.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="shipping-method-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.shippingMethodEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="deletedatLabel" for="shipping-method-deletedat">
                  <Translate contentKey="venjureApp.shippingMethod.deletedat">Deletedat</Translate>
                </Label>
                <AvInput
                  id="shipping-method-deletedat"
                  data-cy="deletedat"
                  type="datetime-local"
                  className="form-control"
                  name="deletedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.shippingMethodEntity.deletedat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="codeLabel" for="shipping-method-code">
                  <Translate contentKey="venjureApp.shippingMethod.code">Code</Translate>
                </Label>
                <AvField
                  id="shipping-method-code"
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
                <Label id="checkerLabel" for="shipping-method-checker">
                  <Translate contentKey="venjureApp.shippingMethod.checker">Checker</Translate>
                </Label>
                <AvField
                  id="shipping-method-checker"
                  data-cy="checker"
                  type="text"
                  name="checker"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="calculatorLabel" for="shipping-method-calculator">
                  <Translate contentKey="venjureApp.shippingMethod.calculator">Calculator</Translate>
                </Label>
                <AvField
                  id="shipping-method-calculator"
                  data-cy="calculator"
                  type="text"
                  name="calculator"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fulfillmenthandlercodeLabel" for="shipping-method-fulfillmenthandlercode">
                  <Translate contentKey="venjureApp.shippingMethod.fulfillmenthandlercode">Fulfillmenthandlercode</Translate>
                </Label>
                <AvField
                  id="shipping-method-fulfillmenthandlercode"
                  data-cy="fulfillmenthandlercode"
                  type="text"
                  name="fulfillmenthandlercode"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/shipping-method" replace color="info">
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
  shippingMethodEntity: storeState.shippingMethod.entity,
  loading: storeState.shippingMethod.loading,
  updating: storeState.shippingMethod.updating,
  updateSuccess: storeState.shippingMethod.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(ShippingMethodUpdate);
