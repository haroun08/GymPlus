import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './gym.reducer';

export const Gym = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const gymList = useAppSelector(state => state.gym.entities);
  const loading = useAppSelector(state => state.gym.loading);

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
      <h2 id="gym-heading" data-cy="GymHeading">
        <Translate contentKey="gymPlusApp.gym.home.title">Gyms</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gymPlusApp.gym.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/gym/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gymPlusApp.gym.home.createLabel">Create new Gym</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gymList && gymList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gymPlusApp.gym.id">Id</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="gymPlusApp.gym.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('streetAddress')}>
                  <Translate contentKey="gymPlusApp.gym.streetAddress">Street Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('streetAddress')} />
                </th>
                <th className="hand" onClick={sort('postalCode')}>
                  <Translate contentKey="gymPlusApp.gym.postalCode">Postal Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postalCode')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  <Translate contentKey="gymPlusApp.gym.city">City</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th className="hand" onClick={sort('stateProvince')}>
                  <Translate contentKey="gymPlusApp.gym.stateProvince">State Province</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stateProvince')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="gymPlusApp.gym.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th>
                  <Translate contentKey="gymPlusApp.gym.id">Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gymList.map((gym, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/gym/${gym.id}`} color="link" size="sm">
                      {gym.id}
                    </Button>
                  </td>
                  <td>{gym.name}</td>
                  <td>{gym.streetAddress}</td>
                  <td>{gym.postalCode}</td>
                  <td>{gym.city}</td>
                  <td>{gym.stateProvince}</td>
                  <td>{gym.phoneNumber}</td>
                  <td>
                    {gym.ids
                      ? gym.ids.map((val, j) => (
                          <span key={j}>
                            {val.id}
                            {j === gym.ids.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/gym/${gym.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/gym/${gym.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/gym/${gym.id}/delete`)}
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
              <Translate contentKey="gymPlusApp.gym.home.notFound">No Gyms found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Gym;
