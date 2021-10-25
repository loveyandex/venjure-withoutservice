import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-translation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductTranslationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductTranslationDetail = (props: IProductTranslationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productTranslationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productTranslationDetailsHeading">
          <Translate contentKey="venjureApp.productTranslation.detail.title">ProductTranslation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productTranslationEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.productTranslation.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {productTranslationEntity.createdat ? (
              <TextFormat value={productTranslationEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.productTranslation.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {productTranslationEntity.updatedat ? (
              <TextFormat value={productTranslationEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="languagecode">
              <Translate contentKey="venjureApp.productTranslation.languagecode">Languagecode</Translate>
            </span>
          </dt>
          <dd>{productTranslationEntity.languagecode}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.productTranslation.name">Name</Translate>
            </span>
          </dt>
          <dd>{productTranslationEntity.name}</dd>
          <dt>
            <span id="slug">
              <Translate contentKey="venjureApp.productTranslation.slug">Slug</Translate>
            </span>
          </dt>
          <dd>{productTranslationEntity.slug}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="venjureApp.productTranslation.description">Description</Translate>
            </span>
          </dt>
          <dd>{productTranslationEntity.description}</dd>
          <dt>
            <Translate contentKey="venjureApp.productTranslation.base">Base</Translate>
          </dt>
          <dd>{productTranslationEntity.base ? productTranslationEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-translation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-translation/${productTranslationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productTranslation }: IRootState) => ({
  productTranslationEntity: productTranslation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductTranslationDetail);
