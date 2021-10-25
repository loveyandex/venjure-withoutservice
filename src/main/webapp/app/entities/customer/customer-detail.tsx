import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerDetail = (props: ICustomerDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { customerEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerDetailsHeading">
          <Translate contentKey="venjureApp.customer.detail.title">Customer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.customer.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{customerEntity.createdat ? <TextFormat value={customerEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.customer.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{customerEntity.updatedat ? <TextFormat value={customerEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deletedat">
              <Translate contentKey="venjureApp.customer.deletedat">Deletedat</Translate>
            </span>
          </dt>
          <dd>{customerEntity.deletedat ? <TextFormat value={customerEntity.deletedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="venjureApp.customer.title">Title</Translate>
            </span>
          </dt>
          <dd>{customerEntity.title}</dd>
          <dt>
            <span id="firstname">
              <Translate contentKey="venjureApp.customer.firstname">Firstname</Translate>
            </span>
          </dt>
          <dd>{customerEntity.firstname}</dd>
          <dt>
            <span id="lastname">
              <Translate contentKey="venjureApp.customer.lastname">Lastname</Translate>
            </span>
          </dt>
          <dd>{customerEntity.lastname}</dd>
          <dt>
            <span id="phonenumber">
              <Translate contentKey="venjureApp.customer.phonenumber">Phonenumber</Translate>
            </span>
          </dt>
          <dd>{customerEntity.phonenumber}</dd>
          <dt>
            <span id="emailaddress">
              <Translate contentKey="venjureApp.customer.emailaddress">Emailaddress</Translate>
            </span>
          </dt>
          <dd>{customerEntity.emailaddress}</dd>
          <dt>
            <Translate contentKey="venjureApp.customer.user">User</Translate>
          </dt>
          <dd>{customerEntity.user ? customerEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.customer.avatar">Avatar</Translate>
          </dt>
          <dd>{customerEntity.avatar ? customerEntity.avatar.id : ''}</dd>
          <dt>
            <Translate contentKey="venjureApp.customer.channel">Channel</Translate>
          </dt>
          <dd>
            {customerEntity.channels
              ? customerEntity.channels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {customerEntity.channels && i === customerEntity.channels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="venjureApp.customer.customerGroup">Customer Group</Translate>
          </dt>
          <dd>
            {customerEntity.customerGroups
              ? customerEntity.customerGroups.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {customerEntity.customerGroups && i === customerEntity.customerGroups.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/customer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer/${customerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ customer }: IRootState) => ({
  customerEntity: customer.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerDetail);
