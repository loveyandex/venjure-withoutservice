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
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { getEntities as getProductVariants } from 'app/entities/product-variant/product-variant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product-variant-asset.reducer';
import { IProductVariantAsset } from 'app/shared/model/product-variant-asset.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductVariantAssetUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductVariantAssetUpdate = (props: IProductVariantAssetUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { productVariantAssetEntity, assets, productVariants, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/product-variant-asset' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAssets();
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
        ...productVariantAssetEntity,
        ...values,
        asset: assets.find(it => it.id.toString() === values.assetId.toString()),
        productvariant: productVariants.find(it => it.id.toString() === values.productvariantId.toString()),
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
          <h2 id="venjureApp.productVariantAsset.home.createOrEditLabel" data-cy="ProductVariantAssetCreateUpdateHeading">
            <Translate contentKey="venjureApp.productVariantAsset.home.createOrEditLabel">Create or edit a ProductVariantAsset</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productVariantAssetEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-variant-asset-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="product-variant-asset-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="product-variant-asset-createdat">
                  <Translate contentKey="venjureApp.productVariantAsset.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="product-variant-asset-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productVariantAssetEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="product-variant-asset-updatedat">
                  <Translate contentKey="venjureApp.productVariantAsset.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="product-variant-asset-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productVariantAssetEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="positionLabel" for="product-variant-asset-position">
                  <Translate contentKey="venjureApp.productVariantAsset.position">Position</Translate>
                </Label>
                <AvField
                  id="product-variant-asset-position"
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
                <Label for="product-variant-asset-asset">
                  <Translate contentKey="venjureApp.productVariantAsset.asset">Asset</Translate>
                </Label>
                <AvInput id="product-variant-asset-asset" data-cy="asset" type="select" className="form-control" name="assetId" required>
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
                <Label for="product-variant-asset-productvariant">
                  <Translate contentKey="venjureApp.productVariantAsset.productvariant">Productvariant</Translate>
                </Label>
                <AvInput
                  id="product-variant-asset-productvariant"
                  data-cy="productvariant"
                  type="select"
                  className="form-control"
                  name="productvariantId"
                  required
                >
                  <option value="" key="0" />
                  {productVariants
                    ? productVariants.map(otherEntity => (
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
              <Button tag={Link} id="cancel-save" to="/product-variant-asset" replace color="info">
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
  productVariants: storeState.productVariant.entities,
  productVariantAssetEntity: storeState.productVariantAsset.entity,
  loading: storeState.productVariantAsset.loading,
  updating: storeState.productVariantAsset.updating,
  updateSuccess: storeState.productVariantAsset.updateSuccess,
});

const mapDispatchToProps = {
  getAssets,
  getProductVariants,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantAssetUpdate);
