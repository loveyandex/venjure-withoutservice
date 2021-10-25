import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './tax-category.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITaxCategoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TaxCategoryDetail = (props: ITaxCategoryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { taxCategoryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taxCategoryDetailsHeading">
          <Translate contentKey="venjureApp.taxCategory.detail.title">TaxCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taxCategoryEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.taxCategory.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {taxCategoryEntity.createdat ? <TextFormat value={taxCategoryEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.taxCategory.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {taxCategoryEntity.updatedat ? <TextFormat value={taxCategoryEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.taxCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{taxCategoryEntity.name}</dd>
          <dt>
            <span id="isdefault">
              <Translate contentKey="venjureApp.taxCategory.isdefault">Isdefault</Translate>
            </span>
          </dt>
          <dd>{taxCategoryEntity.isdefault ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/tax-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tax-category/${taxCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ taxCategory }: IRootState) => ({
  taxCategoryEntity: taxCategory.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TaxCategoryDetail);
