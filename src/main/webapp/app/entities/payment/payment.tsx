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

import { getEntities } from './payment.reducer';

export const Payment = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const paymentList = useAppSelector(state => state.payment.entities);
  const loading = useAppSelector(state => state.payment.loading);

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
      <h2 id="payment-heading" data-cy="PaymentHeading">
        <Translate contentKey="gymPlusApp.payment.home.title">Payments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gymPlusApp.payment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/payment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gymPlusApp.payment.home.createLabel">Create new Payment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {paymentList && paymentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gymPlusApp.payment.id">Id</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="gymPlusApp.payment.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('paymentDate')}>
                  <Translate contentKey="gymPlusApp.payment.paymentDate">Payment Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentDate')} />
                </th>
                <th>
                  <Translate contentKey="gymPlusApp.payment.invoice">Invoice</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="gymPlusApp.payment.order">Order</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paymentList.map((payment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/payment/${payment.id}`} color="link" size="sm">
                      {payment.id}
                    </Button>
                  </td>
                  <td>{payment.status ? 'true' : 'false'}</td>
                  <td>{payment.paymentDate ? <TextFormat type="date" value={payment.paymentDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{payment.invoice ? <Link to={`/invoice/${payment.invoice.id}`}>{payment.invoice.id}</Link> : ''}</td>
                  <td>{payment.order ? <Link to={`/order/${payment.order.id}`}>{payment.order.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/payment/${payment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/payment/${payment.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/payment/${payment.id}/delete`)}
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
              <Translate contentKey="gymPlusApp.payment.home.notFound">No Payments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Payment;
