import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-option-group.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductOptionGroupDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductOptionGroupDetail = (props: IProductOptionGroupDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productOptionGroupEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productOptionGroupDetailsHeading">
          <Translate contentKey="venjureApp.productOptionGroup.detail.title">ProductOptionGroup</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productOptionGroupEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.productOptionGroup.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {productOptionGroupEntity.createdat ? (
              <TextFormat value={productOptionGroupEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.productOptionGroup.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {productOptionGroupEntity.updatedat ? (
              <TextFormat value={productOptionGroupEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="deletedat">
              <Translate contentKey="venjureApp.productOptionGroup.deletedat">Deletedat</Translate>
            </span>
          </dt>
          <dd>
            {productOptionGroupEntity.deletedat ? (
              <TextFormat value={productOptionGroupEntity.deletedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="code">
              <Translate contentKey="venjureApp.productOptionGroup.code">Code</Translate>
            </span>
          </dt>
          <dd>{productOptionGroupEntity.code}</dd>
          <dt>
            <Translate contentKey="venjureApp.productOptionGroup.product">Product</Translate>
          </dt>
          <dd>{productOptionGroupEntity.product ? productOptionGroupEntity.product.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-option-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-option-group/${productOptionGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productOptionGroup }: IRootState) => ({
  productOptionGroupEntity: productOptionGroup.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductOptionGroupDetail);
