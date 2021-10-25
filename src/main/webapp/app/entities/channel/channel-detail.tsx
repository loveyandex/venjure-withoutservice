import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './channel.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IChannelDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ChannelDetail = (props: IChannelDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { channelEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="channelDetailsHeading">
          <Translate contentKey="venjureApp.channel.detail.title">Channel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{channelEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.channel.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{channelEntity.createdat ? <TextFormat value={channelEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.channel.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{channelEntity.updatedat ? <TextFormat value={channelEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="venjureApp.channel.code">Code</Translate>
            </span>
          </dt>
          <dd>{channelEntity.code}</dd>
          <dt>
            <span id="token">
              <Translate contentKey="venjureApp.channel.token">Token</Translate>
            </span>
          </dt>
          <dd>{channelEntity.token}</dd>
          <dt>
            <span id="defaultlanguagecode">
              <Translate contentKey="venjureApp.channel.defaultlanguagecode">Defaultlanguagecode</Translate>
            </span>
          </dt>
          <dd>{channelEntity.defaultlanguagecode}</dd>
          <dt>
            <span id="currencycode">
              <Translate contentKey="venjureApp.channel.currencycode">Currencycode</Translate>
            </span>
          </dt>
          <dd>{channelEntity.currencycode}</dd>
          <dt>
            <span id="pricesincludetax">
              <Translate contentKey="venjureApp.channel.pricesincludetax">Pricesincludetax</Translate>
            </span>
          </dt>
          <dd>{channelEntity.pricesincludetax ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="venjureApp.channel.defaulttaxzone">Defaulttaxzone</Translate>
          </dt>
          <dd>{channelEntity.defaulttaxzone ? channelEntity.defaulttaxzone.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.channel.defaultshippingzone">Defaultshippingzone</Translate>
          </dt>
          <dd>{channelEntity.defaultshippingzone ? channelEntity.defaultshippingzone.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.channel.paymentMethod">Payment Method</Translate>
          </dt>
          <dd>
            {channelEntity.paymentMethods
              ? channelEntity.paymentMethods.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {channelEntity.paymentMethods && i === channelEntity.paymentMethods.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.channel.product">Product</Translate>
          </dt>
          <dd>
            {channelEntity.products
              ? channelEntity.products.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {channelEntity.products && i === channelEntity.products.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.channel.promotion">Promotion</Translate>
          </dt>
          <dd>
            {channelEntity.promotions
              ? channelEntity.promotions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {channelEntity.promotions && i === channelEntity.promotions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.channel.shippingMethod">Shipping Method</Translate>
          </dt>
          <dd>
            {channelEntity.shippingMethods
              ? channelEntity.shippingMethods.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {channelEntity.shippingMethods && i === channelEntity.shippingMethods.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/channel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/channel/${channelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ channel }: IRootState) => ({
  channelEntity: channel.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ChannelDetail);
