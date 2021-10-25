import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './global-settings.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGlobalSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GlobalSettingsDetail = (props: IGlobalSettingsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { globalSettingsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="globalSettingsDetailsHeading">
          <Translate contentKey="venjureApp.globalSettings.detail.title">GlobalSettings</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{globalSettingsEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.globalSettings.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {globalSettingsEntity.createdat ? (
              <TextFormat value={globalSettingsEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.globalSettings.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {globalSettingsEntity.updatedat ? (
              <TextFormat value={globalSettingsEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="availablelanguages">
              <Translate contentKey="venjureApp.globalSettings.availablelanguages">Availablelanguages</Translate>
            </span>
          </dt>
          <dd>{globalSettingsEntity.availablelanguages}</dd>
          <dt>
            <span id="trackinventory">
              <Translate contentKey="venjureApp.globalSettings.trackinventory">Trackinventory</Translate>
            </span>
          </dt>
          <dd>{globalSettingsEntity.trackinventory ? 'true' : 'false'}</dd>
          <dt>
            <span id="outofstockthreshold">
              <Translate contentKey="venjureApp.globalSettings.outofstockthreshold">Outofstockthreshold</Translate>
            </span>
          </dt>
          <dd>{globalSettingsEntity.outofstockthreshold}</dd>
        </dl>
        <Button tag={Link} to="/global-settings" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/global-settings/${globalSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ globalSettings }: IRootState) => ({
  globalSettingsEntity: globalSettings.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GlobalSettingsDetail);
