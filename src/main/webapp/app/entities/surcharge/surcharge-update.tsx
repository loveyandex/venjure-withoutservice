import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IJorder } from 'app/shared/model/jorder.model';
import { getEntities as getJorders } from 'app/entities/jorder/jorder.reducer';
import { IOrderModification } from 'app/shared/model/order-modification.model';
import { getEntities as getOrderModifications } from 'app/entities/order-modification/order-modification.reducer';
import { getEntity, updateEntity, createEntity, reset } from './surcharge.reducer';
import { ISurcharge } from 'app/shared/model/surcharge.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISurchargeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SurchargeUpdate = (props: ISurchargeUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { surchargeEntity, jorders, orderModifications, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/surcharge' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getJorders();
    props.getOrderModifications();
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
        ...surchargeEntity,
        ...values,
        jorder: jorders.find(it => it.id.toString() === values.jorderId.toString()),
        ordermodification: orderModifications.find(it => it.id.toString() === values.ordermodificationId.toString()),
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
          <h2 id="venjureApp.surcharge.home.createOrEditLabel" data-cy="SurchargeCreateUpdateHeading">
            <Translate contentKey="venjureApp.surcharge.home.createOrEditLabel">Create or edit a Surcharge</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : surchargeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="surcharge-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="surcharge-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="surcharge-createdat">
                  <Translate contentKey="venjureApp.surcharge.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="surcharge-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.surchargeEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="surcharge-updatedat">
                  <Translate contentKey="venjureApp.surcharge.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="surcharge-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.surchargeEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="surcharge-description">
                  <Translate contentKey="venjureApp.surcharge.description">Description</Translate>
                </Label>
                <AvField
                  id="surcharge-description"
                  data-cy="description"
                  type="text"
                  name="description"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="listpriceLabel" for="surcharge-listprice">
                  <Translate contentKey="venjureApp.surcharge.listprice">Listprice</Translate>
                </Label>
                <AvField
                  id="surcharge-listprice"
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
                    id="surcharge-listpriceincludestax"
                    data-cy="listpriceincludestax"
                    type="checkbox"
                    className="form-check-input"
                    name="listpriceincludestax"
                  />
                  <Translate contentKey="venjureApp.surcharge.listpriceincludestax">Listpriceincludestax</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="skuLabel" for="surcharge-sku">
                  <Translate contentKey="venjureApp.surcharge.sku">Sku</Translate>
                </Label>
                <AvField
                  id="surcharge-sku"
                  data-cy="sku"
                  type="text"
                  name="sku"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="taxlinesLabel" for="surcharge-taxlines">
                  <Translate contentKey="venjureApp.surcharge.taxlines">Taxlines</Translate>
                </Label>
                <AvField
                  id="surcharge-taxlines"
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
                <Label for="surcharge-jorder">
                  <Translate contentKey="venjureApp.surcharge.jorder">Jorder</Translate>
                </Label>
                <AvInput id="surcharge-jorder" data-cy="jorder" type="select" className="form-control" name="jorderId">
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
              <AvGroup>
                <Label for="surcharge-ordermodification">
                  <Translate contentKey="venjureApp.surcharge.ordermodification">Ordermodification</Translate>
                </Label>
                <AvInput
                  id="surcharge-ordermodification"
                  data-cy="ordermodification"
                  type="select"
                  className="form-control"
                  name="ordermodificationId"
                >
                  <option value="" key="0" />
                  {orderModifications
                    ? orderModifications.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/surcharge" replace color="info">
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
  jorders: storeState.jorder.entities,
  orderModifications: storeState.orderModification.entities,
  surchargeEntity: storeState.surcharge.entity,
  loading: storeState.surcharge.loading,
  updating: storeState.surcharge.updating,
  updateSuccess: storeState.surcharge.updateSuccess,
});

const mapDispatchToProps = {
  getJorders,
  getOrderModifications,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SurchargeUpdate);
