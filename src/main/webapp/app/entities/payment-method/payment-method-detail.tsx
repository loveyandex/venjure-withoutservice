import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payment-method.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentMethodDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PaymentMethodDetail = (props: IPaymentMethodDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { paymentMethodEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentMethodDetailsHeading">
          <Translate contentKey="venjureApp.paymentMethod.detail.title">PaymentMethod</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.paymentMethod.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {paymentMethodEntity.createdat ? (
              <TextFormat value={paymentMethodEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.paymentMethod.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {paymentMethodEntity.updatedat ? (
              <TextFormat value={paymentMethodEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.paymentMethod.name">Name</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.name}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="venjureApp.paymentMethod.code">Code</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.code}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="venjureApp.paymentMethod.description">Description</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.description}</dd>
          <dt>
            <span id="enabled">
              <Translate contentKey="venjureApp.paymentMethod.enabled">Enabled</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.enabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="checker">
              <Translate contentKey="venjureApp.paymentMethod.checker">Checker</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.checker}</dd>
          <dt>
            <span id="handler">
              <Translate contentKey="venjureApp.paymentMethod.handler">Handler</Translate>
            </span>
          </dt>
          <dd>{paymentMethodEntity.handler}</dd>
        </dl>
        <Button tag={Link} to="/payment-method" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-method/${paymentMethodEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ paymentMethod }: IRootState) => ({
  paymentMethodEntity: paymentMethod.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PaymentMethodDetail);
