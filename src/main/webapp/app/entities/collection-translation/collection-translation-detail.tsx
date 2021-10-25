import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './collection-translation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICollectionTranslationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CollectionTranslationDetail = (props: ICollectionTranslationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { collectionTranslationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="collectionTranslationDetailsHeading">
          <Translate contentKey="venjureApp.collectionTranslation.detail.title">CollectionTranslation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{collectionTranslationEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.collectionTranslation.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {collectionTranslationEntity.createdat ? (
              <TextFormat value={collectionTranslationEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.collectionTranslation.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {collectionTranslationEntity.updatedat ? (
              <TextFormat value={collectionTranslationEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="languagecode">
              <Translate contentKey="venjureApp.collectionTranslation.languagecode">Languagecode</Translate>
            </span>
          </dt>
          <dd>{collectionTranslationEntity.languagecode}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.collectionTranslation.name">Name</Translate>
            </span>
          </dt>
          <dd>{collectionTranslationEntity.name}</dd>
          <dt>
            <span id="slug">
              <Translate contentKey="venjureApp.collectionTranslation.slug">Slug</Translate>
            </span>
          </dt>
          <dd>{collectionTranslationEntity.slug}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="venjureApp.collectionTranslation.description">Description</Translate>
            </span>
          </dt>
          <dd>{collectionTranslationEntity.description}</dd>
          <dt>
            <Translate contentKey="venjureApp.collectionTranslation.base">Base</Translate>
          </dt>
          <dd>{collectionTranslationEntity.base ? collectionTranslationEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/collection-translation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/collection-translation/${collectionTranslationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ collectionTranslation }: IRootState) => ({
  collectionTranslationEntity: collectionTranslation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CollectionTranslationDetail);
