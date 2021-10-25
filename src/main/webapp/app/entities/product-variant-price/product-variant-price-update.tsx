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
import { getEntity, updateEntity, createEntity, reset } from './product-variant-price.reducer';
import { IProductVariantPrice } from 'app/shared/model/product-variant-price.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductVariantPriceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductVariantPriceUpdate = (props: IProductVariantPriceUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { productVariantPriceEntity, productVariants, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/product-variant-price' + props.location.search);
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
        ...productVariantPriceEntity,
        ...values,
        variant: productVariants.find(it => it.id.toString() === values.variantId.toString()),
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
          <h2 id="venjureApp.productVariantPrice.home.createOrEditLabel" data-cy="ProductVariantPriceCreateUpdateHeading">
            <Translate contentKey="venjureApp.productVariantPrice.home.createOrEditLabel">Create or edit a ProductVariantPrice</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productVariantPriceEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-variant-price-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="product-variant-price-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="product-variant-price-createdat">
                  <Translate contentKey="venjureApp.productVariantPrice.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="product-variant-price-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productVariantPriceEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="product-variant-price-updatedat">
                  <Translate contentKey="venjureApp.productVariantPrice.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="product-variant-price-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productVariantPriceEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="product-variant-price-price">
                  <Translate contentKey="venjureApp.productVariantPrice.price">Price</Translate>
                </Label>
                <AvField
                  id="product-variant-price-price"
                  data-cy="price"
                  type="string"
                  className="form-control"
                  name="price"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="channelidLabel" for="product-variant-price-channelid">
                  <Translate contentKey="venjureApp.productVariantPrice.channelid">Channelid</Translate>
                </Label>
                <AvField
                  id="product-variant-price-channelid"
                  data-cy="channelid"
                  type="string"
                  className="form-control"
                  name="channelid"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="product-variant-price-variant">
                  <Translate contentKey="venjureApp.productVariantPrice.variant">Variant</Translate>
                </Label>
                <AvInput id="product-variant-price-variant" data-cy="variant" type="select" className="form-control" name="variantId">
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
              <Button tag={Link} id="cancel-save" to="/product-variant-price" replace color="info">
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
  productVariantPriceEntity: storeState.productVariantPrice.entity,
  loading: storeState.productVariantPrice.loading,
  updating: storeState.productVariantPrice.updating,
  updateSuccess: storeState.productVariantPrice.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantPriceUpdate);
