import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-variant-translation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductVariantTranslationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductVariantTranslationDetail = (props: IProductVariantTranslationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productVariantTranslationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productVariantTranslationDetailsHeading">
          <Translate contentKey="venjureApp.productVariantTranslation.detail.title">ProductVariantTranslation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productVariantTranslationEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.productVariantTranslation.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {productVariantTranslationEntity.createdat ? (
              <TextFormat value={productVariantTranslationEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.productVariantTranslation.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {productVariantTranslationEntity.updatedat ? (
              <TextFormat value={productVariantTranslationEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="languagecode">
              <Translate contentKey="venjureApp.productVariantTranslation.languagecode">Languagecode</Translate>
            </span>
          </dt>
          <dd>{productVariantTranslationEntity.languagecode}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.productVariantTranslation.name">Name</Translate>
            </span>
          </dt>
          <dd>{productVariantTranslationEntity.name}</dd>
          <dt>
            <Translate contentKey="venjureApp.productVariantTranslation.base">Base</Translate>
          </dt>
          <dd>{productVariantTranslationEntity.base ? productVariantTranslationEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-variant-translation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-variant-translation/${productVariantTranslationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productVariantTranslation }: IRootState) => ({
  productVariantTranslationEntity: productVariantTranslation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantTranslationDetail);
