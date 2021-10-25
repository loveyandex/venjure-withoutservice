import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './global-settings.reducer';
import { IGlobalSettings } from 'app/shared/model/global-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGlobalSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GlobalSettingsUpdate = (props: IGlobalSettingsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { globalSettingsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/global-settings' + props.location.search);
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

    if (errors.length === 0) {
      const entity = {
        ...globalSettingsEntity,
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
          <h2 id="venjureApp.globalSettings.home.createOrEditLabel" data-cy="GlobalSettingsCreateUpdateHeading">
            <Translate contentKey="venjureApp.globalSettings.home.createOrEditLabel">Create or edit a GlobalSettings</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : globalSettingsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="global-settings-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="global-settings-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="global-settings-createdat">
                  <Translate contentKey="venjureApp.globalSettings.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="global-settings-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.globalSettingsEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="global-settings-updatedat">
                  <Translate contentKey="venjureApp.globalSettings.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="global-settings-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.globalSettingsEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="availablelanguagesLabel" for="global-settings-availablelanguages">
                  <Translate contentKey="venjureApp.globalSettings.availablelanguages">Availablelanguages</Translate>
                </Label>
                <AvField
                  id="global-settings-availablelanguages"
                  data-cy="availablelanguages"
                  type="text"
                  name="availablelanguages"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="trackinventoryLabel">
                  <AvInput
                    id="global-settings-trackinventory"
                    data-cy="trackinventory"
                    type="checkbox"
                    className="form-check-input"
                    name="trackinventory"
                  />
                  <Translate contentKey="venjureApp.globalSettings.trackinventory">Trackinventory</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="outofstockthresholdLabel" for="global-settings-outofstockthreshold">
                  <Translate contentKey="venjureApp.globalSettings.outofstockthreshold">Outofstockthreshold</Translate>
                </Label>
                <AvField
                  id="global-settings-outofstockthreshold"
                  data-cy="outofstockthreshold"
                  type="string"
                  className="form-control"
                  name="outofstockthreshold"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/global-settings" replace color="info">
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
  globalSettingsEntity: storeState.globalSettings.entity,
  loading: storeState.globalSettings.loading,
  updating: storeState.globalSettings.updating,
  updateSuccess: storeState.globalSettings.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GlobalSettingsUpdate);
