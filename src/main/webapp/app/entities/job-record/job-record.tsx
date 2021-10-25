import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './job-record.reducer';
import { IJobRecord } from 'app/shared/model/job-record.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IJobRecordProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const JobRecord = (props: IJobRecordProps) => {
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

  const { jobRecordList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="job-record-heading" data-cy="JobRecordHeading">
        <Translate contentKey="venjureApp.jobRecord.home.title">Job Records</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="venjureApp.jobRecord.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="venjureApp.jobRecord.home.createLabel">Create new Job Record</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {jobRecordList && jobRecordList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="venjureApp.jobRecord.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdat')}>
                  <Translate contentKey="venjureApp.jobRecord.createdat">Createdat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedat')}>
                  <Translate contentKey="venjureApp.jobRecord.updatedat">Updatedat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('queuename')}>
                  <Translate contentKey="venjureApp.jobRecord.queuename">Queuename</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('data')}>
                  <Translate contentKey="venjureApp.jobRecord.data">Data</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('state')}>
                  <Translate contentKey="venjureApp.jobRecord.state">State</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('progress')}>
                  <Translate contentKey="venjureApp.jobRecord.progress">Progress</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('result')}>
                  <Translate contentKey="venjureApp.jobRecord.result">Result</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('error')}>
                  <Translate contentKey="venjureApp.jobRecord.error">Error</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('startedat')}>
                  <Translate contentKey="venjureApp.jobRecord.startedat">Startedat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('settledat')}>
                  <Translate contentKey="venjureApp.jobRecord.settledat">Settledat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('issettled')}>
                  <Translate contentKey="venjureApp.jobRecord.issettled">Issettled</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('retries')}>
                  <Translate contentKey="venjureApp.jobRecord.retries">Retries</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('attempts')}>
                  <Translate contentKey="venjureApp.jobRecord.attempts">Attempts</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {jobRecordList.map((jobRecord, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${jobRecord.id}`} color="link" size="sm">
                      {jobRecord.id}
                    </Button>
                  </td>
                  <td>{jobRecord.createdat ? <TextFormat type="date" value={jobRecord.createdat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{jobRecord.updatedat ? <TextFormat type="date" value={jobRecord.updatedat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{jobRecord.queuename}</td>
                  <td>{jobRecord.data}</td>
                  <td>{jobRecord.state}</td>
                  <td>{jobRecord.progress}</td>
                  <td>{jobRecord.result}</td>
                  <td>{jobRecord.error}</td>
                  <td>{jobRecord.startedat ? <TextFormat type="date" value={jobRecord.startedat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{jobRecord.settledat ? <TextFormat type="date" value={jobRecord.settledat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{jobRecord.issettled ? 'true' : 'false'}</td>
                  <td>{jobRecord.retries}</td>
                  <td>{jobRecord.attempts}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${jobRecord.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${jobRecord.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`${match.url}/${jobRecord.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="venjureApp.jobRecord.home.notFound">No Job Records found</Translate>
            </div>
          )
        )}
      </div>
      {props.totalItems ? (
        <div className={jobRecordList && jobRecordList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ jobRecord }: IRootState) => ({
  jobRecordList: jobRecord.entities,
  loading: jobRecord.loading,
  totalItems: jobRecord.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(JobRecord);
