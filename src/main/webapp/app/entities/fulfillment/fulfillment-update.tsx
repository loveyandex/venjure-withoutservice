import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrderItem } from 'app/shared/model/order-item.model';
import { getEntities as getOrderItems } from 'app/entities/order-item/order-item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './fulfillment.reducer';
import { IFulfillment } from 'app/shared/model/fulfillment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFulfillmentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FulfillmentUpdate = (props: IFulfillmentUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { fulfillmentEntity, orderItems, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/fulfillment' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

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
        ...fulfillmentEntity,
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
          <h2 id="venjureApp.fulfillment.home.createOrEditLabel" data-cy="FulfillmentCreateUpdateHeading">
            <Translate contentKey="venjureApp.fulfillment.home.createOrEditLabel">Create or edit a Fulfillment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : fulfillmentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="fulfillment-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="fulfillment-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="fulfillment-createdat">
                  <Translate contentKey="venjureApp.fulfillment.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="fulfillment-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.fulfillmentEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="fulfillment-updatedat">
                  <Translate contentKey="venjureApp.fulfillment.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="fulfillment-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.fulfillmentEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="stateLabel" for="fulfillment-state">
                  <Translate contentKey="venjureApp.fulfillment.state">State</Translate>
                </Label>
                <AvField
                  id="fulfillment-state"
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
                <Label id="trackingcodeLabel" for="fulfillment-trackingcode">
                  <Translate contentKey="venjureApp.fulfillment.trackingcode">Trackingcode</Translate>
                </Label>
                <AvField
                  id="fulfillment-trackingcode"
                  data-cy="trackingcode"
                  type="text"
                  name="trackingcode"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="methodLabel" for="fulfillment-method">
                  <Translate contentKey="venjureApp.fulfillment.method">Method</Translate>
                </Label>
                <AvField
                  id="fulfillment-method"
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
                <Label id="handlercodeLabel" for="fulfillment-handlercode">
                  <Translate contentKey="venjureApp.fulfillment.handlercode">Handlercode</Translate>
                </Label>
                <AvField
                  id="fulfillment-handlercode"
                  data-cy="handlercode"
                  type="text"
                  name="handlercode"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/fulfillment" replace color="info">
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
  orderItems: storeState.orderItem.entities,
  fulfillmentEntity: storeState.fulfillment.entity,
  loading: storeState.fulfillment.loading,
  updating: storeState.fulfillment.updating,
  updateSuccess: storeState.fulfillment.updateSuccess,
});

const mapDispatchToProps = {
  getOrderItems,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FulfillmentUpdate);
