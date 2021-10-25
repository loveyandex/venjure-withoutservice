import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './stock-movement.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStockMovementDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StockMovementDetail = (props: IStockMovementDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { stockMovementEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stockMovementDetailsHeading">
          <Translate contentKey="venjureApp.stockMovement.detail.title">StockMovement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stockMovementEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.stockMovement.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {stockMovementEntity.createdat ? (
              <TextFormat value={stockMovementEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.stockMovement.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {stockMovementEntity.updatedat ? (
              <TextFormat value={stockMovementEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="type">
              <Translate contentKey="venjureApp.stockMovement.type">Type</Translate>
            </span>
          </dt>
          <dd>{stockMovementEntity.type}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="venjureApp.stockMovement.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{stockMovementEntity.quantity}</dd>
          <dt>
            <span id="discriminator">
              <Translate contentKey="venjureApp.stockMovement.discriminator">Discriminator</Translate>
            </span>
          </dt>
          <dd>{stockMovementEntity.discriminator}</dd>
          <dt>
            <Translate contentKey="venjureApp.stockMovement.productvariant">Productvariant</Translate>
          </dt>
          <dd>{stockMovementEntity.productvariant ? stockMovementEntity.productvariant.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.stockMovement.orderitem">Orderitem</Translate>
          </dt>
          <dd>{stockMovementEntity.orderitem ? stockMovementEntity.orderitem.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.stockMovement.orderline">Orderline</Translate>
          </dt>
          <dd>{stockMovementEntity.orderline ? stockMovementEntity.orderline.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/stock-movement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stock-movement/${stockMovementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ stockMovement }: IRootState) => ({
  stockMovementEntity: stockMovement.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StockMovementDetail);
