import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-option.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductOptionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProductOptionDetail = (props: IProductOptionDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { productOptionEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productOptionDetailsHeading">
          <Translate contentKey="venjureApp.productOption.detail.title">ProductOption</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productOptionEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.productOption.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {productOptionEntity.createdat ? (
              <TextFormat value={productOptionEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.productOption.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {productOptionEntity.updatedat ? (
              <TextFormat value={productOptionEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="deletedat">
              <Translate contentKey="venjureApp.productOption.deletedat">Deletedat</Translate>
            </span>
          </dt>
          <dd>
            {productOptionEntity.deletedat ? (
              <TextFormat value={productOptionEntity.deletedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="code">
              <Translate contentKey="venjureApp.productOption.code">Code</Translate>
            </span>
          </dt>
          <dd>{productOptionEntity.code}</dd>
          <dt>
            <Translate contentKey="venjureApp.productOption.group">Group</Translate>
          </dt>
          <dd>{productOptionEntity.group ? productOptionEntity.group.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-option" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-option/${productOptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ productOption }: IRootState) => ({
  productOptionEntity: productOption.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductOptionDetail);
