import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './product-variant.reducer';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface IProductVariantProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ProductVariant = (props: IProductVariantProps) => {
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

  const { productVariantList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="product-variant-heading" data-cy="ProductVariantHeading">
        <Translate contentKey="venjureApp.productVariant.home.title">Product Variants</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="venjureApp.productVariant.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="venjureApp.productVariant.home.createLabel">Create new Product Variant</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productVariantList && productVariantList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="venjureApp.productVariant.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdat')}>
                  <Translate contentKey="venjureApp.productVariant.createdat">Createdat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedat')}>
                  <Translate contentKey="venjureApp.productVariant.updatedat">Updatedat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('deletedat')}>
                  <Translate contentKey="venjureApp.productVariant.deletedat">Deletedat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('enabled')}>
                  <Translate contentKey="venjureApp.productVariant.enabled">Enabled</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('sku')}>
                  <Translate contentKey="venjureApp.productVariant.sku">Sku</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('stockonhand')}>
                  <Translate contentKey="venjureApp.productVariant.stockonhand">Stockonhand</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('stockallocated')}>
                  <Translate contentKey="venjureApp.productVariant.stockallocated">Stockallocated</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('outofstockthreshold')}>
                  <Translate contentKey="venjureApp.productVariant.outofstockthreshold">Outofstockthreshold</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('useglobaloutofstockthreshold')}>
                  <Translate contentKey="venjureApp.productVariant.useglobaloutofstockthreshold">Useglobaloutofstockthreshold</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('trackinventory')}>
                  <Translate contentKey="venjureApp.productVariant.trackinventory">Trackinventory</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="venjureApp.productVariant.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="venjureApp.productVariant.featuredasset">Featuredasset</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="venjureApp.productVariant.taxcategory">Taxcategory</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productVariantList.map((productVariant, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${productVariant.id}`} color="link" size="sm">
                      {productVariant.id}
                    </Button>
                  </td>
                  <td>
                    {productVariant.createdat ? <TextFormat type="date" value={productVariant.createdat} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {productVariant.updatedat ? <TextFormat type="date" value={productVariant.updatedat} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {productVariant.deletedat ? <TextFormat type="date" value={productVariant.deletedat} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{productVariant.enabled ? 'true' : 'false'}</td>
                  <td>{productVariant.sku}</td>
                  <td>{productVariant.stockonhand}</td>
                  <td>{productVariant.stockallocated}</td>
                  <td>{productVariant.outofstockthreshold}</td>
                  <td>{productVariant.useglobaloutofstockthreshold ? 'true' : 'false'}</td>
                  <td>{productVariant.trackinventory}</td>
                  <td>
                    {productVariant.product ? <Link to={`product/${productVariant.product.id}`}>{productVariant.product.id}</Link> : ''}
                  </td>
                  <td>
                    {productVariant.featuredasset ? (
                      <Link to={`asset/${productVariant.featuredasset.id}`}>{productVariant.featuredasset.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {productVariant.taxcategory ? (
                      <Link to={`tax-category/${productVariant.taxcategory.id}`}>{productVariant.taxcategory.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${productVariant.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${productVariant.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`${match.url}/${productVariant.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="venjureApp.productVariant.home.notFound">No Product Variants found</Translate>
            </div>
          )
        )}
      </div>
      {props.totalItems ? (
        <div className={productVariantList && productVariantList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ productVariant }: IRootState) => ({
  productVariantList: productVariant.entities,
  loading: productVariant.loading,
  totalItems: productVariant.totalItems,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariant);
