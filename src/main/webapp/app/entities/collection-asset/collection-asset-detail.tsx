import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './collection-asset.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICollectionAssetDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CollectionAssetDetail = (props: ICollectionAssetDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { collectionAssetEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="collectionAssetDetailsHeading">
          <Translate contentKey="venjureApp.collectionAsset.detail.title">CollectionAsset</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{collectionAssetEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.collectionAsset.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {collectionAssetEntity.createdat ? (
              <TextFormat value={collectionAssetEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.collectionAsset.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {collectionAssetEntity.updatedat ? (
              <TextFormat value={collectionAssetEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="position">
              <Translate contentKey="venjureApp.collectionAsset.position">Position</Translate>
            </span>
          </dt>
          <dd>{collectionAssetEntity.position}</dd>
          <dt>
            <Translate contentKey="venjureApp.collectionAsset.asset">Asset</Translate>
          </dt>
          <dd>{collectionAssetEntity.asset ? collectionAssetEntity.asset.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.collectionAsset.collection">Collection</Translate>
          </dt>
          <dd>{collectionAssetEntity.collection ? collectionAssetEntity.collection.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/collection-asset" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/collection-asset/${collectionAssetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ collectionAsset }: IRootState) => ({
  collectionAssetEntity: collectionAsset.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CollectionAssetDetail);
