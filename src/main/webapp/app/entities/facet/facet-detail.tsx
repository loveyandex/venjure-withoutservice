import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './facet.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFacetDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacetDetail = (props: IFacetDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { facetEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="facetDetailsHeading">
          <Translate contentKey="venjureApp.facet.detail.title">Facet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{facetEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.facet.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{facetEntity.createdat ? <TextFormat value={facetEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.facet.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{facetEntity.updatedat ? <TextFormat value={facetEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isprivate">
              <Translate contentKey="venjureApp.facet.isprivate">Isprivate</Translate>
            </span>
          </dt>
          <dd>{facetEntity.isprivate ? 'true' : 'false'}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="venjureApp.facet.code">Code</Translate>
            </span>
          </dt>
          <dd>{facetEntity.code}</dd>
          <dt>
            <Translate contentKey="venjureApp.facet.channel">Channel</Translate>
          </dt>
          <dd>
            {facetEntity.channels
              ? facetEntity.channels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {facetEntity.channels && i === facetEntity.channels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/facet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/facet/${facetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ facet }: IRootState) => ({
  facetEntity: facet.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacetDetail);
