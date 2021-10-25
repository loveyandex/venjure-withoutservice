import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product-option-group.reducer';
import { IProductOptionGroup } from 'app/shared/model/product-option-group.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductOptionGroupUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductOptionGroupUpdate = (props: IProductOptionGroupUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { productOptionGroupEntity, products, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/product-option-group' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getProducts();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdat = convertDateTimeToServer(values.createdat);
    values.updatedat = convertDateTimeToServer(values.updatedat);
    values.deletedat = convertDateTimeToServer(values.deletedat);

    if (errors.length === 0) {
      const entity = {
        ...productOptionGroupEntity,
        ...values,
        product: products.find(it => it.id.toString() === values.productId.toString()),
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
          <h2 id="venjureApp.productOptionGroup.home.createOrEditLabel" data-cy="ProductOptionGroupCreateUpdateHeading">
            <Translate contentKey="venjureApp.productOptionGroup.home.createOrEditLabel">Create or edit a ProductOptionGroup</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productOptionGroupEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-option-group-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="product-option-group-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="product-option-group-createdat">
                  <Translate contentKey="venjureApp.productOptionGroup.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="product-option-group-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productOptionGroupEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="product-option-group-updatedat">
                  <Translate contentKey="venjureApp.productOptionGroup.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="product-option-group-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productOptionGroupEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="deletedatLabel" for="product-option-group-deletedat">
                  <Translate contentKey="venjureApp.productOptionGroup.deletedat">Deletedat</Translate>
                </Label>
                <AvInput
                  id="product-option-group-deletedat"
                  data-cy="deletedat"
                  type="datetime-local"
                  className="form-control"
                  name="deletedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productOptionGroupEntity.deletedat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="codeLabel" for="product-option-group-code">
                  <Translate contentKey="venjureApp.productOptionGroup.code">Code</Translate>
                </Label>
                <AvField
                  id="product-option-group-code"
                  data-cy="code"
                  type="text"
                  name="code"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="product-option-group-product">
                  <Translate contentKey="venjureApp.productOptionGroup.product">Product</Translate>
                </Label>
                <AvInput id="product-option-group-product" data-cy="product" type="select" className="form-control" name="productId">
                  <option value="" key="0" />
                  {products
                    ? products.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/product-option-group" replace color="info">
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
  products: storeState.product.entities,
  productOptionGroupEntity: storeState.productOptionGroup.entity,
  loading: storeState.productOptionGroup.loading,
  updating: storeState.productOptionGroup.updating,
  updateSuccess: storeState.productOptionGroup.updateSuccess,
});

const mapDispatchToProps = {
  getProducts,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductOptionGroupUpdate);
