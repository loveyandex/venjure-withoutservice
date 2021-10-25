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
import { getEntities as getCollections } from 'app/entities/collection/collection.reducer';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { getEntities as getProductVariants } from 'app/entities/product-variant/product-variant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './collection.reducer';
import { ICollection } from 'app/shared/model/collection.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICollectionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CollectionUpdate = (props: ICollectionUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { collectionEntity, assets, collections, productVariants, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/collection' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAssets();
    props.getCollections();
    props.getProductVariants();
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
        ...collectionEntity,
        ...values,
        featuredasset: assets.find(it => it.id.toString() === values.featuredassetId.toString()),
        parent: collections.find(it => it.id.toString() === values.parentId.toString()),
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
          <h2 id="venjureApp.collection.home.createOrEditLabel" data-cy="CollectionCreateUpdateHeading">
            <Translate contentKey="venjureApp.collection.home.createOrEditLabel">Create or edit a Collection</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : collectionEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="collection-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="collection-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="collection-createdat">
                  <Translate contentKey="venjureApp.collection.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="collection-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.collectionEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="collection-updatedat">
                  <Translate contentKey="venjureApp.collection.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="collection-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.collectionEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="isrootLabel">
                  <AvInput id="collection-isroot" data-cy="isroot" type="checkbox" className="form-check-input" name="isroot" />
                  <Translate contentKey="venjureApp.collection.isroot">Isroot</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="positionLabel" for="collection-position">
                  <Translate contentKey="venjureApp.collection.position">Position</Translate>
                </Label>
                <AvField
                  id="collection-position"
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
              <AvGroup check>
                <Label id="isprivateLabel">
                  <AvInput id="collection-isprivate" data-cy="isprivate" type="checkbox" className="form-check-input" name="isprivate" />
                  <Translate contentKey="venjureApp.collection.isprivate">Isprivate</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="filtersLabel" for="collection-filters">
                  <Translate contentKey="venjureApp.collection.filters">Filters</Translate>
                </Label>
                <AvField
                  id="collection-filters"
                  data-cy="filters"
                  type="text"
                  name="filters"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="collection-featuredasset">
                  <Translate contentKey="venjureApp.collection.featuredasset">Featuredasset</Translate>
                </Label>
                <AvInput
                  id="collection-featuredasset"
                  data-cy="featuredasset"
                  type="select"
                  className="form-control"
                  name="featuredassetId"
                >
                  <option value="" key="0" />
                  {assets
                    ? assets.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="collection-parent">
                  <Translate contentKey="venjureApp.collection.parent">Parent</Translate>
                </Label>
                <AvInput id="collection-parent" data-cy="parent" type="select" className="form-control" name="parentId">
                  <option value="" key="0" />
                  {collections
                    ? collections.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/collection" replace color="info">
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
  productVariants: storeState.productVariant.entities,
  collectionEntity: storeState.collection.entity,
  loading: storeState.collection.loading,
  updating: storeState.collection.updating,
  updateSuccess: storeState.collection.updateSuccess,
});

const mapDispatchToProps = {
  getAssets,
  getCollections,
  getProductVariants,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CollectionUpdate);
