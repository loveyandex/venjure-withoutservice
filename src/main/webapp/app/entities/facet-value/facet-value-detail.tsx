import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './facet-value.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFacetValueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacetValueDetail = (props: IFacetValueDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { facetValueEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="facetValueDetailsHeading">
          <Translate contentKey="venjureApp.facetValue.detail.title">FacetValue</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{facetValueEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.facetValue.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {facetValueEntity.createdat ? <TextFormat value={facetValueEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.facetValue.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {facetValueEntity.updatedat ? <TextFormat value={facetValueEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="code">
              <Translate contentKey="venjureApp.facetValue.code">Code</Translate>
            </span>
          </dt>
          <dd>{facetValueEntity.code}</dd>
          <dt>
            <Translate contentKey="venjureApp.facetValue.facet">Facet</Translate>
          </dt>
          <dd>{facetValueEntity.facet ? facetValueEntity.facet.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.facetValue.channel">Channel</Translate>
          </dt>
          <dd>
            {facetValueEntity.channels
              ? facetValueEntity.channels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {facetValueEntity.channels && i === facetValueEntity.channels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.facetValue.product">Product</Translate>
          </dt>
          <dd>
            {facetValueEntity.products
              ? facetValueEntity.products.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {facetValueEntity.products && i === facetValueEntity.products.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/facet-value" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/facet-value/${facetValueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ facetValue }: IRootState) => ({
  facetValueEntity: facetValue.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacetValueDetail);
