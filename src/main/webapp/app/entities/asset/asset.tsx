import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './asset.reducer';
import { IAsset } from 'app/shared/model/asset.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IAssetProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Asset = (props: IAssetProps) => {
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

  const { assetList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="asset-heading" data-cy="AssetHeading">
        <Translate contentKey="venjureApp.asset.home.title">Assets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="venjureApp.asset.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="venjureApp.asset.home.createLabel">Create new Asset</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {assetList && assetList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="venjureApp.asset.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdat')}>
                  <Translate contentKey="venjureApp.asset.createdat">Createdat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedat')}>
                  <Translate contentKey="venjureApp.asset.updatedat">Updatedat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="venjureApp.asset.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="venjureApp.asset.type">Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('mimetype')}>
                  <Translate contentKey="venjureApp.asset.mimetype">Mimetype</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('width')}>
                  <Translate contentKey="venjureApp.asset.width">Width</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('height')}>
                  <Translate contentKey="venjureApp.asset.height">Height</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('filesize')}>
                  <Translate contentKey="venjureApp.asset.filesize">Filesize</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('source')}>
                  <Translate contentKey="venjureApp.asset.source">Source</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('preview')}>
                  <Translate contentKey="venjureApp.asset.preview">Preview</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('focalpoint')}>
                  <Translate contentKey="venjureApp.asset.focalpoint">Focalpoint</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {assetList.map((asset, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${asset.id}`} color="link" size="sm">
                      {asset.id}
                    </Button>
                  </td>
                  <td>{asset.createdat ? <TextFormat type="date" value={asset.createdat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{asset.updatedat ? <TextFormat type="date" value={asset.updatedat} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{asset.name}</td>
                  <td>{asset.type}</td>
                  <td>{asset.mimetype}</td>
                  <td>{asset.width}</td>
                  <td>{asset.height}</td>
                  <td>{asset.filesize}</td>
                  <td>{asset.source}</td>
                  <td>{asset.preview}</td>
                  <td>{asset.focalpoint}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${asset.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${asset.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`${match.url}/${asset.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="venjureApp.asset.home.notFound">No Assets found</Translate>
            </div>
          )
        )}
      </div>
      {props.totalItems ? (
        <div className={assetList && assetList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ asset }: IRootState) => ({
  assetList: asset.entities,
  loading: asset.loading,
  totalItems: asset.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Asset);
