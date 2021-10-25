import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { getEntity, updateEntity, createEntity, reset } from './address.reducer';
import { IAddress } from 'app/shared/model/address.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAddressUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AddressUpdate = (props: IAddressUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { addressEntity, customers, countries, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/address' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCustomers();
    props.getCountries();
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
        ...addressEntity,
        ...values,
        customer: customers.find(it => it.id.toString() === values.customerId.toString()),
        country: countries.find(it => it.id.toString() === values.countryId.toString()),
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
          <h2 id="venjureApp.address.home.createOrEditLabel" data-cy="AddressCreateUpdateHeading">
            <Translate contentKey="venjureApp.address.home.createOrEditLabel">Create or edit a Address</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : addressEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="address-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="address-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="address-createdat">
                  <Translate contentKey="venjureApp.address.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="address-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.addressEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="address-updatedat">
                  <Translate contentKey="venjureApp.address.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="address-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.addressEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fullnameLabel" for="address-fullname">
                  <Translate contentKey="venjureApp.address.fullname">Fullname</Translate>
                </Label>
                <AvField
                  id="address-fullname"
                  data-cy="fullname"
                  type="text"
                  name="fullname"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="companyLabel" for="address-company">
                  <Translate contentKey="venjureApp.address.company">Company</Translate>
                </Label>
                <AvField
                  id="address-company"
                  data-cy="company"
                  type="text"
                  name="company"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="streetline1Label" for="address-streetline1">
                  <Translate contentKey="venjureApp.address.streetline1">Streetline 1</Translate>
                </Label>
                <AvField
                  id="address-streetline1"
                  data-cy="streetline1"
                  type="text"
                  name="streetline1"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="streetline2Label" for="address-streetline2">
                  <Translate contentKey="venjureApp.address.streetline2">Streetline 2</Translate>
                </Label>
                <AvField
                  id="address-streetline2"
                  data-cy="streetline2"
                  type="text"
                  name="streetline2"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="cityLabel" for="address-city">
                  <Translate contentKey="venjureApp.address.city">City</Translate>
                </Label>
                <AvField
                  id="address-city"
                  data-cy="city"
                  type="text"
                  name="city"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="provinceLabel" for="address-province">
                  <Translate contentKey="venjureApp.address.province">Province</Translate>
                </Label>
                <AvField
                  id="address-province"
                  data-cy="province"
                  type="text"
                  name="province"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="postalcodeLabel" for="address-postalcode">
                  <Translate contentKey="venjureApp.address.postalcode">Postalcode</Translate>
                </Label>
                <AvField
                  id="address-postalcode"
                  data-cy="postalcode"
                  type="text"
                  name="postalcode"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="phonenumberLabel" for="address-phonenumber">
                  <Translate contentKey="venjureApp.address.phonenumber">Phonenumber</Translate>
                </Label>
                <AvField
                  id="address-phonenumber"
                  data-cy="phonenumber"
                  type="text"
                  name="phonenumber"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="defaultshippingaddressLabel">
                  <AvInput
                    id="address-defaultshippingaddress"
                    data-cy="defaultshippingaddress"
                    type="checkbox"
                    className="form-check-input"
                    name="defaultshippingaddress"
                  />
                  <Translate contentKey="venjureApp.address.defaultshippingaddress">Defaultshippingaddress</Translate>
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="defaultbillingaddressLabel">
                  <AvInput
                    id="address-defaultbillingaddress"
                    data-cy="defaultbillingaddress"
                    type="checkbox"
                    className="form-check-input"
                    name="defaultbillingaddress"
                  />
                  <Translate contentKey="venjureApp.address.defaultbillingaddress">Defaultbillingaddress</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="address-customer">
                  <Translate contentKey="venjureApp.address.customer">Customer</Translate>
                </Label>
                <AvInput id="address-customer" data-cy="customer" type="select" className="form-control" name="customerId">
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
                <Label for="address-country">
                  <Translate contentKey="venjureApp.address.country">Country</Translate>
                </Label>
                <AvInput id="address-country" data-cy="country" type="select" className="form-control" name="countryId">
                  <option value="" key="0" />
                  {countries
                    ? countries.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/address" replace color="info">
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
  customers: storeState.customer.entities,
  countries: storeState.country.entities,
  addressEntity: storeState.address.entity,
  loading: storeState.address.loading,
  updating: storeState.address.updating,
  updateSuccess: storeState.address.updateSuccess,
});

const mapDispatchToProps = {
  getCustomers,
  getCountries,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AddressUpdate);
