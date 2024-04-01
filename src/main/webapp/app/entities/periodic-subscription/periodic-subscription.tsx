import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './periodic-subscription.reducer';

export const PeriodicSubscription = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const periodicSubscriptionList = useAppSelector(state => state.periodicSubscription.entities);
  const loading = useAppSelector(state => state.periodicSubscription.loading);

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
      <h2 id="periodic-subscription-heading" data-cy="PeriodicSubscriptionHeading">
        <Translate contentKey="gymPlusApp.periodicSubscription.home.title">Periodic Subscriptions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gymPlusApp.periodicSubscription.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/periodic-subscription/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gymPlusApp.periodicSubscription.home.createLabel">Create new Periodic Subscription</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {periodicSubscriptionList && periodicSubscriptionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gymPlusApp.periodicSubscription.id">Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="gymPlusApp.periodicSubscription.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="gymPlusApp.periodicSubscription.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="gymPlusApp.periodicSubscription.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('paymentStatus')}>
                  <Translate contentKey="gymPlusApp.periodicSubscription.paymentStatus">Payment Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentStatus')} />
                </th>
                <th>
                  <Translate contentKey="gymPlusApp.periodicSubscription.id">Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="gymPlusApp.periodicSubscription.plans">Plans</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="gymPlusApp.periodicSubscription.invoices">Invoices</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {periodicSubscriptionList.map((periodicSubscription, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/periodic-subscription/${periodicSubscription.id}`} color="link" size="sm">
                      {periodicSubscription.id}
                    </Button>
                  </td>
                  <td>{periodicSubscription.name}</td>
                  <td>{periodicSubscription.description}</td>
                  <td>{periodicSubscription.status ? 'true' : 'false'}</td>
                  <td>{periodicSubscription.paymentStatus ? 'true' : 'false'}</td>
                  <td>
                    {periodicSubscription.ids
                      ? periodicSubscription.ids.map((val, j) => (
                          <span key={j}>
                            {val.id}
                            {j === periodicSubscription.ids.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {periodicSubscription.plans ? (
                      <Link to={`/plan/${periodicSubscription.plans.id}`}>{periodicSubscription.plans.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {periodicSubscription.invoices ? (
                      <Link to={`/invoice/${periodicSubscription.invoices.id}`}>{periodicSubscription.invoices.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/periodic-subscription/${periodicSubscription.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/periodic-subscription/${periodicSubscription.id}/edit`}
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
                        onClick={() => (window.location.href = `/periodic-subscription/${periodicSubscription.id}/delete`)}
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
              <Translate contentKey="gymPlusApp.periodicSubscription.home.notFound">No Periodic Subscriptions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PeriodicSubscription;
