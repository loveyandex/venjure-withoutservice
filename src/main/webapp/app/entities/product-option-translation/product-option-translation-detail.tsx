import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-option-translation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductOptionTranslationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductOptionTranslationDetail = (props: IProductOptionTranslationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productOptionTranslationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productOptionTranslationDetailsHeading">
          <Translate contentKey="venjureApp.productOptionTranslation.detail.title">ProductOptionTranslation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productOptionTranslationEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.productOptionTranslation.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {productOptionTranslationEntity.createdat ? (
              <TextFormat value={productOptionTranslationEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.productOptionTranslation.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {productOptionTranslationEntity.updatedat ? (
              <TextFormat value={productOptionTranslationEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="languagecode">
              <Translate contentKey="venjureApp.productOptionTranslation.languagecode">Languagecode</Translate>
            </span>
          </dt>
          <dd>{productOptionTranslationEntity.languagecode}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.productOptionTranslation.name">Name</Translate>
            </span>
          </dt>
          <dd>{productOptionTranslationEntity.name}</dd>
          <dt>
            <Translate contentKey="venjureApp.productOptionTranslation.base">Base</Translate>
          </dt>
          <dd>{productOptionTranslationEntity.base ? productOptionTranslationEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-option-translation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-option-translation/${productOptionTranslationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productOptionTranslation }: IRootState) => ({
  productOptionTranslationEntity: productOptionTranslation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductOptionTranslationDetail);
