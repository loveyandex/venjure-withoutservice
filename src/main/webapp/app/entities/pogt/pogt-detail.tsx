import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './pogt.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPogtDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PogtDetail = (props: IPogtDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { pogtEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pogtDetailsHeading">
          <Translate contentKey="venjureApp.pogt.detail.title">Pogt</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pogtEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.pogt.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>{pogtEntity.createdat ? <TextFormat value={pogtEntity.createdat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.pogt.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>{pogtEntity.updatedat ? <TextFormat value={pogtEntity.updatedat} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="languagecode">
              <Translate contentKey="venjureApp.pogt.languagecode">Languagecode</Translate>
            </span>
          </dt>
          <dd>{pogtEntity.languagecode}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.pogt.name">Name</Translate>
            </span>
          </dt>
          <dd>{pogtEntity.name}</dd>
          <dt>
            <Translate contentKey="venjureApp.pogt.base">Base</Translate>
          </dt>
          <dd>{pogtEntity.base ? pogtEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pogt" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pogt/${pogtEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ pogt }: IRootState) => ({
  pogtEntity: pogt.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PogtDetail);
