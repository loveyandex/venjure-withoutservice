import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './facet-translation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFacetTranslationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacetTranslationDetail = (props: IFacetTranslationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { facetTranslationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="facetTranslationDetailsHeading">
          <Translate contentKey="venjureApp.facetTranslation.detail.title">FacetTranslation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{facetTranslationEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.facetTranslation.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {facetTranslationEntity.createdat ? (
              <TextFormat value={facetTranslationEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.facetTranslation.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {facetTranslationEntity.updatedat ? (
              <TextFormat value={facetTranslationEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="languagecode">
              <Translate contentKey="venjureApp.facetTranslation.languagecode">Languagecode</Translate>
            </span>
          </dt>
          <dd>{facetTranslationEntity.languagecode}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.facetTranslation.name">Name</Translate>
            </span>
          </dt>
          <dd>{facetTranslationEntity.name}</dd>
          <dt>
            <Translate contentKey="venjureApp.facetTranslation.base">Base</Translate>
          </dt>
          <dd>{facetTranslationEntity.base ? facetTranslationEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/facet-translation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/facet-translation/${facetTranslationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ facetTranslation }: IRootState) => ({
  facetTranslationEntity: facetTranslation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacetTranslationDetail);
