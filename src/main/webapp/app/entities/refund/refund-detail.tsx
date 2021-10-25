import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './refund.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRefundDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RefundDetail = (props: IRefundDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { refundEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="refundDetailsHeading">
          <Translate contentKey="venjureApp.refund.detail.title">Refund</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{refundEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.refund.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{refundEntity.createdat ? <TextFormat value={refundEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.refund.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{refundEntity.updatedat ? <TextFormat value={refundEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="items">
              <Translate contentKey="venjureApp.refund.items">Items</Translate>
            </span>
          </dt>
          <dd>{refundEntity.items}</dd>
          <dt>
            <span id="shipping">
              <Translate contentKey="venjureApp.refund.shipping">Shipping</Translate>
            </span>
          </dt>
          <dd>{refundEntity.shipping}</dd>
          <dt>
            <span id="adjustment">
              <Translate contentKey="venjureApp.refund.adjustment">Adjustment</Translate>
            </span>
          </dt>
          <dd>{refundEntity.adjustment}</dd>
          <dt>
            <span id="total">
              <Translate contentKey="venjureApp.refund.total">Total</Translate>
            </span>
          </dt>
          <dd>{refundEntity.total}</dd>
          <dt>
            <span id="method">
              <Translate contentKey="venjureApp.refund.method">Method</Translate>
            </span>
          </dt>
          <dd>{refundEntity.method}</dd>
          <dt>
            <span id="reason">
              <Translate contentKey="venjureApp.refund.reason">Reason</Translate>
            </span>
          </dt>
          <dd>{refundEntity.reason}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="venjureApp.refund.state">State</Translate>
            </span>
          </dt>
          <dd>{refundEntity.state}</dd>
          <dt>
            <span id="transactionid">
              <Translate contentKey="venjureApp.refund.transactionid">Transactionid</Translate>
            </span>
          </dt>
          <dd>{refundEntity.transactionid}</dd>
          <dt>
            <span id="metadata">
              <Translate contentKey="venjureApp.refund.metadata">Metadata</Translate>
            </span>
          </dt>
          <dd>{refundEntity.metadata}</dd>
          <dt>
            <Translate contentKey="venjureApp.refund.payment">Payment</Translate>
          </dt>
          <dd>{refundEntity.payment ? refundEntity.payment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/refund" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/refund/${refundEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ refund }: IRootState) => ({
  refundEntity: refund.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RefundDetail);
