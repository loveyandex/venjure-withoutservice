import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-asset.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductAssetDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductAssetDetail = (props: IProductAssetDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productAssetEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productAssetDetailsHeading">
          <Translate contentKey="venjureApp.productAsset.detail.title">ProductAsset</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productAssetEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.productAsset.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {productAssetEntity.createdat ? <TextFormat value={productAssetEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.productAsset.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {productAssetEntity.updatedat ? <TextFormat value={productAssetEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="position">
              <Translate contentKey="venjureApp.productAsset.position">Position</Translate>
            </span>
          </dt>
          <dd>{productAssetEntity.position}</dd>
          <dt>
            <Translate contentKey="venjureApp.productAsset.asset">Asset</Translate>
          </dt>
          <dd>{productAssetEntity.asset ? productAssetEntity.asset.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.productAsset.product">Product</Translate>
          </dt>
          <dd>{productAssetEntity.product ? productAssetEntity.product.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-asset" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-asset/${productAssetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productAsset }: IRootState) => ({
  productAssetEntity: productAsset.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductAssetDetail);
