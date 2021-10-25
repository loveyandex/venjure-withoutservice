import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFacet } from 'app/shared/model/facet.model';
import { getEntities as getFacets } from 'app/entities/facet/facet.reducer';
import { IChannel } from 'app/shared/model/channel.model';
import { getEntities as getChannels } from 'app/entities/channel/channel.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { getEntities as getProductVariants } from 'app/entities/product-variant/product-variant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './facet-value.reducer';
import { IFacetValue } from 'app/shared/model/facet-value.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFacetValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacetValueUpdate = (props: IFacetValueUpdateProps) => {
  const [idschannel, setIdschannel] = useState([]);
  const [idsproduct, setIdsproduct] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { facetValueEntity, facets, channels, products, productVariants, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/facet-value' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getFacets();
    props.getChannels();
    props.getProducts();
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
        ...facetValueEntity,
        ...values,
        channels: mapIdList(values.channels),
        products: mapIdList(values.products),
        facet: facets.find(it => it.id.toString() === values.facetId.toString()),
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
          <h2 id="venjureApp.facetValue.home.createOrEditLabel" data-cy="FacetValueCreateUpdateHeading">
            <Translate contentKey="venjureApp.facetValue.home.createOrEditLabel">Create or edit a FacetValue</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : facetValueEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="facet-value-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="facet-value-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="facet-value-createdat">
                  <Translate contentKey="venjureApp.facetValue.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="facet-value-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.facetValueEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="facet-value-updatedat">
                  <Translate contentKey="venjureApp.facetValue.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="facet-value-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.facetValueEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="codeLabel" for="facet-value-code">
                  <Translate contentKey="venjureApp.facetValue.code">Code</Translate>
                </Label>
                <AvField
                  id="facet-value-code"
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
                <Label for="facet-value-facet">
                  <Translate contentKey="venjureApp.facetValue.facet">Facet</Translate>
                </Label>
                <AvInput id="facet-value-facet" data-cy="facet" type="select" className="form-control" name="facetId">
                  <option value="" key="0" />
                  {facets
                    ? facets.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="facet-value-channel">
                  <Translate contentKey="venjureApp.facetValue.channel">Channel</Translate>
                </Label>
                <AvInput
                  id="facet-value-channel"
                  data-cy="channel"
                  type="select"
                  multiple
                  className="form-control"
                  name="channels"
                  value={!isNew && facetValueEntity.channels && facetValueEntity.channels.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {channels
                    ? channels.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="facet-value-product">
                  <Translate contentKey="venjureApp.facetValue.product">Product</Translate>
                </Label>
                <AvInput
                  id="facet-value-product"
                  data-cy="product"
                  type="select"
                  multiple
                  className="form-control"
                  name="products"
                  value={!isNew && facetValueEntity.products && facetValueEntity.products.map(e => e.id)}
                >
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
              <Button tag={Link} id="cancel-save" to="/facet-value" replace color="info">
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
  facets: storeState.facet.entities,
  channels: storeState.channel.entities,
  products: storeState.product.entities,
  productVariants: storeState.productVariant.entities,
  facetValueEntity: storeState.facetValue.entity,
  loading: storeState.facetValue.loading,
  updating: storeState.facetValue.updating,
  updateSuccess: storeState.facetValue.updateSuccess,
});

const mapDispatchToProps = {
  getFacets,
  getChannels,
  getProducts,
  getProductVariants,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacetValueUpdate);
