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
import { ITaxCategory } from 'app/shared/model/tax-category.model';
import { getEntities as getTaxCategories } from 'app/entities/tax-category/tax-category.reducer';
import { IAsset } from 'app/shared/model/asset.model';
import { getEntities as getAssets } from 'app/entities/asset/asset.reducer';
import { IJorder } from 'app/shared/model/jorder.model';
import { getEntities as getJorders } from 'app/entities/jorder/jorder.reducer';
import { getEntity, updateEntity, createEntity, reset } from './order-line.reducer';
import { IOrderLine } from 'app/shared/model/order-line.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrderLineUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderLineUpdate = (props: IOrderLineUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { orderLineEntity, productVariants, taxCategories, assets, jorders, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/order-line' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getProductVariants();
    props.getTaxCategories();
    props.getAssets();
    props.getJorders();
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
        ...orderLineEntity,
        ...values,
        productvariant: productVariants.find(it => it.id.toString() === values.productvariantId.toString()),
        taxcategory: taxCategories.find(it => it.id.toString() === values.taxcategoryId.toString()),
        featuredAsset: assets.find(it => it.id.toString() === values.featuredAssetId.toString()),
        jorder: jorders.find(it => it.id.toString() === values.jorderId.toString()),
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
          <h2 id="venjureApp.orderLine.home.createOrEditLabel" data-cy="OrderLineCreateUpdateHeading">
            <Translate contentKey="venjureApp.orderLine.home.createOrEditLabel">Create or edit a OrderLine</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : orderLineEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="order-line-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="order-line-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="order-line-createdat">
                  <Translate contentKey="venjureApp.orderLine.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="order-line-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.orderLineEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="order-line-updatedat">
                  <Translate contentKey="venjureApp.orderLine.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="order-line-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.orderLineEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="order-line-productvariant">
                  <Translate contentKey="venjureApp.orderLine.productvariant">Productvariant</Translate>
                </Label>
                <AvInput
                  id="order-line-productvariant"
                  data-cy="productvariant"
                  type="select"
                  className="form-control"
                  name="productvariantId"
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
              </AvGroup>
              <AvGroup>
                <Label for="order-line-taxcategory">
                  <Translate contentKey="venjureApp.orderLine.taxcategory">Taxcategory</Translate>
                </Label>
                <AvInput id="order-line-taxcategory" data-cy="taxcategory" type="select" className="form-control" name="taxcategoryId">
                  <option value="" key="0" />
                  {taxCategories
                    ? taxCategories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="order-line-featuredAsset">
                  <Translate contentKey="venjureApp.orderLine.featuredAsset">Featured Asset</Translate>
                </Label>
                <AvInput
                  id="order-line-featuredAsset"
                  data-cy="featuredAsset"
                  type="select"
                  className="form-control"
                  name="featuredAssetId"
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
                <Label for="order-line-jorder">
                  <Translate contentKey="venjureApp.orderLine.jorder">Jorder</Translate>
                </Label>
                <AvInput id="order-line-jorder" data-cy="jorder" type="select" className="form-control" name="jorderId">
                  <option value="" key="0" />
                  {jorders
                    ? jorders.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/order-line" replace color="info">
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
  taxCategories: storeState.taxCategory.entities,
  assets: storeState.asset.entities,
  jorders: storeState.jorder.entities,
  orderLineEntity: storeState.orderLine.entity,
  loading: storeState.orderLine.loading,
  updating: storeState.orderLine.updating,
  updateSuccess: storeState.orderLine.updateSuccess,
});

const mapDispatchToProps = {
  getProductVariants,
  getTaxCategories,
  getAssets,
  getJorders,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderLineUpdate);
