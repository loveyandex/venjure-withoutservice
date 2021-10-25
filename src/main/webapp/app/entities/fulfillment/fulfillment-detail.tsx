import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './fulfillment.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFulfillmentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FulfillmentDetail = (props: IFulfillmentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { fulfillmentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fulfillmentDetailsHeading">
          <Translate contentKey="venjureApp.fulfillment.detail.title">Fulfillment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{fulfillmentEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.fulfillment.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {fulfillmentEntity.createdat ? <TextFormat value={fulfillmentEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.fulfillment.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {fulfillmentEntity.updatedat ? <TextFormat value={fulfillmentEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="state">
              <Translate contentKey="venjureApp.fulfillment.state">State</Translate>
            </span>
          </dt>
          <dd>{fulfillmentEntity.state}</dd>
          <dt>
            <span id="trackingcode">
              <Translate contentKey="venjureApp.fulfillment.trackingcode">Trackingcode</Translate>
            </span>
          </dt>
          <dd>{fulfillmentEntity.trackingcode}</dd>
          <dt>
            <span id="method">
              <Translate contentKey="venjureApp.fulfillment.method">Method</Translate>
            </span>
          </dt>
          <dd>{fulfillmentEntity.method}</dd>
          <dt>
            <span id="handlercode">
              <Translate contentKey="venjureApp.fulfillment.handlercode">Handlercode</Translate>
            </span>
          </dt>
          <dd>{fulfillmentEntity.handlercode}</dd>
        </dl>
        <Button tag={Link} to="/fulfillment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/fulfillment/${fulfillmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ fulfillment }: IRootState) => ({
  fulfillmentEntity: fulfillment.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FulfillmentDetail);
