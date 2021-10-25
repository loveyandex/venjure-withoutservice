import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './jorder.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IJorderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const JorderDetail = (props: IJorderDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { jorderEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jorderDetailsHeading">
          <Translate contentKey="venjureApp.jorder.detail.title">Jorder</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.jorder.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.createdat ? <TextFormat value={jorderEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.jorder.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.updatedat ? <TextFormat value={jorderEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="venjureApp.jorder.code">Code</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.code}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="venjureApp.jorder.state">State</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.state}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="venjureApp.jorder.active">Active</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="orderplacedat">
              <Translate contentKey="venjureApp.jorder.orderplacedat">Orderplacedat</Translate>
            </span>
          </dt>
          <dd>
            {jorderEntity.orderplacedat ? <TextFormat value={jorderEntity.orderplacedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="couponcodes">
              <Translate contentKey="venjureApp.jorder.couponcodes">Couponcodes</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.couponcodes}</dd>
          <dt>
            <span id="shippingaddress">
              <Translate contentKey="venjureApp.jorder.shippingaddress">Shippingaddress</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.shippingaddress}</dd>
          <dt>
            <span id="billingaddress">
              <Translate contentKey="venjureApp.jorder.billingaddress">Billingaddress</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.billingaddress}</dd>
          <dt>
            <span id="currencycode">
              <Translate contentKey="venjureApp.jorder.currencycode">Currencycode</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.currencycode}</dd>
          <dt>
            <span id="subtotal">
              <Translate contentKey="venjureApp.jorder.subtotal">Subtotal</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.subtotal}</dd>
          <dt>
            <span id="subtotalwithtax">
              <Translate contentKey="venjureApp.jorder.subtotalwithtax">Subtotalwithtax</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.subtotalwithtax}</dd>
          <dt>
            <span id="shipping">
              <Translate contentKey="venjureApp.jorder.shipping">Shipping</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.shipping}</dd>
          <dt>
            <span id="shippingwithtax">
              <Translate contentKey="venjureApp.jorder.shippingwithtax">Shippingwithtax</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.shippingwithtax}</dd>
          <dt>
            <span id="taxzoneid">
              <Translate contentKey="venjureApp.jorder.taxzoneid">Taxzoneid</Translate>
            </span>
          </dt>
          <dd>{jorderEntity.taxzoneid}</dd>
          <dt>
            <Translate contentKey="venjureApp.jorder.customer">Customer</Translate>
          </dt>
          <dd>{jorderEntity.customer ? jorderEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.jorder.channel">Channel</Translate>
          </dt>
          <dd>
            {jorderEntity.channels
              ? jorderEntity.channels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {jorderEntity.channels && i === jorderEntity.channels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.jorder.promotion">Promotion</Translate>
          </dt>
          <dd>
            {jorderEntity.promotions
              ? jorderEntity.promotions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {jorderEntity.promotions && i === jorderEntity.promotions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/jorder" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/jorder/${jorderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ jorder }: IRootState) => ({
  jorderEntity: jorder.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(JorderDetail);
