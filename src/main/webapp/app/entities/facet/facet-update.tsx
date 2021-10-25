import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IChannel } from 'app/shared/model/channel.model';
import { getEntities as getChannels } from 'app/entities/channel/channel.reducer';
import { getEntity, updateEntity, createEntity, reset } from './facet.reducer';
import { IFacet } from 'app/shared/model/facet.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFacetUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacetUpdate = (props: IFacetUpdateProps) => {
  const [idschannel, setIdschannel] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { facetEntity, channels, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/facet' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

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

    if (errors.length === 0) {
      const entity = {
        ...facetEntity,
        ...values,
        channels: mapIdList(values.channels),
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
          <h2 id="venjureApp.facet.home.createOrEditLabel" data-cy="FacetCreateUpdateHeading">
            <Translate contentKey="venjureApp.facet.home.createOrEditLabel">Create or edit a Facet</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : facetEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="facet-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="facet-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="createdatLabel" for="facet-createdat">
                  <Translate contentKey="venjureApp.facet.createdat">Createdat</Translate>
                </Label>
                <AvInput
                  id="facet-createdat"
                  data-cy="createdat"
                  type="datetime-local"
                  className="form-control"
                  name="createdat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.facetEntity.createdat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedatLabel" for="facet-updatedat">
                  <Translate contentKey="venjureApp.facet.updatedat">Updatedat</Translate>
                </Label>
                <AvInput
                  id="facet-updatedat"
                  data-cy="updatedat"
                  type="datetime-local"
                  className="form-control"
                  name="updatedat"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.facetEntity.updatedat)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="isprivateLabel">
                  <AvInput id="facet-isprivate" data-cy="isprivate" type="checkbox" className="form-check-input" name="isprivate" />
                  <Translate contentKey="venjureApp.facet.isprivate">Isprivate</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="codeLabel" for="facet-code">
                  <Translate contentKey="venjureApp.facet.code">Code</Translate>
                </Label>
                <AvField
                  id="facet-code"
                  data-cy="code"
                  type="text"
                  name="code"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="facet-channel">
                  <Translate contentKey="venjureApp.facet.channel">Channel</Translate>
                </Label>
                <AvInput
                  id="facet-channel"
                  data-cy="channel"
                  type="select"
                  multiple
                  className="form-control"
                  name="channels"
                  value={!isNew && facetEntity.channels && facetEntity.channels.map(e => e.id)}
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
              <Button tag={Link} id="cancel-save" to="/facet" replace color="info">
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
  channels: storeState.channel.entities,
  facetEntity: storeState.facet.entity,
  loading: storeState.facet.loading,
  updating: storeState.facet.updating,
  updateSuccess: storeState.facet.updateSuccess,
});

const mapDispatchToProps = {
  getChannels,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacetUpdate);
