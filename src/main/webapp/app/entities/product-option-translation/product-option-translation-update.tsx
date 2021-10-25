import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProductOption } from 'app/shared/model/product-option.model';
import { getEntities as getProductOptions } from 'app/entities/product-option/product-option.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product-option-translation.reducer';
import { IProductOptionTranslation } from 'app/shared/model/product-option-translation.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductOptionTranslationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductOptionTranslationUpdate = (props: IProductOptionTranslationUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { productOptionTranslationEntity, productOptions, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/product-option-translation' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getProductOptions();
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
        ...productOptionTranslationEntity,
        ...values,
        base: productOptions.find(it => it.id.toString() === values.baseId.toString()),
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
          <h2 id="venjureApp.productOptionTranslation.home.createOrEditLabel" data-cy="ProductOptionTranslationCreateUpdateHeading">
            <Translate contentKey="venjureApp.productOptionTranslation.home.createOrEditLabel">
              Create or edit a ProductOptionTranslation
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productOptionTranslationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-option-translation-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="product-option-translation-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="product-option-translation-createdat">
                  <Translate contentKey="venjureApp.productOptionTranslation.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="product-option-translation-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productOptionTranslationEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="product-option-translation-updatedat">
                  <Translate contentKey="venjureApp.productOptionTranslation.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="product-option-translation-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productOptionTranslationEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="languagecodeLabel" for="product-option-translation-languagecode">
                  <Translate contentKey="venjureApp.productOptionTranslation.languagecode">Languagecode</Translate>
                </Label>
                <AvField
                  id="product-option-translation-languagecode"
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
                <Label id="nameLabel" for="product-option-translation-name">
                  <Translate contentKey="venjureApp.productOptionTranslation.name">Name</Translate>
                </Label>
                <AvField
                  id="product-option-translation-name"
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
                <Label for="product-option-translation-base">
                  <Translate contentKey="venjureApp.productOptionTranslation.base">Base</Translate>
                </Label>
                <AvInput id="product-option-translation-base" data-cy="base" type="select" className="form-control" name="baseId">
                  <option value="" key="0" />
                  {productOptions
                    ? productOptions.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/product-option-translation" replace color="info">
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
  productOptions: storeState.productOption.entities,
  productOptionTranslationEntity: storeState.productOptionTranslation.entity,
  loading: storeState.productOptionTranslation.loading,
  updating: storeState.productOptionTranslation.updating,
  updateSuccess: storeState.productOptionTranslation.updateSuccess,
});

const mapDispatchToProps = {
  getProductOptions,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductOptionTranslationUpdate);
