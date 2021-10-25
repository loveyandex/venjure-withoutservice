import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProductVariant } from 'app/shared/model/product-variant.model';
import { getEntities as getProductVariants } from 'app/entities/product-variant/product-variant.reducer';
import { IOrderItem } from 'app/shared/model/order-item.model';
import { getEntities as getOrderItems } from 'app/entities/order-item/order-item.reducer';
import { IOrderLine } from 'app/shared/model/order-line.model';
import { getEntities as getOrderLines } from 'app/entities/order-line/order-line.reducer';
import { getEntity, updateEntity, createEntity, reset } from './stock-movement.reducer';
import { IStockMovement } from 'app/shared/model/stock-movement.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IStockMovementUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StockMovementUpdate = (props: IStockMovementUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { stockMovementEntity, productVariants, orderItems, orderLines, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/stock-movement' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getProductVariants();
    props.getOrderItems();
    props.getOrderLines();
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
        ...stockMovementEntity,
        ...values,
        productvariant: productVariants.find(it => it.id.toString() === values.productvariantId.toString()),
        orderitem: orderItems.find(it => it.id.toString() === values.orderitemId.toString()),
        orderline: orderLines.find(it => it.id.toString() === values.orderlineId.toString()),
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
          <h2 id="venjureApp.stockMovement.home.createOrEditLabel" data-cy="StockMovementCreateUpdateHeading">
            <Translate contentKey="venjureApp.stockMovement.home.createOrEditLabel">Create or edit a StockMovement</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : stockMovementEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="stock-movement-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="stock-movement-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="stock-movement-createdat">
                  <Translate contentKey="venjureApp.stockMovement.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="stock-movement-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.stockMovementEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="stock-movement-updatedat">
                  <Translate contentKey="venjureApp.stockMovement.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="stock-movement-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.stockMovementEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="stock-movement-type">
                  <Translate contentKey="venjureApp.stockMovement.type">Type</Translate>
                </Label>
                <AvField
                  id="stock-movement-type"
                  data-cy="type"
                  type="text"
                  name="type"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="quantityLabel" for="stock-movement-quantity">
                  <Translate contentKey="venjureApp.stockMovement.quantity">Quantity</Translate>
                </Label>
                <AvField
                  id="stock-movement-quantity"
                  data-cy="quantity"
                  type="string"
                  className="form-control"
                  name="quantity"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="discriminatorLabel" for="stock-movement-discriminator">
                  <Translate contentKey="venjureApp.stockMovement.discriminator">Discriminator</Translate>
                </Label>
                <AvField
                  id="stock-movement-discriminator"
                  data-cy="discriminator"
                  type="text"
                  name="discriminator"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="stock-movement-productvariant">
                  <Translate contentKey="venjureApp.stockMovement.productvariant">Productvariant</Translate>
                </Label>
                <AvInput
                  id="stock-movement-productvariant"
                  data-cy="productvariant"
                  type="select"
                  className="form-control"
                  name="productvariantId"
                >
                  <option value="" key="0" />
                  {productVariants
                    ? productVariants.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="stock-movement-orderitem">
                  <Translate contentKey="venjureApp.stockMovement.orderitem">Orderitem</Translate>
                </Label>
                <AvInput id="stock-movement-orderitem" data-cy="orderitem" type="select" className="form-control" name="orderitemId">
                  <option value="" key="0" />
                  {orderItems
                    ? orderItems.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="stock-movement-orderline">
                  <Translate contentKey="venjureApp.stockMovement.orderline">Orderline</Translate>
                </Label>
                <AvInput id="stock-movement-orderline" data-cy="orderline" type="select" className="form-control" name="orderlineId">
                  <option value="" key="0" />
                  {orderLines
                    ? orderLines.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/stock-movement" replace color="info">
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
  productVariants: storeState.productVariant.entities,
  orderItems: storeState.orderItem.entities,
  orderLines: storeState.orderLine.entities,
  stockMovementEntity: storeState.stockMovement.entity,
  loading: storeState.stockMovement.loading,
  updating: storeState.stockMovement.updating,
  updateSuccess: storeState.stockMovement.updateSuccess,
});

const mapDispatchToProps = {
  getProductVariants,
  getOrderItems,
  getOrderLines,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StockMovementUpdate);
