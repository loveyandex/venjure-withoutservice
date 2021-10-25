import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order-item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderItemDetail = (props: IOrderItemDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { orderItemEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderItemDetailsHeading">
          <Translate contentKey="venjureApp.orderItem.detail.title">OrderItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderItemEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.orderItem.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {orderItemEntity.createdat ? <TextFormat value={orderItemEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.orderItem.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {orderItemEntity.updatedat ? <TextFormat value={orderItemEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="initiallistprice">
              <Translate contentKey="venjureApp.orderItem.initiallistprice">Initiallistprice</Translate>
            </span>
          </dt>
          <dd>{orderItemEntity.initiallistprice}</dd>
          <dt>
            <span id="listprice">
              <Translate contentKey="venjureApp.orderItem.listprice">Listprice</Translate>
            </span>
          </dt>
          <dd>{orderItemEntity.listprice}</dd>
          <dt>
            <span id="listpriceincludestax">
              <Translate contentKey="venjureApp.orderItem.listpriceincludestax">Listpriceincludestax</Translate>
            </span>
          </dt>
          <dd>{orderItemEntity.listpriceincludestax ? 'true' : 'false'}</dd>
          <dt>
            <span id="adjustments">
              <Translate contentKey="venjureApp.orderItem.adjustments">Adjustments</Translate>
            </span>
          </dt>
          <dd>{orderItemEntity.adjustments}</dd>
          <dt>
            <span id="taxlines">
              <Translate contentKey="venjureApp.orderItem.taxlines">Taxlines</Translate>
            </span>
          </dt>
          <dd>{orderItemEntity.taxlines}</dd>
          <dt>
            <span id="cancelled">
              <Translate contentKey="venjureApp.orderItem.cancelled">Cancelled</Translate>
            </span>
          </dt>
          <dd>{orderItemEntity.cancelled ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="venjureApp.orderItem.line">Line</Translate>
          </dt>
          <dd>{orderItemEntity.line ? orderItemEntity.line.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.orderItem.refund">Refund</Translate>
          </dt>
          <dd>{orderItemEntity.refund ? orderItemEntity.refund.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.orderItem.fulfillment">Fulfillment</Translate>
          </dt>
          <dd>
            {orderItemEntity.fulfillments
              ? orderItemEntity.fulfillments.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {orderItemEntity.fulfillments && i === orderItemEntity.fulfillments.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.orderItem.orderModification">Order Modification</Translate>
          </dt>
          <dd>
            {orderItemEntity.orderModifications
              ? orderItemEntity.orderModifications.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {orderItemEntity.orderModifications && i === orderItemEntity.orderModifications.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/order-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-item/${orderItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ orderItem }: IRootState) => ({
  orderItemEntity: orderItem.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderItemDetail);
