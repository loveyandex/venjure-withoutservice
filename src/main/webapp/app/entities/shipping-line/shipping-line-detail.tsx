import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shipping-line.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShippingLineDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShippingLineDetail = (props: IShippingLineDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { shippingLineEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shippingLineDetailsHeading">
          <Translate contentKey="venjureApp.shippingLine.detail.title">ShippingLine</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shippingLineEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.shippingLine.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {shippingLineEntity.createdat ? <TextFormat value={shippingLineEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.shippingLine.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {shippingLineEntity.updatedat ? <TextFormat value={shippingLineEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="listprice">
              <Translate contentKey="venjureApp.shippingLine.listprice">Listprice</Translate>
            </span>
          </dt>
          <dd>{shippingLineEntity.listprice}</dd>
          <dt>
            <span id="listpriceincludestax">
              <Translate contentKey="venjureApp.shippingLine.listpriceincludestax">Listpriceincludestax</Translate>
            </span>
          </dt>
          <dd>{shippingLineEntity.listpriceincludestax ? 'true' : 'false'}</dd>
          <dt>
            <span id="adjustments">
              <Translate contentKey="venjureApp.shippingLine.adjustments">Adjustments</Translate>
            </span>
          </dt>
          <dd>{shippingLineEntity.adjustments}</dd>
          <dt>
            <span id="taxlines">
              <Translate contentKey="venjureApp.shippingLine.taxlines">Taxlines</Translate>
            </span>
          </dt>
          <dd>{shippingLineEntity.taxlines}</dd>
          <dt>
            <Translate contentKey="venjureApp.shippingLine.shippingmethod">Shippingmethod</Translate>
          </dt>
          <dd>{shippingLineEntity.shippingmethod ? shippingLineEntity.shippingmethod.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.shippingLine.jorder">Jorder</Translate>
          </dt>
          <dd>{shippingLineEntity.jorder ? shippingLineEntity.jorder.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/shipping-line" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shipping-line/${shippingLineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ shippingLine }: IRootState) => ({
  shippingLineEntity: shippingLine.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShippingLineDetail);
