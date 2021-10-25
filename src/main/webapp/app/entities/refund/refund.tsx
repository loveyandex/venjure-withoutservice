import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './refund.reducer';
import { IRefund } from 'app/shared/model/refund.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IRefundProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Refund = (props: IRefundProps) => {
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

  const { refundList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="refund-heading" data-cy="RefundHeading">
        <Translate contentKey="venjureApp.refund.home.title">Refunds</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="venjureApp.refund.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="venjureApp.refund.home.createLabel">Create new Refund</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {refundList && refundList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="venjureApp.refund.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdat')}>
                  <Translate contentKey="venjureApp.refund.createdat">Createdat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedat')}>
                  <Translate contentKey="venjureApp.refund.updatedat">Updatedat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('items')}>
                  <Translate contentKey="venjureApp.refund.items">Items</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('shipping')}>
                  <Translate contentKey="venjureApp.refund.shipping">Shipping</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('adjustment')}>
                  <Translate contentKey="venjureApp.refund.adjustment">Adjustment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('total')}>
                  <Translate contentKey="venjureApp.refund.total">Total</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('method')}>
                  <Translate contentKey="venjureApp.refund.method">Method</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('reason')}>
                  <Translate contentKey="venjureApp.refund.reason">Reason</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('state')}>
                  <Translate contentKey="venjureApp.refund.state">State</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('transactionid')}>
                  <Translate contentKey="venjureApp.refund.transactionid">Transactionid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('metadata')}>
                  <Translate contentKey="venjureApp.refund.metadata">Metadata</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="venjureApp.refund.payment">Payment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {refundList.map((refund, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${refund.id}`} color="link" size="sm">
                      {refund.id}
                    </Button>
                  </td>
                  <td>{refund.createdat ? <TextFormat type="date" value={refund.createdat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{refund.updatedat ? <TextFormat type="date" value={refund.updatedat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{refund.items}</td>
                  <td>{refund.shipping}</td>
                  <td>{refund.adjustment}</td>
                  <td>{refund.total}</td>
                  <td>{refund.method}</td>
                  <td>{refund.reason}</td>
                  <td>{refund.state}</td>
                  <td>{refund.transactionid}</td>
                  <td>{refund.metadata}</td>
                  <td>{refund.payment ? <Link to={`payment/${refund.payment.id}`}>{refund.payment.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${refund.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${refund.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`${match.url}/${refund.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="venjureApp.refund.home.notFound">No Refunds found</Translate>
            </div>
          )
        )}
      </div>
      {props.totalItems ? (
        <div className={refundList && refundList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ refund }: IRootState) => ({
  refundList: refund.entities,
  loading: refund.loading,
  totalItems: refund.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Refund);
