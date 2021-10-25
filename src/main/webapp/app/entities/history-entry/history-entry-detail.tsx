import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './history-entry.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHistoryEntryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HistoryEntryDetail = (props: IHistoryEntryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { historyEntryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="historyEntryDetailsHeading">
          <Translate contentKey="venjureApp.historyEntry.detail.title">HistoryEntry</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{historyEntryEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.historyEntry.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {historyEntryEntity.createdat ? <TextFormat value={historyEntryEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.historyEntry.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {historyEntryEntity.updatedat ? <TextFormat value={historyEntryEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="type">
              <Translate contentKey="venjureApp.historyEntry.type">Type</Translate>
            </span>
          </dt>
          <dd>{historyEntryEntity.type}</dd>
          <dt>
            <span id="ispublic">
              <Translate contentKey="venjureApp.historyEntry.ispublic">Ispublic</Translate>
            </span>
          </dt>
          <dd>{historyEntryEntity.ispublic ? 'true' : 'false'}</dd>
          <dt>
            <span id="data">
              <Translate contentKey="venjureApp.historyEntry.data">Data</Translate>
            </span>
          </dt>
          <dd>{historyEntryEntity.data}</dd>
          <dt>
            <span id="discriminator">
              <Translate contentKey="venjureApp.historyEntry.discriminator">Discriminator</Translate>
            </span>
          </dt>
          <dd>{historyEntryEntity.discriminator}</dd>
          <dt>
            <Translate contentKey="venjureApp.historyEntry.administrator">Administrator</Translate>
          </dt>
          <dd>{historyEntryEntity.administrator ? historyEntryEntity.administrator.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.historyEntry.customer">Customer</Translate>
          </dt>
          <dd>{historyEntryEntity.customer ? historyEntryEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.historyEntry.jorder">Jorder</Translate>
          </dt>
          <dd>{historyEntryEntity.jorder ? historyEntryEntity.jorder.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/history-entry" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/history-entry/${historyEntryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ historyEntry }: IRootState) => ({
  historyEntryEntity: historyEntry.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HistoryEntryDetail);
