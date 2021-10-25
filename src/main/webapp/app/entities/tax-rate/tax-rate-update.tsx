import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ITaxCategory } from 'app/shared/model/tax-category.model';
import { getEntities as getTaxCategories } from 'app/entities/tax-category/tax-category.reducer';
import { IZone } from 'app/shared/model/zone.model';
import { getEntities as getZones } from 'app/entities/zone/zone.reducer';
import { ICustomerGroup } from 'app/shared/model/customer-group.model';
import { getEntities as getCustomerGroups } from 'app/entities/customer-group/customer-group.reducer';
import { getEntity, updateEntity, createEntity, reset } from './tax-rate.reducer';
import { ITaxRate } from 'app/shared/model/tax-rate.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITaxRateUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TaxRateUpdate = (props: ITaxRateUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { taxRateEntity, taxCategories, zones, customerGroups, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/tax-rate' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getTaxCategories();
    props.getZones();
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

    if (errors.length === 0) {
      const entity = {
        ...taxRateEntity,
        ...values,
        category: taxCategories.find(it => it.id.toString() === values.categoryId.toString()),
        zone: zones.find(it => it.id.toString() === values.zoneId.toString()),
        customergroup: customerGroups.find(it => it.id.toString() === values.customergroupId.toString()),
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
          <h2 id="venjureApp.taxRate.home.createOrEditLabel" data-cy="TaxRateCreateUpdateHeading">
            <Translate contentKey="venjureApp.taxRate.home.createOrEditLabel">Create or edit a TaxRate</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : taxRateEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="tax-rate-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="tax-rate-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="tax-rate-createdat">
                  <Translate contentKey="venjureApp.taxRate.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="tax-rate-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.taxRateEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="tax-rate-updatedat">
                  <Translate contentKey="venjureApp.taxRate.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="tax-rate-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.taxRateEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="tax-rate-name">
                  <Translate contentKey="venjureApp.taxRate.name">Name</Translate>
                </Label>
                <AvField
                  id="tax-rate-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="enabledLabel">
                  <AvInput id="tax-rate-enabled" data-cy="enabled" type="checkbox" className="form-check-input" name="enabled" />
                  <Translate contentKey="venjureApp.taxRate.enabled">Enabled</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="valueLabel" for="tax-rate-value">
                  <Translate contentKey="venjureApp.taxRate.value">Value</Translate>
                </Label>
                <AvField
                  id="tax-rate-value"
                  data-cy="value"
                  type="text"
                  name="value"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="tax-rate-category">
                  <Translate contentKey="venjureApp.taxRate.category">Category</Translate>
                </Label>
                <AvInput id="tax-rate-category" data-cy="category" type="select" className="form-control" name="categoryId">
                  <option value="" key="0" />
                  {taxCategories
                    ? taxCategories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="tax-rate-zone">
                  <Translate contentKey="venjureApp.taxRate.zone">Zone</Translate>
                </Label>
                <AvInput id="tax-rate-zone" data-cy="zone" type="select" className="form-control" name="zoneId">
                  <option value="" key="0" />
                  {zones
                    ? zones.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="tax-rate-customergroup">
                  <Translate contentKey="venjureApp.taxRate.customergroup">Customergroup</Translate>
                </Label>
                <AvInput id="tax-rate-customergroup" data-cy="customergroup" type="select" className="form-control" name="customergroupId">
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
              <Button tag={Link} id="cancel-save" to="/tax-rate" replace color="info">
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
  taxCategories: storeState.taxCategory.entities,
  zones: storeState.zone.entities,
  customerGroups: storeState.customerGroup.entities,
  taxRateEntity: storeState.taxRate.entity,
  loading: storeState.taxRate.loading,
  updating: storeState.taxRate.updating,
  updateSuccess: storeState.taxRate.updateSuccess,
});

const mapDispatchToProps = {
  getTaxCategories,
  getZones,
  getCustomerGroups,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TaxRateUpdate);
