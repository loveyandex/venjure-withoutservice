import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './administrator.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAdministratorDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AdministratorDetail = (props: IAdministratorDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { administratorEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="administratorDetailsHeading">
          <Translate contentKey="venjureApp.administrator.detail.title">Administrator</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{administratorEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.administrator.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {administratorEntity.createdat ? (
              <TextFormat value={administratorEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.administrator.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {administratorEntity.updatedat ? (
              <TextFormat value={administratorEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="deletedat">
              <Translate contentKey="venjureApp.administrator.deletedat">Deletedat</Translate>
            </span>
          </dt>
          <dd>
            {administratorEntity.deletedat ? (
              <TextFormat value={administratorEntity.deletedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="firstname">
              <Translate contentKey="venjureApp.administrator.firstname">Firstname</Translate>
            </span>
          </dt>
          <dd>{administratorEntity.firstname}</dd>
          <dt>
            <span id="lastname">
              <Translate contentKey="venjureApp.administrator.lastname">Lastname</Translate>
            </span>
          </dt>
          <dd>{administratorEntity.lastname}</dd>
          <dt>
            <span id="emailaddress">
              <Translate contentKey="venjureApp.administrator.emailaddress">Emailaddress</Translate>
            </span>
          </dt>
          <dd>{administratorEntity.emailaddress}</dd>
          <dt>
            <Translate contentKey="venjureApp.administrator.user">User</Translate>
          </dt>
          <dd>{administratorEntity.user ? administratorEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/administrator" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/administrator/${administratorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ administrator }: IRootState) => ({
  administratorEntity: administrator.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AdministratorDetail);
