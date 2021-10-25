import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICollection } from 'app/shared/model/collection.model';
import { getEntities as getCollections } from 'app/entities/collection/collection.reducer';
import { getEntity, updateEntity, createEntity, reset } from './collection-translation.reducer';
import { ICollectionTranslation } from 'app/shared/model/collection-translation.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICollectionTranslationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CollectionTranslationUpdate = (props: ICollectionTranslationUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { collectionTranslationEntity, collections, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/collection-translation' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

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
        ...collectionTranslationEntity,
        ...values,
        base: collections.find(it => it.id.toString() === values.baseId.toString()),
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
          <h2 id="venjureApp.collectionTranslation.home.createOrEditLabel" data-cy="CollectionTranslationCreateUpdateHeading">
            <Translate contentKey="venjureApp.collectionTranslation.home.createOrEditLabel">
              Create or edit a CollectionTranslation
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : collectionTranslationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="collection-translation-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="collection-translation-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="collection-translation-createdat">
                  <Translate contentKey="venjureApp.collectionTranslation.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="collection-translation-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.collectionTranslationEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="collection-translation-updatedat">
                  <Translate contentKey="venjureApp.collectionTranslation.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="collection-translation-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.collectionTranslationEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="languagecodeLabel" for="collection-translation-languagecode">
                  <Translate contentKey="venjureApp.collectionTranslation.languagecode">Languagecode</Translate>
                </Label>
                <AvField
                  id="collection-translation-languagecode"
                  data-cy="languagecode"
                  type="text"
                  name="languagecode"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="collection-translation-name">
                  <Translate contentKey="venjureApp.collectionTranslation.name">Name</Translate>
                </Label>
                <AvField
                  id="collection-translation-name"
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
                <Label id="slugLabel" for="collection-translation-slug">
                  <Translate contentKey="venjureApp.collectionTranslation.slug">Slug</Translate>
                </Label>
                <AvField
                  id="collection-translation-slug"
                  data-cy="slug"
                  type="text"
                  name="slug"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="collection-translation-description">
                  <Translate contentKey="venjureApp.collectionTranslation.description">Description</Translate>
                </Label>
                <AvField
                  id="collection-translation-description"
                  data-cy="description"
                  type="text"
                  name="description"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="collection-translation-base">
                  <Translate contentKey="venjureApp.collectionTranslation.base">Base</Translate>
                </Label>
                <AvInput id="collection-translation-base" data-cy="base" type="select" className="form-control" name="baseId">
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
              <Button tag={Link} id="cancel-save" to="/collection-translation" replace color="info">
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
  collections: storeState.collection.entities,
  collectionTranslationEntity: storeState.collectionTranslation.entity,
  loading: storeState.collectionTranslation.loading,
  updating: storeState.collectionTranslation.updating,
  updateSuccess: storeState.collectionTranslation.updateSuccess,
});

const mapDispatchToProps = {
  getCollections,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CollectionTranslationUpdate);
