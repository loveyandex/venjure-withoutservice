import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './job-record.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IJobRecordDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const JobRecordDetail = (props: IJobRecordDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { jobRecordEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobRecordDetailsHeading">
          <Translate contentKey="venjureApp.jobRecord.detail.title">JobRecord</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.jobRecord.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {jobRecordEntity.createdat ? <TextFormat value={jobRecordEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.jobRecord.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {jobRecordEntity.updatedat ? <TextFormat value={jobRecordEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="queuename">
              <Translate contentKey="venjureApp.jobRecord.queuename">Queuename</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.queuename}</dd>
          <dt>
            <span id="data">
              <Translate contentKey="venjureApp.jobRecord.data">Data</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.data}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="venjureApp.jobRecord.state">State</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.state}</dd>
          <dt>
            <span id="progress">
              <Translate contentKey="venjureApp.jobRecord.progress">Progress</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.progress}</dd>
          <dt>
            <span id="result">
              <Translate contentKey="venjureApp.jobRecord.result">Result</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.result}</dd>
          <dt>
            <span id="error">
              <Translate contentKey="venjureApp.jobRecord.error">Error</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.error}</dd>
          <dt>
            <span id="startedat">
              <Translate contentKey="venjureApp.jobRecord.startedat">Startedat</Translate>
            </span>
          </dt>
          <dd>
            {jobRecordEntity.startedat ? <TextFormat value={jobRecordEntity.startedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="settledat">
              <Translate contentKey="venjureApp.jobRecord.settledat">Settledat</Translate>
            </span>
          </dt>
          <dd>
            {jobRecordEntity.settledat ? <TextFormat value={jobRecordEntity.settledat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="issettled">
              <Translate contentKey="venjureApp.jobRecord.issettled">Issettled</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.issettled ? 'true' : 'false'}</dd>
          <dt>
            <span id="retries">
              <Translate contentKey="venjureApp.jobRecord.retries">Retries</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.retries}</dd>
          <dt>
            <span id="attempts">
              <Translate contentKey="venjureApp.jobRecord.attempts">Attempts</Translate>
            </span>
          </dt>
          <dd>{jobRecordEntity.attempts}</dd>
        </dl>
        <Button tag={Link} to="/job-record" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job-record/${jobRecordEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ jobRecord }: IRootState) => ({
  jobRecordEntity: jobRecord.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(JobRecordDetail);
