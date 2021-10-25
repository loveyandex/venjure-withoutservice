import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order-modification.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderModificationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderModificationDetail = (props: IOrderModificationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { orderModificationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderModificationDetailsHeading">
          <Translate contentKey="venjureApp.orderModification.detail.title">OrderModification</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderModificationEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.orderModification.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {orderModificationEntity.createdat ? (
              <TextFormat value={orderModificationEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.orderModification.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {orderModificationEntity.updatedat ? (
              <TextFormat value={orderModificationEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="note">
              <Translate contentKey="venjureApp.orderModification.note">Note</Translate>
            </span>
          </dt>
          <dd>{orderModificationEntity.note}</dd>
          <dt>
            <span id="pricechange">
              <Translate contentKey="venjureApp.orderModification.pricechange">Pricechange</Translate>
            </span>
          </dt>
          <dd>{orderModificationEntity.pricechange}</dd>
          <dt>
            <span id="shippingaddresschange">
              <Translate contentKey="venjureApp.orderModification.shippingaddresschange">Shippingaddresschange</Translate>
            </span>
          </dt>
          <dd>{orderModificationEntity.shippingaddresschange}</dd>
          <dt>
            <span id="billingaddresschange">
              <Translate contentKey="venjureApp.orderModification.billingaddresschange">Billingaddresschange</Translate>
            </span>
          </dt>
          <dd>{orderModificationEntity.billingaddresschange}</dd>
          <dt>
            <Translate contentKey="venjureApp.orderModification.payment">Payment</Translate>
          </dt>
          <dd>{orderModificationEntity.payment ? orderModificationEntity.payment.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.orderModification.refund">Refund</Translate>
          </dt>
          <dd>{orderModificationEntity.refund ? orderModificationEntity.refund.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.orderModification.jorder">Jorder</Translate>
          </dt>
          <dd>{orderModificationEntity.jorder ? orderModificationEntity.jorder.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-modification" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-modification/${orderModificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ orderModification }: IRootState) => ({
  orderModificationEntity: orderModification.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderModificationDetail);
