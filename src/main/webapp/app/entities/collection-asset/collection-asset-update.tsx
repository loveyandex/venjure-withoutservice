import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAsset } from 'app/shared/model/asset.model';
import { getEntities as getAssets } from 'app/entities/asset/asset.reducer';
import { ICollection } from 'app/shared/model/collection.model';
import { getEntities as getCollections } from 'app/entities/collection/collection.reducer';
import { getEntity, updateEntity, createEntity, reset } from './collection-asset.reducer';
import { ICollectionAsset } from 'app/shared/model/collection-asset.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICollectionAssetUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CollectionAssetUpdate = (props: ICollectionAssetUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { collectionAssetEntity, assets, collections, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/collection-asset' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAssets();
    props.getCollections();
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
        ...collectionAssetEntity,
        ...values,
        asset: assets.find(it => it.id.toString() === values.assetId.toString()),
        collection: collections.find(it => it.id.toString() === values.collectionId.toString()),
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
          <h2 id="venjureApp.collectionAsset.home.createOrEditLabel" data-cy="CollectionAssetCreateUpdateHeading">
            <Translate contentKey="venjureApp.collectionAsset.home.createOrEditLabel">Create or edit a CollectionAsset</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : collectionAssetEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="collection-asset-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="collection-asset-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="collection-asset-createdat">
                  <Translate contentKey="venjureApp.collectionAsset.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="collection-asset-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.collectionAssetEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="collection-asset-updatedat">
                  <Translate contentKey="venjureApp.collectionAsset.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="collection-asset-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.collectionAssetEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="positionLabel" for="collection-asset-position">
                  <Translate contentKey="venjureApp.collectionAsset.position">Position</Translate>
                </Label>
                <AvField
                  id="collection-asset-position"
                  data-cy="position"
                  type="string"
                  className="form-control"
                  name="position"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="collection-asset-asset">
                  <Translate contentKey="venjureApp.collectionAsset.asset">Asset</Translate>
                </Label>
                <AvInput id="collection-asset-asset" data-cy="asset" type="select" className="form-control" name="assetId" required>
                  <option value="" key="0" />
                  {assets
                    ? assets.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="collection-asset-collection">
                  <Translate contentKey="venjureApp.collectionAsset.collection">Collection</Translate>
                </Label>
                <AvInput
                  id="collection-asset-collection"
                  data-cy="collection"
                  type="select"
                  className="form-control"
                  name="collectionId"
                  required
                >
                  <option value="" key="0" />
                  {collections
                    ? collections.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/collection-asset" replace color="info">
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
  assets: storeState.asset.entities,
  collections: storeState.collection.entities,
  collectionAssetEntity: storeState.collectionAsset.entity,
  loading: storeState.collectionAsset.loading,
  updating: storeState.collectionAsset.updating,
  updateSuccess: storeState.collectionAsset.updateSuccess,
});

const mapDispatchToProps = {
  getAssets,
  getCollections,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CollectionAssetUpdate);
