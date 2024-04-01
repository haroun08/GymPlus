import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './product-history.reducer';

export const ProductHistory = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const productHistoryList = useAppSelector(state => state.productHistory.entities);
  const loading = useAppSelector(state => state.productHistory.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="product-history-heading" data-cy="ProductHistoryHeading">
        <Translate contentKey="gymPlusApp.productHistory.home.title">Product Histories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gymPlusApp.productHistory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product-history/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gymPlusApp.productHistory.home.createLabel">Create new Product History</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productHistoryList && productHistoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gymPlusApp.productHistory.id">Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="gymPlusApp.productHistory.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="gymPlusApp.productHistory.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="gymPlusApp.productHistory.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th className="hand" onClick={sort('availableStockQuantity')}>
                  <Translate contentKey="gymPlusApp.productHistory.availableStockQuantity">Available Stock Quantity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('availableStockQuantity')} />
                </th>
                <th className="hand" onClick={sort('validFrom')}>
                  <Translate contentKey="gymPlusApp.productHistory.validFrom">Valid From</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('validFrom')} />
                </th>
                <th className="hand" onClick={sort('validTo')}>
                  <Translate contentKey="gymPlusApp.productHistory.validTo">Valid To</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('validTo')} />
                </th>
                <th>
                  <Translate contentKey="gymPlusApp.productHistory.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productHistoryList.map((productHistory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-history/${productHistory.id}`} color="link" size="sm">
                      {productHistory.id}
                    </Button>
                  </td>
                  <td>{productHistory.name}</td>
                  <td>{productHistory.description}</td>
                  <td>{productHistory.price}</td>
                  <td>{productHistory.availableStockQuantity}</td>
                  <td>
                    {productHistory.validFrom ? <TextFormat type="date" value={productHistory.validFrom} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {productHistory.validTo ? <TextFormat type="date" value={productHistory.validTo} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {productHistory.product ? <Link to={`/product/${productHistory.product.id}`}>{productHistory.product.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/product-history/${productHistory.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product-history/${productHistory.id}/edit`}
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
                        onClick={() => (window.location.href = `/product-history/${productHistory.id}/delete`)}
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
              <Translate contentKey="gymPlusApp.productHistory.home.notFound">No Product Histories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProductHistory;
