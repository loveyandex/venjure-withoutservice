import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProductOptionGroup } from 'app/shared/model/product-option-group.model';
import { getEntities as getProductOptionGroups } from 'app/entities/product-option-group/product-option-group.reducer';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { getEntities as getProductVariants } from 'app/entities/product-variant/product-variant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product-option.reducer';
import { IProductOption } from 'app/shared/model/product-option.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductOptionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductOptionUpdate = (props: IProductOptionUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { productOptionEntity, productOptionGroups, productVariants, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/product-option' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getProductOptionGroups();
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
    values.deletedat = convertDateTimeToServer(values.deletedat);

    if (errors.length === 0) {
      const entity = {
        ...productOptionEntity,
        ...values,
        group: productOptionGroups.find(it => it.id.toString() === values.groupId.toString()),
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
          <h2 id="venjureApp.productOption.home.createOrEditLabel" data-cy="ProductOptionCreateUpdateHeading">
            <Translate contentKey="venjureApp.productOption.home.createOrEditLabel">Create or edit a ProductOption</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : productOptionEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="product-option-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="product-option-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="product-option-createdat">
                  <Translate contentKey="venjureApp.productOption.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="product-option-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productOptionEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="product-option-updatedat">
                  <Translate contentKey="venjureApp.productOption.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="product-option-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productOptionEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="deletedatLabel" for="product-option-deletedat">
                  <Translate contentKey="venjureApp.productOption.deletedat">Deletedat</Translate>
                </Label>
                <AvInput
                  id="product-option-deletedat"
                  data-cy="deletedat"
                  type="datetime-local"
                  className="form-control"
                  name="deletedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.productOptionEntity.deletedat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="codeLabel" for="product-option-code">
                  <Translate contentKey="venjureApp.productOption.code">Code</Translate>
                </Label>
                <AvField
                  id="product-option-code"
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
                <Label for="product-option-group">
                  <Translate contentKey="venjureApp.productOption.group">Group</Translate>
                </Label>
                <AvInput id="product-option-group" data-cy="group" type="select" className="form-control" name="groupId" required>
                  <option value="" key="0" />
                  {productOptionGroups
                    ? productOptionGroups.map(otherEntity => (
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
              <Button tag={Link} id="cancel-save" to="/product-option" replace color="info">
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
  productOptionGroups: storeState.productOptionGroup.entities,
  productVariants: storeState.productVariant.entities,
  productOptionEntity: storeState.productOption.entity,
  loading: storeState.productOption.loading,
  updating: storeState.productOption.updating,
  updateSuccess: storeState.productOption.updateSuccess,
});

const mapDispatchToProps = {
  getProductOptionGroups,
  getProductVariants,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductOptionUpdate);
