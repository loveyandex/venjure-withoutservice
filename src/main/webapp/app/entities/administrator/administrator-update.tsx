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
import { getEntity, updateEntity, createEntity, reset } from './administrator.reducer';
import { IAdministrator } from 'app/shared/model/administrator.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAdministratorUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AdministratorUpdate = (props: IAdministratorUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { administratorEntity, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/administrator' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
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
        ...administratorEntity,
        ...values,
        user: users.find(it => it.id.toString() === values.userId.toString()),
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
          <h2 id="venjureApp.administrator.home.createOrEditLabel" data-cy="AdministratorCreateUpdateHeading">
            <Translate contentKey="venjureApp.administrator.home.createOrEditLabel">Create or edit a Administrator</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : administratorEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="administrator-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="administrator-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="administrator-createdat">
                  <Translate contentKey="venjureApp.administrator.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="administrator-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.administratorEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="administrator-updatedat">
                  <Translate contentKey="venjureApp.administrator.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="administrator-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.administratorEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="deletedatLabel" for="administrator-deletedat">
                  <Translate contentKey="venjureApp.administrator.deletedat">Deletedat</Translate>
                </Label>
                <AvInput
                  id="administrator-deletedat"
                  data-cy="deletedat"
                  type="datetime-local"
                  className="form-control"
                  name="deletedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.administratorEntity.deletedat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="firstnameLabel" for="administrator-firstname">
                  <Translate contentKey="venjureApp.administrator.firstname">Firstname</Translate>
                </Label>
                <AvField
                  id="administrator-firstname"
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
                <Label id="lastnameLabel" for="administrator-lastname">
                  <Translate contentKey="venjureApp.administrator.lastname">Lastname</Translate>
                </Label>
                <AvField
                  id="administrator-lastname"
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
                <Label id="emailaddressLabel" for="administrator-emailaddress">
                  <Translate contentKey="venjureApp.administrator.emailaddress">Emailaddress</Translate>
                </Label>
                <AvField
                  id="administrator-emailaddress"
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
                <Label for="administrator-user">
                  <Translate contentKey="venjureApp.administrator.user">User</Translate>
                </Label>
                <AvInput id="administrator-user" data-cy="user" type="select" className="form-control" name="userId">
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
              <Button tag={Link} id="cancel-save" to="/administrator" replace color="info">
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
  administratorEntity: storeState.administrator.entity,
  loading: storeState.administrator.loading,
  updating: storeState.administrator.updating,
  updateSuccess: storeState.administrator.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AdministratorUpdate);
