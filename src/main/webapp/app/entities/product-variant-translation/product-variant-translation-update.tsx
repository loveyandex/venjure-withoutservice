import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProductVariant } from 'app/shared/model/product-variant.model';
import { getEntities as getProductVariants } from 'app/entities/product-variant/product-variant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product-variant-translation.reducer';
import { IProductVariantTranslation } from 'app/shared/model/product-variant-translation.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductVariantTranslationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductVariantTranslationUpdate = (props: IProductVariantTranslationUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { productVariantTranslationEntity, productVariants, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/product-variant-translation' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

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
        ...productVariantTranslationEntity,
        ...values,
        base: productVariants.find(it => it.id.toString() === values.baseId.toString()),
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
          <h2 id="venjureApp.productVariantTranslation.home.createOrEditLabel" data-cy="ProductVariantTranslationCreateUpdateHeading">
            <Translate contentKey="venjureApp.productVariantTranslation.home.createOrEditLabel">
              Create or edit a ProductVariantTranslation
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productVariantTranslationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-variant-translation-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="product-variant-translation-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="product-variant-translation-createdat">
                  <Translate contentKey="venjureApp.productVariantTranslation.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="product-variant-translation-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productVariantTranslationEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="product-variant-translation-updatedat">
                  <Translate contentKey="venjureApp.productVariantTranslation.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="product-variant-translation-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productVariantTranslationEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="languagecodeLabel" for="product-variant-translation-languagecode">
                  <Translate contentKey="venjureApp.productVariantTranslation.languagecode">Languagecode</Translate>
                </Label>
                <AvField
                  id="product-variant-translation-languagecode"
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
                <Label id="nameLabel" for="product-variant-translation-name">
                  <Translate contentKey="venjureApp.productVariantTranslation.name">Name</Translate>
                </Label>
                <AvField
                  id="product-variant-translation-name"
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
                <Label for="product-variant-translation-base">
                  <Translate contentKey="venjureApp.productVariantTranslation.base">Base</Translate>
                </Label>
                <AvInput id="product-variant-translation-base" data-cy="base" type="select" className="form-control" name="baseId">
                  <option value="" key="0" />
                  {productVariants
                    ? productVariants.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/product-variant-translation" replace color="info">
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
  productVariants: storeState.productVariant.entities,
  productVariantTranslationEntity: storeState.productVariantTranslation.entity,
  loading: storeState.productVariantTranslation.loading,
  updating: storeState.productVariantTranslation.updating,
  updateSuccess: storeState.productVariantTranslation.updateSuccess,
});

const mapDispatchToProps = {
  getProductVariants,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantTranslationUpdate);
