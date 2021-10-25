import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-variant-price.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductVariantPriceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductVariantPriceDetail = (props: IProductVariantPriceDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productVariantPriceEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productVariantPriceDetailsHeading">
          <Translate contentKey="venjureApp.productVariantPrice.detail.title">ProductVariantPrice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productVariantPriceEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.productVariantPrice.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {productVariantPriceEntity.createdat ? (
              <TextFormat value={productVariantPriceEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.productVariantPrice.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {productVariantPriceEntity.updatedat ? (
              <TextFormat value={productVariantPriceEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="price">
              <Translate contentKey="venjureApp.productVariantPrice.price">Price</Translate>
            </span>
          </dt>
          <dd>{productVariantPriceEntity.price}</dd>
          <dt>
            <span id="channelid">
              <Translate contentKey="venjureApp.productVariantPrice.channelid">Channelid</Translate>
            </span>
          </dt>
          <dd>{productVariantPriceEntity.channelid}</dd>
          <dt>
            <Translate contentKey="venjureApp.productVariantPrice.variant">Variant</Translate>
          </dt>
          <dd>{productVariantPriceEntity.variant ? productVariantPriceEntity.variant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-variant-price" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-variant-price/${productVariantPriceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productVariantPrice }: IRootState) => ({
  productVariantPriceEntity: productVariantPrice.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantPriceDetail);
