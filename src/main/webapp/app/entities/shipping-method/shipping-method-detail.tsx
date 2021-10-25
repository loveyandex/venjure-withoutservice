import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shipping-method.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShippingMethodDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShippingMethodDetail = (props: IShippingMethodDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { shippingMethodEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shippingMethodDetailsHeading">
          <Translate contentKey="venjureApp.shippingMethod.detail.title">ShippingMethod</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shippingMethodEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.shippingMethod.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {shippingMethodEntity.createdat ? (
              <TextFormat value={shippingMethodEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.shippingMethod.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {shippingMethodEntity.updatedat ? (
              <TextFormat value={shippingMethodEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="deletedat">
              <Translate contentKey="venjureApp.shippingMethod.deletedat">Deletedat</Translate>
            </span>
          </dt>
          <dd>
            {shippingMethodEntity.deletedat ? (
              <TextFormat value={shippingMethodEntity.deletedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="code">
              <Translate contentKey="venjureApp.shippingMethod.code">Code</Translate>
            </span>
          </dt>
          <dd>{shippingMethodEntity.code}</dd>
          <dt>
            <span id="checker">
              <Translate contentKey="venjureApp.shippingMethod.checker">Checker</Translate>
            </span>
          </dt>
          <dd>{shippingMethodEntity.checker}</dd>
          <dt>
            <span id="calculator">
              <Translate contentKey="venjureApp.shippingMethod.calculator">Calculator</Translate>
            </span>
          </dt>
          <dd>{shippingMethodEntity.calculator}</dd>
          <dt>
            <span id="fulfillmenthandlercode">
              <Translate contentKey="venjureApp.shippingMethod.fulfillmenthandlercode">Fulfillmenthandlercode</Translate>
            </span>
          </dt>
          <dd>{shippingMethodEntity.fulfillmenthandlercode}</dd>
        </dl>
        <Button tag={Link} to="/shipping-method" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shipping-method/${shippingMethodEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ shippingMethod }: IRootState) => ({
  shippingMethodEntity: shippingMethod.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShippingMethodDetail);
