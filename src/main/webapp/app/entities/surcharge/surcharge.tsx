import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './surcharge.reducer';
import { ISurcharge } from 'app/shared/model/surcharge.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface ISurchargeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Surcharge = (props: ISurchargeProps) => {
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get('sort');
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { surchargeList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="surcharge-heading" data-cy="SurchargeHeading">
        <Translate contentKey="venjureApp.surcharge.home.title">Surcharges</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="venjureApp.surcharge.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="venjureApp.surcharge.home.createLabel">Create new Surcharge</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {surchargeList && surchargeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="venjureApp.surcharge.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdat')}>
                  <Translate contentKey="venjureApp.surcharge.createdat">Createdat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedat')}>
                  <Translate contentKey="venjureApp.surcharge.updatedat">Updatedat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="venjureApp.surcharge.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('listprice')}>
                  <Translate contentKey="venjureApp.surcharge.listprice">Listprice</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('listpriceincludestax')}>
                  <Translate contentKey="venjureApp.surcharge.listpriceincludestax">Listpriceincludestax</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('sku')}>
                  <Translate contentKey="venjureApp.surcharge.sku">Sku</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('taxlines')}>
                  <Translate contentKey="venjureApp.surcharge.taxlines">Taxlines</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="venjureApp.surcharge.jorder">Jorder</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="venjureApp.surcharge.ordermodification">Ordermodification</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {surchargeList.map((surcharge, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${surcharge.id}`} color="link" size="sm">
                      {surcharge.id}
                    </Button>
                  </td>
                  <td>{surcharge.createdat ? <TextFormat type="date" value={surcharge.createdat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{surcharge.updatedat ? <TextFormat type="date" value={surcharge.updatedat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{surcharge.description}</td>
                  <td>{surcharge.listprice}</td>
                  <td>{surcharge.listpriceincludestax ? 'true' : 'false'}</td>
                  <td>{surcharge.sku}</td>
                  <td>{surcharge.taxlines}</td>
                  <td>{surcharge.jorder ? <Link to={`jorder/${surcharge.jorder.id}`}>{surcharge.jorder.id}</Link> : ''}</td>
                  <td>
                    {surcharge.ordermodification ? (
                      <Link to={`order-modification/${surcharge.ordermodification.id}`}>{surcharge.ordermodification.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${surcharge.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${surcharge.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${surcharge.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="venjureApp.surcharge.home.notFound">No Surcharges found</Translate>
            </div>
          )
        )}
      </div>
      {props.totalItems ? (
        <div className={surchargeList && surchargeList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={props.totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

const mapStateToProps = ({ surcharge }: IRootState) => ({
  surchargeList: surcharge.entities,
  loading: surcharge.loading,
  totalItems: surcharge.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Surcharge);
