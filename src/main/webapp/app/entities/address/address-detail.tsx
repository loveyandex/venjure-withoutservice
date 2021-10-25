import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './address.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAddressDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AddressDetail = (props: IAddressDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { addressEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="addressDetailsHeading">
          <Translate contentKey="venjureApp.address.detail.title">Address</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{addressEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.address.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{addressEntity.createdat ? <TextFormat value={addressEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.address.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{addressEntity.updatedat ? <TextFormat value={addressEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="fullname">
              <Translate contentKey="venjureApp.address.fullname">Fullname</Translate>
            </span>
          </dt>
          <dd>{addressEntity.fullname}</dd>
          <dt>
            <span id="company">
              <Translate contentKey="venjureApp.address.company">Company</Translate>
            </span>
          </dt>
          <dd>{addressEntity.company}</dd>
          <dt>
            <span id="streetline1">
              <Translate contentKey="venjureApp.address.streetline1">Streetline 1</Translate>
            </span>
          </dt>
          <dd>{addressEntity.streetline1}</dd>
          <dt>
            <span id="streetline2">
              <Translate contentKey="venjureApp.address.streetline2">Streetline 2</Translate>
            </span>
          </dt>
          <dd>{addressEntity.streetline2}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="venjureApp.address.city">City</Translate>
            </span>
          </dt>
          <dd>{addressEntity.city}</dd>
          <dt>
            <span id="province">
              <Translate contentKey="venjureApp.address.province">Province</Translate>
            </span>
          </dt>
          <dd>{addressEntity.province}</dd>
          <dt>
            <span id="postalcode">
              <Translate contentKey="venjureApp.address.postalcode">Postalcode</Translate>
            </span>
          </dt>
          <dd>{addressEntity.postalcode}</dd>
          <dt>
            <span id="phonenumber">
              <Translate contentKey="venjureApp.address.phonenumber">Phonenumber</Translate>
            </span>
          </dt>
          <dd>{addressEntity.phonenumber}</dd>
          <dt>
            <span id="defaultshippingaddress">
              <Translate contentKey="venjureApp.address.defaultshippingaddress">Defaultshippingaddress</Translate>
            </span>
          </dt>
          <dd>{addressEntity.defaultshippingaddress ? 'true' : 'false'}</dd>
          <dt>
            <span id="defaultbillingaddress">
              <Translate contentKey="venjureApp.address.defaultbillingaddress">Defaultbillingaddress</Translate>
            </span>
          </dt>
          <dd>{addressEntity.defaultbillingaddress ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="venjureApp.address.customer">Customer</Translate>
          </dt>
          <dd>{addressEntity.customer ? addressEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.address.country">Country</Translate>
          </dt>
          <dd>{addressEntity.country ? addressEntity.country.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/address" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/address/${addressEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ address }: IRootState) => ({
  addressEntity: address.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AddressDetail);
