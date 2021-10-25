import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './zone.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IZoneDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ZoneDetail = (props: IZoneDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { zoneEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="zoneDetailsHeading">
          <Translate contentKey="venjureApp.zone.detail.title">Zone</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{zoneEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.zone.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{zoneEntity.createdat ? <TextFormat value={zoneEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.zone.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{zoneEntity.updatedat ? <TextFormat value={zoneEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.zone.name">Name</Translate>
            </span>
          </dt>
          <dd>{zoneEntity.name}</dd>
          <dt>
            <Translate contentKey="venjureApp.zone.country">Country</Translate>
          </dt>
          <dd>
            {zoneEntity.countries
              ? zoneEntity.countries.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {zoneEntity.countries && i === zoneEntity.countries.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/zone" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/zone/${zoneEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ zone }: IRootState) => ({
  zoneEntity: zone.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ZoneDetail);
