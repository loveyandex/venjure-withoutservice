import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order-line.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderLineDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderLineDetail = (props: IOrderLineDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { orderLineEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderLineDetailsHeading">
          <Translate contentKey="venjureApp.orderLine.detail.title">OrderLine</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderLineEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.orderLine.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {orderLineEntity.createdat ? <TextFormat value={orderLineEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.orderLine.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {orderLineEntity.updatedat ? <TextFormat value={orderLineEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.orderLine.productvariant">Productvariant</Translate>
          </dt>
          <dd>{orderLineEntity.productvariant ? orderLineEntity.productvariant.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.orderLine.taxcategory">Taxcategory</Translate>
          </dt>
          <dd>{orderLineEntity.taxcategory ? orderLineEntity.taxcategory.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.orderLine.featuredAsset">Featured Asset</Translate>
          </dt>
          <dd>{orderLineEntity.featuredAsset ? orderLineEntity.featuredAsset.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.orderLine.jorder">Jorder</Translate>
          </dt>
          <dd>{orderLineEntity.jorder ? orderLineEntity.jorder.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-line" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-line/${orderLineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ orderLine }: IRootState) => ({
  orderLineEntity: orderLine.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderLineDetail);
