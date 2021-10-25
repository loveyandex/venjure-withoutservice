import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './promotion.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPromotionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PromotionDetail = (props: IPromotionDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { promotionEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="promotionDetailsHeading">
          <Translate contentKey="venjureApp.promotion.detail.title">Promotion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.promotion.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {promotionEntity.createdat ? <TextFormat value={promotionEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.promotion.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {promotionEntity.updatedat ? <TextFormat value={promotionEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedat">
              <Translate contentKey="venjureApp.promotion.deletedat">Deletedat</Translate>
            </span>
          </dt>
          <dd>
            {promotionEntity.deletedat ? <TextFormat value={promotionEntity.deletedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="startsat">
              <Translate contentKey="venjureApp.promotion.startsat">Startsat</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.startsat ? <TextFormat value={promotionEntity.startsat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endsat">
              <Translate contentKey="venjureApp.promotion.endsat">Endsat</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.endsat ? <TextFormat value={promotionEntity.endsat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="couponcode">
              <Translate contentKey="venjureApp.promotion.couponcode">Couponcode</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.couponcode}</dd>
          <dt>
            <span id="percustomerusagelimit">
              <Translate contentKey="venjureApp.promotion.percustomerusagelimit">Percustomerusagelimit</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.percustomerusagelimit}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.promotion.name">Name</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.name}</dd>
          <dt>
            <span id="enabled">
              <Translate contentKey="venjureApp.promotion.enabled">Enabled</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.enabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="conditions">
              <Translate contentKey="venjureApp.promotion.conditions">Conditions</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.conditions}</dd>
          <dt>
            <span id="actions">
              <Translate contentKey="venjureApp.promotion.actions">Actions</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.actions}</dd>
          <dt>
            <span id="priorityscore">
              <Translate contentKey="venjureApp.promotion.priorityscore">Priorityscore</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.priorityscore}</dd>
        </dl>
        <Button tag={Link} to="/promotion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/promotion/${promotionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ promotion }: IRootState) => ({
  promotionEntity: promotion.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PromotionDetail);
