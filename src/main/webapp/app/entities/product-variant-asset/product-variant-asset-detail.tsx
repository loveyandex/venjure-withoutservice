import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-variant-asset.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductVariantAssetDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductVariantAssetDetail = (props: IProductVariantAssetDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productVariantAssetEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productVariantAssetDetailsHeading">
          <Translate contentKey="venjureApp.productVariantAsset.detail.title">ProductVariantAsset</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productVariantAssetEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.productVariantAsset.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {productVariantAssetEntity.createdat ? (
              <TextFormat value={productVariantAssetEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.productVariantAsset.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {productVariantAssetEntity.updatedat ? (
              <TextFormat value={productVariantAssetEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="position">
              <Translate contentKey="venjureApp.productVariantAsset.position">Position</Translate>
            </span>
          </dt>
          <dd>{productVariantAssetEntity.position}</dd>
          <dt>
            <Translate contentKey="venjureApp.productVariantAsset.asset">Asset</Translate>
          </dt>
          <dd>{productVariantAssetEntity.asset ? productVariantAssetEntity.asset.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.productVariantAsset.productvariant">Productvariant</Translate>
          </dt>
          <dd>{productVariantAssetEntity.productvariant ? productVariantAssetEntity.productvariant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-variant-asset" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-variant-asset/${productVariantAssetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productVariantAsset }: IRootState) => ({
  productVariantAssetEntity: productVariantAsset.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantAssetDetail);
