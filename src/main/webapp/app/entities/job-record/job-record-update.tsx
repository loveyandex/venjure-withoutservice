import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './job-record.reducer';
import { IJobRecord } from 'app/shared/model/job-record.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IJobRecordUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const JobRecordUpdate = (props: IJobRecordUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { jobRecordEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/job-record' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdat = convertDateTimeToServer(values.createdat);
    values.updatedat = convertDateTimeToServer(values.updatedat);
    values.startedat = convertDateTimeToServer(values.startedat);
    values.settledat = convertDateTimeToServer(values.settledat);

    if (errors.length === 0) {
      const entity = {
        ...jobRecordEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="venjureApp.jobRecord.home.createOrEditLabel" data-cy="JobRecordCreateUpdateHeading">
            <Translate contentKey="venjureApp.jobRecord.home.createOrEditLabel">Create or edit a JobRecord</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : jobRecordEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="job-record-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="job-record-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="job-record-createdat">
                  <Translate contentKey="venjureApp.jobRecord.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="job-record-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.jobRecordEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="job-record-updatedat">
                  <Translate contentKey="venjureApp.jobRecord.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="job-record-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.jobRecordEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="queuenameLabel" for="job-record-queuename">
                  <Translate contentKey="venjureApp.jobRecord.queuename">Queuename</Translate>
                </Label>
                <AvField
                  id="job-record-queuename"
                  data-cy="queuename"
                  type="text"
                  name="queuename"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="dataLabel" for="job-record-data">
                  <Translate contentKey="venjureApp.jobRecord.data">Data</Translate>
                </Label>
                <AvField
                  id="job-record-data"
                  data-cy="data"
                  type="text"
                  name="data"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="stateLabel" for="job-record-state">
                  <Translate contentKey="venjureApp.jobRecord.state">State</Translate>
                </Label>
                <AvField
                  id="job-record-state"
                  data-cy="state"
                  type="text"
                  name="state"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="progressLabel" for="job-record-progress">
                  <Translate contentKey="venjureApp.jobRecord.progress">Progress</Translate>
                </Label>
                <AvField
                  id="job-record-progress"
                  data-cy="progress"
                  type="string"
                  className="form-control"
                  name="progress"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="resultLabel" for="job-record-result">
                  <Translate contentKey="venjureApp.jobRecord.result">Result</Translate>
                </Label>
                <AvField
                  id="job-record-result"
                  data-cy="result"
                  type="text"
                  name="result"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="errorLabel" for="job-record-error">
                  <Translate contentKey="venjureApp.jobRecord.error">Error</Translate>
                </Label>
                <AvField
                  id="job-record-error"
                  data-cy="error"
                  type="text"
                  name="error"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="startedatLabel" for="job-record-startedat">
                  <Translate contentKey="venjureApp.jobRecord.startedat">Startedat</Translate>
                </Label>
                <AvInput
                  id="job-record-startedat"
                  data-cy="startedat"
                  type="datetime-local"
                  className="form-control"
                  name="startedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.jobRecordEntity.startedat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="settledatLabel" for="job-record-settledat">
                  <Translate contentKey="venjureApp.jobRecord.settledat">Settledat</Translate>
                </Label>
                <AvInput
                  id="job-record-settledat"
                  data-cy="settledat"
                  type="datetime-local"
                  className="form-control"
                  name="settledat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.jobRecordEntity.settledat)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="issettledLabel">
                  <AvInput id="job-record-issettled" data-cy="issettled" type="checkbox" className="form-check-input" name="issettled" />
                  <Translate contentKey="venjureApp.jobRecord.issettled">Issettled</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="retriesLabel" for="job-record-retries">
                  <Translate contentKey="venjureApp.jobRecord.retries">Retries</Translate>
                </Label>
                <AvField
                  id="job-record-retries"
                  data-cy="retries"
                  type="string"
                  className="form-control"
                  name="retries"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="attemptsLabel" for="job-record-attempts">
                  <Translate contentKey="venjureApp.jobRecord.attempts">Attempts</Translate>
                </Label>
                <AvField
                  id="job-record-attempts"
                  data-cy="attempts"
                  type="string"
                  className="form-control"
                  name="attempts"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/job-record" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  jobRecordEntity: storeState.jobRecord.entity,
  loading: storeState.jobRecord.loading,
  updating: storeState.jobRecord.updating,
  updateSuccess: storeState.jobRecord.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(JobRecordUpdate);
