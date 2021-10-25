import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './tax-rate.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITaxRateDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TaxRateDetail = (props: ITaxRateDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { taxRateEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taxRateDetailsHeading">
          <Translate contentKey="venjureApp.taxRate.detail.title">TaxRate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taxRateEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.taxRate.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{taxRateEntity.createdat ? <TextFormat value={taxRateEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.taxRate.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{taxRateEntity.updatedat ? <TextFormat value={taxRateEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.taxRate.name">Name</Translate>
            </span>
          </dt>
          <dd>{taxRateEntity.name}</dd>
          <dt>
            <span id="enabled">
              <Translate contentKey="venjureApp.taxRate.enabled">Enabled</Translate>
            </span>
          </dt>
          <dd>{taxRateEntity.enabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="venjureApp.taxRate.value">Value</Translate>
            </span>
          </dt>
          <dd>{taxRateEntity.value}</dd>
          <dt>
            <Translate contentKey="venjureApp.taxRate.category">Category</Translate>
          </dt>
          <dd>{taxRateEntity.category ? taxRateEntity.category.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.taxRate.zone">Zone</Translate>
          </dt>
          <dd>{taxRateEntity.zone ? taxRateEntity.zone.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.taxRate.customergroup">Customergroup</Translate>
          </dt>
          <dd>{taxRateEntity.customergroup ? taxRateEntity.customergroup.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tax-rate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tax-rate/${taxRateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ taxRate }: IRootState) => ({
  taxRateEntity: taxRate.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TaxRateDetail);
