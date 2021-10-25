import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './asset.reducer';
import { IAsset } from 'app/shared/model/asset.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAssetUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AssetUpdate = (props: IAssetUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { assetEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/asset' + props.location.search);
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
        ...assetEntity,
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
          <h2 id="venjureApp.asset.home.createOrEditLabel" data-cy="AssetCreateUpdateHeading">
            <Translate contentKey="venjureApp.asset.home.createOrEditLabel">Create or edit a Asset</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : assetEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="asset-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="asset-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="asset-createdat">
                  <Translate contentKey="venjureApp.asset.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="asset-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.assetEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="asset-updatedat">
                  <Translate contentKey="venjureApp.asset.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="asset-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.assetEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="asset-name">
                  <Translate contentKey="venjureApp.asset.name">Name</Translate>
                </Label>
                <AvField
                  id="asset-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="asset-type">
                  <Translate contentKey="venjureApp.asset.type">Type</Translate>
                </Label>
                <AvField
                  id="asset-type"
                  data-cy="type"
                  type="text"
                  name="type"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="mimetypeLabel" for="asset-mimetype">
                  <Translate contentKey="venjureApp.asset.mimetype">Mimetype</Translate>
                </Label>
                <AvField
                  id="asset-mimetype"
                  data-cy="mimetype"
                  type="text"
                  name="mimetype"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="widthLabel" for="asset-width">
                  <Translate contentKey="venjureApp.asset.width">Width</Translate>
                </Label>
                <AvField
                  id="asset-width"
                  data-cy="width"
                  type="string"
                  className="form-control"
                  name="width"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="heightLabel" for="asset-height">
                  <Translate contentKey="venjureApp.asset.height">Height</Translate>
                </Label>
                <AvField
                  id="asset-height"
                  data-cy="height"
                  type="string"
                  className="form-control"
                  name="height"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="filesizeLabel" for="asset-filesize">
                  <Translate contentKey="venjureApp.asset.filesize">Filesize</Translate>
                </Label>
                <AvField
                  id="asset-filesize"
                  data-cy="filesize"
                  type="string"
                  className="form-control"
                  name="filesize"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="sourceLabel" for="asset-source">
                  <Translate contentKey="venjureApp.asset.source">Source</Translate>
                </Label>
                <AvField
                  id="asset-source"
                  data-cy="source"
                  type="text"
                  name="source"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="previewLabel" for="asset-preview">
                  <Translate contentKey="venjureApp.asset.preview">Preview</Translate>
                </Label>
                <AvField
                  id="asset-preview"
                  data-cy="preview"
                  type="text"
                  name="preview"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="focalpointLabel" for="asset-focalpoint">
                  <Translate contentKey="venjureApp.asset.focalpoint">Focalpoint</Translate>
                </Label>
                <AvField
                  id="asset-focalpoint"
                  data-cy="focalpoint"
                  type="text"
                  name="focalpoint"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/asset" replace color="info">
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
  assetEntity: storeState.asset.entity,
  loading: storeState.asset.loading,
  updating: storeState.asset.updating,
  updateSuccess: storeState.asset.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AssetUpdate);
