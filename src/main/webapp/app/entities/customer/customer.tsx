import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './customer.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface ICustomerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Customer = (props: ICustomerProps) => {
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

  const { customerList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="customer-heading" data-cy="CustomerHeading">
        <Translate contentKey="venjureApp.customer.home.title">Customers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="venjureApp.customer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="venjureApp.customer.home.createLabel">Create new Customer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {customerList && customerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="venjureApp.customer.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdat')}>
                  <Translate contentKey="venjureApp.customer.createdat">Createdat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedat')}>
                  <Translate contentKey="venjureApp.customer.updatedat">Updatedat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('deletedat')}>
                  <Translate contentKey="venjureApp.customer.deletedat">Deletedat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('title')}>
                  <Translate contentKey="venjureApp.customer.title">Title</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('firstname')}>
                  <Translate contentKey="venjureApp.customer.firstname">Firstname</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastname')}>
                  <Translate contentKey="venjureApp.customer.lastname">Lastname</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('phonenumber')}>
                  <Translate contentKey="venjureApp.customer.phonenumber">Phonenumber</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('emailaddress')}>
                  <Translate contentKey="venjureApp.customer.emailaddress">Emailaddress</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="venjureApp.customer.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="venjureApp.customer.avatar">Avatar</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerList.map((customer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${customer.id}`} color="link" size="sm">
                      {customer.id}
                    </Button>
                  </td>
                  <td>{customer.createdat ? <TextFormat type="date" value={customer.createdat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{customer.updatedat ? <TextFormat type="date" value={customer.updatedat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{customer.deletedat ? <TextFormat type="date" value={customer.deletedat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{customer.title}</td>
                  <td>{customer.firstname}</td>
                  <td>{customer.lastname}</td>
                  <td>{customer.phonenumber}</td>
                  <td>{customer.emailaddress}</td>
                  <td>{customer.user ? customer.user.id : ''}</td>
                  <td>{customer.avatar ? <Link to={`asset/${customer.avatar.id}`}>{customer.avatar.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${customer.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`${match.url}/${customer.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="venjureApp.customer.home.notFound">No Customers found</Translate>
            </div>
          )
        )}
      </div>
      {props.totalItems ? (
        <div className={customerList && customerList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ customer }: IRootState) => ({
  customerList: customer.entities,
  loading: customer.loading,
  totalItems: customer.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Customer);
