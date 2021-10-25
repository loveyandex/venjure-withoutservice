import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShippingMethod } from 'app/shared/model/shipping-method.model';
import { getEntities as getShippingMethods } from 'app/entities/shipping-method/shipping-method.reducer';
import { getEntity, updateEntity, createEntity, reset } from './shipping-method-translation.reducer';
import { IShippingMethodTranslation } from 'app/shared/model/shipping-method-translation.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShippingMethodTranslationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShippingMethodTranslationUpdate = (props: IShippingMethodTranslationUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { shippingMethodTranslationEntity, shippingMethods, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/shipping-method-translation' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getShippingMethods();
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
        ...shippingMethodTranslationEntity,
        ...values,
        base: shippingMethods.find(it => it.id.toString() === values.baseId.toString()),
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
          <h2 id="venjureApp.shippingMethodTranslation.home.createOrEditLabel" data-cy="ShippingMethodTranslationCreateUpdateHeading">
            <Translate contentKey="venjureApp.shippingMethodTranslation.home.createOrEditLabel">
              Create or edit a ShippingMethodTranslation
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : shippingMethodTranslationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="shipping-method-translation-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="shipping-method-translation-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="shipping-method-translation-createdat">
                  <Translate contentKey="venjureApp.shippingMethodTranslation.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="shipping-method-translation-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.shippingMethodTranslationEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="shipping-method-translation-updatedat">
                  <Translate contentKey="venjureApp.shippingMethodTranslation.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="shipping-method-translation-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.shippingMethodTranslationEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="languagecodeLabel" for="shipping-method-translation-languagecode">
                  <Translate contentKey="venjureApp.shippingMethodTranslation.languagecode">Languagecode</Translate>
                </Label>
                <AvField
                  id="shipping-method-translation-languagecode"
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
                <Label id="nameLabel" for="shipping-method-translation-name">
                  <Translate contentKey="venjureApp.shippingMethodTranslation.name">Name</Translate>
                </Label>
                <AvField
                  id="shipping-method-translation-name"
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
                <Label id="descriptionLabel" for="shipping-method-translation-description">
                  <Translate contentKey="venjureApp.shippingMethodTranslation.description">Description</Translate>
                </Label>
                <AvField
                  id="shipping-method-translation-description"
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
                <Label for="shipping-method-translation-base">
                  <Translate contentKey="venjureApp.shippingMethodTranslation.base">Base</Translate>
                </Label>
                <AvInput id="shipping-method-translation-base" data-cy="base" type="select" className="form-control" name="baseId">
                  <option value="" key="0" />
                  {shippingMethods
                    ? shippingMethods.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/shipping-method-translation" replace color="info">
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
  shippingMethods: storeState.shippingMethod.entities,
  shippingMethodTranslationEntity: storeState.shippingMethodTranslation.entity,
  loading: storeState.shippingMethodTranslation.loading,
  updating: storeState.shippingMethodTranslation.updating,
  updateSuccess: storeState.shippingMethodTranslation.updateSuccess,
});

const mapDispatchToProps = {
  getShippingMethods,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShippingMethodTranslationUpdate);
