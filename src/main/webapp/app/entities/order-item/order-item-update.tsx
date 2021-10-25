import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrderLine } from 'app/shared/model/order-line.model';
import { getEntities as getOrderLines } from 'app/entities/order-line/order-line.reducer';
import { IRefund } from 'app/shared/model/refund.model';
import { getEntities as getRefunds } from 'app/entities/refund/refund.reducer';
import { IFulfillment } from 'app/shared/model/fulfillment.model';
import { getEntities as getFulfillments } from 'app/entities/fulfillment/fulfillment.reducer';
import { IOrderModification } from 'app/shared/model/order-modification.model';
import { getEntities as getOrderModifications } from 'app/entities/order-modification/order-modification.reducer';
import { getEntity, updateEntity, createEntity, reset } from './order-item.reducer';
import { IOrderItem } from 'app/shared/model/order-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrderItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderItemUpdate = (props: IOrderItemUpdateProps) => {
  const [idsfulfillment, setIdsfulfillment] = useState([]);
  const [idsorderModification, setIdsorderModification] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { orderItemEntity, orderLines, refunds, fulfillments, orderModifications, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/order-item' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getOrderLines();
    props.getRefunds();
    props.getFulfillments();
    props.getOrderModifications();
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
        ...orderItemEntity,
        ...values,
        fulfillments: mapIdList(values.fulfillments),
        orderModifications: mapIdList(values.orderModifications),
        line: orderLines.find(it => it.id.toString() === values.lineId.toString()),
        refund: refunds.find(it => it.id.toString() === values.refundId.toString()),
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
          <h2 id="venjureApp.orderItem.home.createOrEditLabel" data-cy="OrderItemCreateUpdateHeading">
            <Translate contentKey="venjureApp.orderItem.home.createOrEditLabel">Create or edit a OrderItem</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : orderItemEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="order-item-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="order-item-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="order-item-createdat">
                  <Translate contentKey="venjureApp.orderItem.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="order-item-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.orderItemEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="order-item-updatedat">
                  <Translate contentKey="venjureApp.orderItem.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="order-item-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.orderItemEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="initiallistpriceLabel" for="order-item-initiallistprice">
                  <Translate contentKey="venjureApp.orderItem.initiallistprice">Initiallistprice</Translate>
                </Label>
                <AvField
                  id="order-item-initiallistprice"
                  data-cy="initiallistprice"
                  type="string"
                  className="form-control"
                  name="initiallistprice"
                />
              </AvGroup>
              <AvGroup>
                <Label id="listpriceLabel" for="order-item-listprice">
                  <Translate contentKey="venjureApp.orderItem.listprice">Listprice</Translate>
                </Label>
                <AvField
                  id="order-item-listprice"
                  data-cy="listprice"
                  type="string"
                  className="form-control"
                  name="listprice"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="listpriceincludestaxLabel">
                  <AvInput
                    id="order-item-listpriceincludestax"
                    data-cy="listpriceincludestax"
                    type="checkbox"
                    className="form-check-input"
                    name="listpriceincludestax"
                  />
                  <Translate contentKey="venjureApp.orderItem.listpriceincludestax">Listpriceincludestax</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="adjustmentsLabel" for="order-item-adjustments">
                  <Translate contentKey="venjureApp.orderItem.adjustments">Adjustments</Translate>
                </Label>
                <AvField
                  id="order-item-adjustments"
                  data-cy="adjustments"
                  type="text"
                  name="adjustments"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="taxlinesLabel" for="order-item-taxlines">
                  <Translate contentKey="venjureApp.orderItem.taxlines">Taxlines</Translate>
                </Label>
                <AvField
                  id="order-item-taxlines"
                  data-cy="taxlines"
                  type="text"
                  name="taxlines"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="cancelledLabel">
                  <AvInput id="order-item-cancelled" data-cy="cancelled" type="checkbox" className="form-check-input" name="cancelled" />
                  <Translate contentKey="venjureApp.orderItem.cancelled">Cancelled</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="order-item-line">
                  <Translate contentKey="venjureApp.orderItem.line">Line</Translate>
                </Label>
                <AvInput id="order-item-line" data-cy="line" type="select" className="form-control" name="lineId" required>
                  <option value="" key="0" />
                  {orderLines
                    ? orderLines.map(otherEntity => (
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
              <AvGroup>
                <Label for="order-item-refund">
                  <Translate contentKey="venjureApp.orderItem.refund">Refund</Translate>
                </Label>
                <AvInput id="order-item-refund" data-cy="refund" type="select" className="form-control" name="refundId">
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
                <Label for="order-item-fulfillment">
                  <Translate contentKey="venjureApp.orderItem.fulfillment">Fulfillment</Translate>
                </Label>
                <AvInput
                  id="order-item-fulfillment"
                  data-cy="fulfillment"
                  type="select"
                  multiple
                  className="form-control"
                  name="fulfillments"
                  value={!isNew && orderItemEntity.fulfillments && orderItemEntity.fulfillments.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {fulfillments
                    ? fulfillments.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="order-item-orderModification">
                  <Translate contentKey="venjureApp.orderItem.orderModification">Order Modification</Translate>
                </Label>
                <AvInput
                  id="order-item-orderModification"
                  data-cy="orderModification"
                  type="select"
                  multiple
                  className="form-control"
                  name="orderModifications"
                  value={!isNew && orderItemEntity.orderModifications && orderItemEntity.orderModifications.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {orderModifications
                    ? orderModifications.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/order-item" replace color="info">
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
  orderLines: storeState.orderLine.entities,
  refunds: storeState.refund.entities,
  fulfillments: storeState.fulfillment.entities,
  orderModifications: storeState.orderModification.entities,
  orderItemEntity: storeState.orderItem.entity,
  loading: storeState.orderItem.loading,
  updating: storeState.orderItem.updating,
  updateSuccess: storeState.orderItem.updateSuccess,
});

const mapDispatchToProps = {
  getOrderLines,
  getRefunds,
  getFulfillments,
  getOrderModifications,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderItemUpdate);
