import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './facet-value-translation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFacetValueTranslationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacetValueTranslationDetail = (props: IFacetValueTranslationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { facetValueTranslationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="facetValueTranslationDetailsHeading">
          <Translate contentKey="venjureApp.facetValueTranslation.detail.title">FacetValueTranslation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{facetValueTranslationEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.facetValueTranslation.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {facetValueTranslationEntity.createdat ? (
              <TextFormat value={facetValueTranslationEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.facetValueTranslation.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {facetValueTranslationEntity.updatedat ? (
              <TextFormat value={facetValueTranslationEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="languagecode">
              <Translate contentKey="venjureApp.facetValueTranslation.languagecode">Languagecode</Translate>
            </span>
          </dt>
          <dd>{facetValueTranslationEntity.languagecode}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.facetValueTranslation.name">Name</Translate>
            </span>
          </dt>
          <dd>{facetValueTranslationEntity.name}</dd>
          <dt>
            <Translate contentKey="venjureApp.facetValueTranslation.base">Base</Translate>
          </dt>
          <dd>{facetValueTranslationEntity.base ? facetValueTranslationEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/facet-value-translation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/facet-value-translation/${facetValueTranslationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ facetValueTranslation }: IRootState) => ({
  facetValueTranslationEntity: facetValueTranslation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacetValueTranslationDetail);
