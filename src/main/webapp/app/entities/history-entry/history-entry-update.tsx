import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAdministrator } from 'app/shared/model/administrator.model';
import { getEntities as getAdministrators } from 'app/entities/administrator/administrator.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { IJorder } from 'app/shared/model/jorder.model';
import { getEntities as getJorders } from 'app/entities/jorder/jorder.reducer';
import { getEntity, updateEntity, createEntity, reset } from './history-entry.reducer';
import { IHistoryEntry } from 'app/shared/model/history-entry.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHistoryEntryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HistoryEntryUpdate = (props: IHistoryEntryUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { historyEntryEntity, administrators, customers, jorders, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/history-entry' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAdministrators();
    props.getCustomers();
    props.getJorders();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdat = convertDateTimeToServer(values.createdat);
    values.updatedat = convertDateTimeToServer(values.updatedat);

    if (errors.length === 0) {
      const entity = {
        ...historyEntryEntity,
        ...values,
        administrator: administrators.find(it => it.id.toString() === values.administratorId.toString()),
        customer: customers.find(it => it.id.toString() === values.customerId.toString()),
        jorder: jorders.find(it => it.id.toString() === values.jorderId.toString()),
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
          <h2 id="venjureApp.historyEntry.home.createOrEditLabel" data-cy="HistoryEntryCreateUpdateHeading">
            <Translate contentKey="venjureApp.historyEntry.home.createOrEditLabel">Create or edit a HistoryEntry</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : historyEntryEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="history-entry-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="history-entry-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="history-entry-createdat">
                  <Translate contentKey="venjureApp.historyEntry.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="history-entry-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.historyEntryEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="history-entry-updatedat">
                  <Translate contentKey="venjureApp.historyEntry.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="history-entry-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.historyEntryEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="history-entry-type">
                  <Translate contentKey="venjureApp.historyEntry.type">Type</Translate>
                </Label>
                <AvField
                  id="history-entry-type"
                  data-cy="type"
                  type="text"
                  name="type"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="ispublicLabel">
                  <AvInput id="history-entry-ispublic" data-cy="ispublic" type="checkbox" className="form-check-input" name="ispublic" />
                  <Translate contentKey="venjureApp.historyEntry.ispublic">Ispublic</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="dataLabel" for="history-entry-data">
                  <Translate contentKey="venjureApp.historyEntry.data">Data</Translate>
                </Label>
                <AvField
                  id="history-entry-data"
                  data-cy="data"
                  type="text"
                  name="data"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="discriminatorLabel" for="history-entry-discriminator">
                  <Translate contentKey="venjureApp.historyEntry.discriminator">Discriminator</Translate>
                </Label>
                <AvField
                  id="history-entry-discriminator"
                  data-cy="discriminator"
                  type="text"
                  name="discriminator"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="history-entry-administrator">
                  <Translate contentKey="venjureApp.historyEntry.administrator">Administrator</Translate>
                </Label>
                <AvInput
                  id="history-entry-administrator"
                  data-cy="administrator"
                  type="select"
                  className="form-control"
                  name="administratorId"
                >
                  <option value="" key="0" />
                  {administrators
                    ? administrators.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="history-entry-customer">
                  <Translate contentKey="venjureApp.historyEntry.customer">Customer</Translate>
                </Label>
                <AvInput id="history-entry-customer" data-cy="customer" type="select" className="form-control" name="customerId">
                  <option value="" key="0" />
                  {customers
                    ? customers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="history-entry-jorder">
                  <Translate contentKey="venjureApp.historyEntry.jorder">Jorder</Translate>
                </Label>
                <AvInput id="history-entry-jorder" data-cy="jorder" type="select" className="form-control" name="jorderId">
                  <option value="" key="0" />
                  {jorders
                    ? jorders.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/history-entry" replace color="info">
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
  administrators: storeState.administrator.entities,
  customers: storeState.customer.entities,
  jorders: storeState.jorder.entities,
  historyEntryEntity: storeState.historyEntry.entity,
  loading: storeState.historyEntry.loading,
  updating: storeState.historyEntry.updating,
  updateSuccess: storeState.historyEntry.updateSuccess,
});

const mapDispatchToProps = {
  getAdministrators,
  getCustomers,
  getJorders,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HistoryEntryUpdate);
