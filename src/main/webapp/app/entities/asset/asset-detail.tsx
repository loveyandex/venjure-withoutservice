import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './asset.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAssetDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AssetDetail = (props: IAssetDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { assetEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assetDetailsHeading">
          <Translate contentKey="venjureApp.asset.detail.title">Asset</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{assetEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.asset.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{assetEntity.createdat ? <TextFormat value={assetEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.asset.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{assetEntity.updatedat ? <TextFormat value={assetEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.asset.name">Name</Translate>
            </span>
          </dt>
          <dd>{assetEntity.name}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="venjureApp.asset.type">Type</Translate>
            </span>
          </dt>
          <dd>{assetEntity.type}</dd>
          <dt>
            <span id="mimetype">
              <Translate contentKey="venjureApp.asset.mimetype">Mimetype</Translate>
            </span>
          </dt>
          <dd>{assetEntity.mimetype}</dd>
          <dt>
            <span id="width">
              <Translate contentKey="venjureApp.asset.width">Width</Translate>
            </span>
          </dt>
          <dd>{assetEntity.width}</dd>
          <dt>
            <span id="height">
              <Translate contentKey="venjureApp.asset.height">Height</Translate>
            </span>
          </dt>
          <dd>{assetEntity.height}</dd>
          <dt>
            <span id="filesize">
              <Translate contentKey="venjureApp.asset.filesize">Filesize</Translate>
            </span>
          </dt>
          <dd>{assetEntity.filesize}</dd>
          <dt>
            <span id="source">
              <Translate contentKey="venjureApp.asset.source">Source</Translate>
            </span>
          </dt>
          <dd>{assetEntity.source}</dd>
          <dt>
            <span id="preview">
              <Translate contentKey="venjureApp.asset.preview">Preview</Translate>
            </span>
          </dt>
          <dd>{assetEntity.preview}</dd>
          <dt>
            <span id="focalpoint">
              <Translate contentKey="venjureApp.asset.focalpoint">Focalpoint</Translate>
            </span>
          </dt>
          <dd>{assetEntity.focalpoint}</dd>
        </dl>
        <Button tag={Link} to="/asset" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/asset/${assetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ asset }: IRootState) => ({
  assetEntity: asset.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AssetDetail);
