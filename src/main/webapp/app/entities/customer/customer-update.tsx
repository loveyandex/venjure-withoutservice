import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IAsset } from 'app/shared/model/asset.model';
import { getEntities as getAssets } from 'app/entities/asset/asset.reducer';
import { IChannel } from 'app/shared/model/channel.model';
import { getEntities as getChannels } from 'app/entities/channel/channel.reducer';
import { ICustomerGroup } from 'app/shared/model/customer-group.model';
import { getEntities as getCustomerGroups } from 'app/entities/customer-group/customer-group.reducer';
import { getEntity, updateEntity, createEntity, reset } from './customer.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerUpdate = (props: ICustomerUpdateProps) => {
  const [idschannel, setIdschannel] = useState([]);
  const [idscustomerGroup, setIdscustomerGroup] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { customerEntity, users, assets, channels, customerGroups, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/customer' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getAssets();
    props.getChannels();
    props.getCustomerGroups();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdat = convertDateTimeToServer(values.createdat);
    values.updatedat = convertDateTimeToServer(values.updatedat);
    values.deletedat = convertDateTimeToServer(values.deletedat);

    if (errors.length === 0) {
      const entity = {
        ...customerEntity,
        ...values,
        channels: mapIdList(values.channels),
        customerGroups: mapIdList(values.customerGroups),
        user: users.find(it => it.id.toString() === values.userId.toString()),
        avatar: assets.find(it => it.id.toString() === values.avatarId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="venjureApp.customer.home.createOrEditLabel" data-cy="CustomerCreateUpdateHeading">
            <Translate contentKey="venjureApp.customer.home.createOrEditLabel">Create or edit a Customer</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : customerEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="customer-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="customer-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="customer-createdat">
                  <Translate contentKey="venjureApp.customer.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="customer-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.customerEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="customer-updatedat">
                  <Translate contentKey="venjureApp.customer.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="customer-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.customerEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="deletedatLabel" for="customer-deletedat">
                  <Translate contentKey="venjureApp.customer.deletedat">Deletedat</Translate>
                </Label>
                <AvInput
                  id="customer-deletedat"
                  data-cy="deletedat"
                  type="datetime-local"
                  className="form-control"
                  name="deletedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.customerEntity.deletedat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="titleLabel" for="customer-title">
                  <Translate contentKey="venjureApp.customer.title">Title</Translate>
                </Label>
                <AvField
                  id="customer-title"
                  data-cy="title"
                  type="text"
                  name="title"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="firstnameLabel" for="customer-firstname">
                  <Translate contentKey="venjureApp.customer.firstname">Firstname</Translate>
                </Label>
                <AvField
                  id="customer-firstname"
                  data-cy="firstname"
                  type="text"
                  name="firstname"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="lastnameLabel" for="customer-lastname">
                  <Translate contentKey="venjureApp.customer.lastname">Lastname</Translate>
                </Label>
                <AvField
                  id="customer-lastname"
                  data-cy="lastname"
                  type="text"
                  name="lastname"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="phonenumberLabel" for="customer-phonenumber">
                  <Translate contentKey="venjureApp.customer.phonenumber">Phonenumber</Translate>
                </Label>
                <AvField
                  id="customer-phonenumber"
                  data-cy="phonenumber"
                  type="text"
                  name="phonenumber"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="emailaddressLabel" for="customer-emailaddress">
                  <Translate contentKey="venjureApp.customer.emailaddress">Emailaddress</Translate>
                </Label>
                <AvField
                  id="customer-emailaddress"
                  data-cy="emailaddress"
                  type="text"
                  name="emailaddress"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="customer-user">
                  <Translate contentKey="venjureApp.customer.user">User</Translate>
                </Label>
                <AvInput id="customer-user" data-cy="user" type="select" className="form-control" name="userId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="customer-avatar">
                  <Translate contentKey="venjureApp.customer.avatar">Avatar</Translate>
                </Label>
                <AvInput id="customer-avatar" data-cy="avatar" type="select" className="form-control" name="avatarId">
                  <option value="" key="0" />
                  {assets
                    ? assets.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="customer-channel">
                  <Translate contentKey="venjureApp.customer.channel">Channel</Translate>
                </Label>
                <AvInput
                  id="customer-channel"
                  data-cy="channel"
                  type="select"
                  multiple
                  className="form-control"
                  name="channels"
                  value={!isNew && customerEntity.channels && customerEntity.channels.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {channels
                    ? channels.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="customer-customerGroup">
                  <Translate contentKey="venjureApp.customer.customerGroup">Customer Group</Translate>
                </Label>
                <AvInput
                  id="customer-customerGroup"
                  data-cy="customerGroup"
                  type="select"
                  multiple
                  className="form-control"
                  name="customerGroups"
                  value={!isNew && customerEntity.customerGroups && customerEntity.customerGroups.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {customerGroups
                    ? customerGroups.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/customer" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  assets: storeState.asset.entities,
  channels: storeState.channel.entities,
  customerGroups: storeState.customerGroup.entities,
  customerEntity: storeState.customer.entity,
  loading: storeState.customer.loading,
  updating: storeState.customer.updating,
  updateSuccess: storeState.customer.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getAssets,
  getChannels,
  getCustomerGroups,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerUpdate);
