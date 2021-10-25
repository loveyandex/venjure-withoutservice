import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shipping-method-translation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShippingMethodTranslationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShippingMethodTranslationDetail = (props: IShippingMethodTranslationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { shippingMethodTranslationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shippingMethodTranslationDetailsHeading">
          <Translate contentKey="venjureApp.shippingMethodTranslation.detail.title">ShippingMethodTranslation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shippingMethodTranslationEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.shippingMethodTranslation.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {shippingMethodTranslationEntity.createdat ? (
              <TextFormat value={shippingMethodTranslationEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.shippingMethodTranslation.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {shippingMethodTranslationEntity.updatedat ? (
              <TextFormat value={shippingMethodTranslationEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="languagecode">
              <Translate contentKey="venjureApp.shippingMethodTranslation.languagecode">Languagecode</Translate>
            </span>
          </dt>
          <dd>{shippingMethodTranslationEntity.languagecode}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.shippingMethodTranslation.name">Name</Translate>
            </span>
          </dt>
          <dd>{shippingMethodTranslationEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="venjureApp.shippingMethodTranslation.description">Description</Translate>
            </span>
          </dt>
          <dd>{shippingMethodTranslationEntity.description}</dd>
          <dt>
            <Translate contentKey="venjureApp.shippingMethodTranslation.base">Base</Translate>
          </dt>
          <dd>{shippingMethodTranslationEntity.base ? shippingMethodTranslationEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/shipping-method-translation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shipping-method-translation/${shippingMethodTranslationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ shippingMethodTranslation }: IRootState) => ({
  shippingMethodTranslationEntity: shippingMethodTranslation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShippingMethodTranslationDetail);
