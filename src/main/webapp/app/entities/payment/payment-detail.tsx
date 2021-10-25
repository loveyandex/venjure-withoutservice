import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payment.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PaymentDetail = (props: IPaymentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { paymentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailsHeading">
          <Translate contentKey="venjureApp.payment.detail.title">Payment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.payment.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.createdat ? <TextFormat value={paymentEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.payment.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.updatedat ? <TextFormat value={paymentEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="method">
              <Translate contentKey="venjureApp.payment.method">Method</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.method}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="venjureApp.payment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.amount}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="venjureApp.payment.state">State</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.state}</dd>
          <dt>
            <span id="errormessage">
              <Translate contentKey="venjureApp.payment.errormessage">Errormessage</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.errormessage}</dd>
          <dt>
            <span id="transactionid">
              <Translate contentKey="venjureApp.payment.transactionid">Transactionid</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.transactionid}</dd>
          <dt>
            <span id="metadata">
              <Translate contentKey="venjureApp.payment.metadata">Metadata</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.metadata}</dd>
          <dt>
            <Translate contentKey="venjureApp.payment.jorder">Jorder</Translate>
          </dt>
          <dd>{paymentEntity.jorder ? paymentEntity.jorder.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment/${paymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ payment }: IRootState) => ({
  paymentEntity: payment.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentDetail);
