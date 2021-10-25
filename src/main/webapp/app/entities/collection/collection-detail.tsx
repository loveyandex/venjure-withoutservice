import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './collection.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICollectionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CollectionDetail = (props: ICollectionDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { collectionEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="collectionDetailsHeading">
          <Translate contentKey="venjureApp.collection.detail.title">Collection</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.collection.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {collectionEntity.createdat ? <TextFormat value={collectionEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.collection.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {collectionEntity.updatedat ? <TextFormat value={collectionEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isroot">
              <Translate contentKey="venjureApp.collection.isroot">Isroot</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.isroot ? 'true' : 'false'}</dd>
          <dt>
            <span id="position">
              <Translate contentKey="venjureApp.collection.position">Position</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.position}</dd>
          <dt>
            <span id="isprivate">
              <Translate contentKey="venjureApp.collection.isprivate">Isprivate</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.isprivate ? 'true' : 'false'}</dd>
          <dt>
            <span id="filters">
              <Translate contentKey="venjureApp.collection.filters">Filters</Translate>
            </span>
          </dt>
          <dd>{collectionEntity.filters}</dd>
          <dt>
            <Translate contentKey="venjureApp.collection.featuredasset">Featuredasset</Translate>
          </dt>
          <dd>{collectionEntity.featuredasset ? collectionEntity.featuredasset.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.collection.parent">Parent</Translate>
          </dt>
          <dd>{collectionEntity.parent ? collectionEntity.parent.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/collection" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/collection/${collectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ collection }: IRootState) => ({
  collectionEntity: collection.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CollectionDetail);
