import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-variant.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductVariantDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductVariantDetail = (props: IProductVariantDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productVariantEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productVariantDetailsHeading">
          <Translate contentKey="venjureApp.productVariant.detail.title">ProductVariant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.productVariant.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {productVariantEntity.createdat ? (
              <TextFormat value={productVariantEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.productVariant.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {productVariantEntity.updatedat ? (
              <TextFormat value={productVariantEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="deletedat">
              <Translate contentKey="venjureApp.productVariant.deletedat">Deletedat</Translate>
            </span>
          </dt>
          <dd>
            {productVariantEntity.deletedat ? (
              <TextFormat value={productVariantEntity.deletedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="enabled">
              <Translate contentKey="venjureApp.productVariant.enabled">Enabled</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.enabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="sku">
              <Translate contentKey="venjureApp.productVariant.sku">Sku</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.sku}</dd>
          <dt>
            <span id="stockonhand">
              <Translate contentKey="venjureApp.productVariant.stockonhand">Stockonhand</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.stockonhand}</dd>
          <dt>
            <span id="stockallocated">
              <Translate contentKey="venjureApp.productVariant.stockallocated">Stockallocated</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.stockallocated}</dd>
          <dt>
            <span id="outofstockthreshold">
              <Translate contentKey="venjureApp.productVariant.outofstockthreshold">Outofstockthreshold</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.outofstockthreshold}</dd>
          <dt>
            <span id="useglobaloutofstockthreshold">
              <Translate contentKey="venjureApp.productVariant.useglobaloutofstockthreshold">Useglobaloutofstockthreshold</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.useglobaloutofstockthreshold ? 'true' : 'false'}</dd>
          <dt>
            <span id="trackinventory">
              <Translate contentKey="venjureApp.productVariant.trackinventory">Trackinventory</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.trackinventory}</dd>
          <dt>
            <Translate contentKey="venjureApp.productVariant.product">Product</Translate>
          </dt>
          <dd>{productVariantEntity.product ? productVariantEntity.product.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.productVariant.featuredasset">Featuredasset</Translate>
          </dt>
          <dd>{productVariantEntity.featuredasset ? productVariantEntity.featuredasset.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.productVariant.taxcategory">Taxcategory</Translate>
          </dt>
          <dd>{productVariantEntity.taxcategory ? productVariantEntity.taxcategory.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.productVariant.channel">Channel</Translate>
          </dt>
          <dd>
            {productVariantEntity.channels
              ? productVariantEntity.channels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {productVariantEntity.channels && i === productVariantEntity.channels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.productVariant.productVariants">Product Variants</Translate>
          </dt>
          <dd>
            {productVariantEntity.productVariants
              ? productVariantEntity.productVariants.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {productVariantEntity.productVariants && i === productVariantEntity.productVariants.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.productVariant.facetValue">Facet Value</Translate>
          </dt>
          <dd>
            {productVariantEntity.facetValues
              ? productVariantEntity.facetValues.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {productVariantEntity.facetValues && i === productVariantEntity.facetValues.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.productVariant.productOption">Product Option</Translate>
          </dt>
          <dd>
            {productVariantEntity.productOptions
              ? productVariantEntity.productOptions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {productVariantEntity.productOptions && i === productVariantEntity.productOptions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/product-variant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-variant/${productVariantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productVariant }: IRootState) => ({
  productVariantEntity: productVariant.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantDetail);
