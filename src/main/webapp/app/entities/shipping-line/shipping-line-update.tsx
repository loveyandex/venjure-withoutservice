import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShippingMethod } from 'app/shared/model/shipping-method.model';
import { getEntities as getShippingMethods } from 'app/entities/shipping-method/shipping-method.reducer';
import { IJorder } from 'app/shared/model/jorder.model';
import { getEntities as getJorders } from 'app/entities/jorder/jorder.reducer';
import { getEntity, updateEntity, createEntity, reset } from './shipping-line.reducer';
import { IShippingLine } from 'app/shared/model/shipping-line.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShippingLineUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ShippingLineUpdate = (props: IShippingLineUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { shippingLineEntity, shippingMethods, jorders, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/shipping-line' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getShippingMethods();
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
        ...shippingLineEntity,
        ...values,
        shippingmethod: shippingMethods.find(it => it.id.toString() === values.shippingmethodId.toString()),
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
          <h2 id="venjureApp.shippingLine.home.createOrEditLabel" data-cy="ShippingLineCreateUpdateHeading">
            <Translate contentKey="venjureApp.shippingLine.home.createOrEditLabel">Create or edit a ShippingLine</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : shippingLineEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="shipping-line-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="shipping-line-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="shipping-line-createdat">
                  <Translate contentKey="venjureApp.shippingLine.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="shipping-line-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.shippingLineEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="shipping-line-updatedat">
                  <Translate contentKey="venjureApp.shippingLine.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="shipping-line-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.shippingLineEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="listpriceLabel" for="shipping-line-listprice">
                  <Translate contentKey="venjureApp.shippingLine.listprice">Listprice</Translate>
                </Label>
                <AvField
                  id="shipping-line-listprice"
                  data-cy="listprice"
                  type="string"
                  className="form-control"
                  name="listprice"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="listpriceincludestaxLabel">
                  <AvInput
                    id="shipping-line-listpriceincludestax"
                    data-cy="listpriceincludestax"
                    type="checkbox"
                    className="form-check-input"
                    name="listpriceincludestax"
                  />
                  <Translate contentKey="venjureApp.shippingLine.listpriceincludestax">Listpriceincludestax</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="adjustmentsLabel" for="shipping-line-adjustments">
                  <Translate contentKey="venjureApp.shippingLine.adjustments">Adjustments</Translate>
                </Label>
                <AvField
                  id="shipping-line-adjustments"
                  data-cy="adjustments"
                  type="text"
                  name="adjustments"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="taxlinesLabel" for="shipping-line-taxlines">
                  <Translate contentKey="venjureApp.shippingLine.taxlines">Taxlines</Translate>
                </Label>
                <AvField
                  id="shipping-line-taxlines"
                  data-cy="taxlines"
                  type="text"
                  name="taxlines"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="shipping-line-shippingmethod">
                  <Translate contentKey="venjureApp.shippingLine.shippingmethod">Shippingmethod</Translate>
                </Label>
                <AvInput
                  id="shipping-line-shippingmethod"
                  data-cy="shippingmethod"
                  type="select"
                  className="form-control"
                  name="shippingmethodId"
                  required
                >
                  <option value="" key="0" />
                  {shippingMethods
                    ? shippingMethods.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="shipping-line-jorder">
                  <Translate contentKey="venjureApp.shippingLine.jorder">Jorder</Translate>
                </Label>
                <AvInput id="shipping-line-jorder" data-cy="jorder" type="select" className="form-control" name="jorderId">
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
              <Button tag={Link} id="cancel-save" to="/shipping-line" replace color="info">
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
  shippingMethods: storeState.shippingMethod.entities,
  jorders: storeState.jorder.entities,
  shippingLineEntity: storeState.shippingLine.entity,
  loading: storeState.shippingLine.loading,
  updating: storeState.shippingLine.updating,
  updateSuccess: storeState.shippingLine.updateSuccess,
});

const mapDispatchToProps = {
  getShippingMethods,
  getJorders,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ShippingLineUpdate);
