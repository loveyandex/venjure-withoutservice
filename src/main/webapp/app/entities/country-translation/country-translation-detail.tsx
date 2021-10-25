import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './country-translation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICountryTranslationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CountryTranslationDetail = (props: ICountryTranslationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { countryTranslationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="countryTranslationDetailsHeading">
          <Translate contentKey="venjureApp.countryTranslation.detail.title">CountryTranslation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{countryTranslationEntity.id}</dd>
          <dt>
            <span id="createdat">
              <Translate contentKey="venjureApp.countryTranslation.createdat">Createdat</Translate>
            </span>
          </dt>
          <dd>
            {countryTranslationEntity.createdat ? (
              <TextFormat value={countryTranslationEntity.createdat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedat">
              <Translate contentKey="venjureApp.countryTranslation.updatedat">Updatedat</Translate>
            </span>
          </dt>
          <dd>
            {countryTranslationEntity.updatedat ? (
              <TextFormat value={countryTranslationEntity.updatedat} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="languagecode">
              <Translate contentKey="venjureApp.countryTranslation.languagecode">Languagecode</Translate>
            </span>
          </dt>
          <dd>{countryTranslationEntity.languagecode}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="venjureApp.countryTranslation.name">Name</Translate>
            </span>
          </dt>
          <dd>{countryTranslationEntity.name}</dd>
          <dt>
            <Translate contentKey="venjureApp.countryTranslation.base">Base</Translate>
          </dt>
          <dd>{countryTranslationEntity.base ? countryTranslationEntity.base.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/country-translation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/country-translation/${countryTranslationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ countryTranslation }: IRootState) => ({
  countryTranslationEntity: countryTranslation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CountryTranslationDetail);
