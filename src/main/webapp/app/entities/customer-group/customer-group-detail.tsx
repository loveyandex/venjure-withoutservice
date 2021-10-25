import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer-group.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerGroupDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CustomerGroupDetail = (props: ICustomerGroupDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { customerGroupEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerGroupDetailsHeading">
          <Translate contentKey="venjureApp.customerGroup.detail.title">CustomerGroup</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerGroupEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.customerGroup.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {customerGroupEntity.createdat ? (
              <TextFormat value={customerGroupEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.customerGroup.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {customerGroupEntity.updatedat ? (
              <TextFormat value={customerGroupEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.customerGroup.name">Name</Translate>
            </span>
          </dt>
          <dd>{customerGroupEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/customer-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer-group/${customerGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ customerGroup }: IRootState) => ({
  customerGroupEntity: customerGroup.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerGroupDetail);
