import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './surcharge.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISurchargeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SurchargeDetail = (props: ISurchargeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { surchargeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="surchargeDetailsHeading">
          <Translate contentKey="venjureApp.surcharge.detail.title">Surcharge</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{surchargeEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.surcharge.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {surchargeEntity.createdat ? <TextFormat value={surchargeEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.surcharge.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {surchargeEntity.updatedat ? <TextFormat value={surchargeEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="venjureApp.surcharge.description">Description</Translate>
            </span>
          </dt>
          <dd>{surchargeEntity.description}</dd>
          <dt>
            <span id="listprice">
              <Translate contentKey="venjureApp.surcharge.listprice">Listprice</Translate>
            </span>
          </dt>
          <dd>{surchargeEntity.listprice}</dd>
          <dt>
            <span id="listpriceincludestax">
              <Translate contentKey="venjureApp.surcharge.listpriceincludestax">Listpriceincludestax</Translate>
            </span>
          </dt>
          <dd>{surchargeEntity.listpriceincludestax ? 'true' : 'false'}</dd>
          <dt>
            <span id="sku">
              <Translate contentKey="venjureApp.surcharge.sku">Sku</Translate>
            </span>
          </dt>
          <dd>{surchargeEntity.sku}</dd>
          <dt>
            <span id="taxlines">
              <Translate contentKey="venjureApp.surcharge.taxlines">Taxlines</Translate>
            </span>
          </dt>
          <dd>{surchargeEntity.taxlines}</dd>
          <dt>
            <Translate contentKey="venjureApp.surcharge.jorder">Jorder</Translate>
          </dt>
          <dd>{surchargeEntity.jorder ? surchargeEntity.jorder.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.surcharge.ordermodification">Ordermodification</Translate>
          </dt>
          <dd>{surchargeEntity.ordermodification ? surchargeEntity.ordermodification.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/surcharge" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/surcharge/${surchargeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ surcharge }: IRootState) => ({
  surchargeEntity: surcharge.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SurchargeDetail);
