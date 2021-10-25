import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFacetValue } from 'app/shared/model/facet-value.model';
import { getEntities as getFacetValues } from 'app/entities/facet-value/facet-value.reducer';
import { getEntity, updateEntity, createEntity, reset } from './facet-value-translation.reducer';
import { IFacetValueTranslation } from 'app/shared/model/facet-value-translation.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFacetValueTranslationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacetValueTranslationUpdate = (props: IFacetValueTranslationUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { facetValueTranslationEntity, facetValues, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/facet-value-translation' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getFacetValues();
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
        ...facetValueTranslationEntity,
        ...values,
        base: facetValues.find(it => it.id.toString() === values.baseId.toString()),
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
          <h2 id="venjureApp.facetValueTranslation.home.createOrEditLabel" data-cy="FacetValueTranslationCreateUpdateHeading">
            <Translate contentKey="venjureApp.facetValueTranslation.home.createOrEditLabel">
              Create or edit a FacetValueTranslation
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : facetValueTranslationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="facet-value-translation-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="facet-value-translation-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="facet-value-translation-createdat">
                  <Translate contentKey="venjureApp.facetValueTranslation.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="facet-value-translation-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.facetValueTranslationEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="facet-value-translation-updatedat">
                  <Translate contentKey="venjureApp.facetValueTranslation.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="facet-value-translation-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.facetValueTranslationEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="languagecodeLabel" for="facet-value-translation-languagecode">
                  <Translate contentKey="venjureApp.facetValueTranslation.languagecode">Languagecode</Translate>
                </Label>
                <AvField
                  id="facet-value-translation-languagecode"
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
                <Label id="nameLabel" for="facet-value-translation-name">
                  <Translate contentKey="venjureApp.facetValueTranslation.name">Name</Translate>
                </Label>
                <AvField
                  id="facet-value-translation-name"
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
                <Label for="facet-value-translation-base">
                  <Translate contentKey="venjureApp.facetValueTranslation.base">Base</Translate>
                </Label>
                <AvInput id="facet-value-translation-base" data-cy="base" type="select" className="form-control" name="baseId">
                  <option value="" key="0" />
                  {facetValues
                    ? facetValues.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/facet-value-translation" replace color="info">
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
  facetValues: storeState.facetValue.entities,
  facetValueTranslationEntity: storeState.facetValueTranslation.entity,
  loading: storeState.facetValueTranslation.loading,
  updating: storeState.facetValueTranslation.updating,
  updateSuccess: storeState.facetValueTranslation.updateSuccess,
});

const mapDispatchToProps = {
  getFacetValues,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacetValueTranslationUpdate);
