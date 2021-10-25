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
import { IChannel } from 'app/shared/model/channel.model';
import { getEntities as getChannels } from 'app/entities/channel/channel.reducer';
import { getEntity, updateEntity, createEntity, reset } from './promotion.reducer';
import { IPromotion } from 'app/shared/model/promotion.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPromotionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PromotionUpdate = (props: IPromotionUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { promotionEntity, jorders, channels, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/promotion' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getJorders();
    props.getChannels();
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
    values.startsat = convertDateTimeToServer(values.startsat);
    values.endsat = convertDateTimeToServer(values.endsat);

    if (errors.length === 0) {
      const entity = {
        ...promotionEntity,
        ...values,
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
          <h2 id="venjureApp.promotion.home.createOrEditLabel" data-cy="PromotionCreateUpdateHeading">
            <Translate contentKey="venjureApp.promotion.home.createOrEditLabel">Create or edit a Promotion</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : promotionEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="promotion-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="promotion-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="promotion-createdat">
                  <Translate contentKey="venjureApp.promotion.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="promotion-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.promotionEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="promotion-updatedat">
                  <Translate contentKey="venjureApp.promotion.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="promotion-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.promotionEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="deletedatLabel" for="promotion-deletedat">
                  <Translate contentKey="venjureApp.promotion.deletedat">Deletedat</Translate>
                </Label>
                <AvInput
                  id="promotion-deletedat"
                  data-cy="deletedat"
                  type="datetime-local"
                  className="form-control"
                  name="deletedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.promotionEntity.deletedat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="startsatLabel" for="promotion-startsat">
                  <Translate contentKey="venjureApp.promotion.startsat">Startsat</Translate>
                </Label>
                <AvInput
                  id="promotion-startsat"
                  data-cy="startsat"
                  type="datetime-local"
                  className="form-control"
                  name="startsat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.promotionEntity.startsat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endsatLabel" for="promotion-endsat">
                  <Translate contentKey="venjureApp.promotion.endsat">Endsat</Translate>
                </Label>
                <AvInput
                  id="promotion-endsat"
                  data-cy="endsat"
                  type="datetime-local"
                  className="form-control"
                  name="endsat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.promotionEntity.endsat)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="couponcodeLabel" for="promotion-couponcode">
                  <Translate contentKey="venjureApp.promotion.couponcode">Couponcode</Translate>
                </Label>
                <AvField
                  id="promotion-couponcode"
                  data-cy="couponcode"
                  type="text"
                  name="couponcode"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="percustomerusagelimitLabel" for="promotion-percustomerusagelimit">
                  <Translate contentKey="venjureApp.promotion.percustomerusagelimit">Percustomerusagelimit</Translate>
                </Label>
                <AvField
                  id="promotion-percustomerusagelimit"
                  data-cy="percustomerusagelimit"
                  type="string"
                  className="form-control"
                  name="percustomerusagelimit"
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameLabel" for="promotion-name">
                  <Translate contentKey="venjureApp.promotion.name">Name</Translate>
                </Label>
                <AvField
                  id="promotion-name"
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
                  <AvInput id="promotion-enabled" data-cy="enabled" type="checkbox" className="form-check-input" name="enabled" />
                  <Translate contentKey="venjureApp.promotion.enabled">Enabled</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="conditionsLabel" for="promotion-conditions">
                  <Translate contentKey="venjureApp.promotion.conditions">Conditions</Translate>
                </Label>
                <AvField
                  id="promotion-conditions"
                  data-cy="conditions"
                  type="text"
                  name="conditions"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="actionsLabel" for="promotion-actions">
                  <Translate contentKey="venjureApp.promotion.actions">Actions</Translate>
                </Label>
                <AvField
                  id="promotion-actions"
                  data-cy="actions"
                  type="text"
                  name="actions"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="priorityscoreLabel" for="promotion-priorityscore">
                  <Translate contentKey="venjureApp.promotion.priorityscore">Priorityscore</Translate>
                </Label>
                <AvField
                  id="promotion-priorityscore"
                  data-cy="priorityscore"
                  type="string"
                  className="form-control"
                  name="priorityscore"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/promotion" replace color="info">
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
  channels: storeState.channel.entities,
  promotionEntity: storeState.promotion.entity,
  loading: storeState.promotion.loading,
  updating: storeState.promotion.updating,
  updateSuccess: storeState.promotion.updateSuccess,
});

const mapDispatchToProps = {
  getJorders,
  getChannels,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PromotionUpdate);
