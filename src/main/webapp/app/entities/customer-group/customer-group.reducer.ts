import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomerGroup, defaultValue } from 'app/shared/model/customer-group.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMERGROUP_LIST: 'customerGroup/FETCH_CUSTOMERGROUP_LIST',
  FETCH_CUSTOMERGROUP: 'customerGroup/FETCH_CUSTOMERGROUP',
  CREATE_CUSTOMERGROUP: 'customerGroup/CREATE_CUSTOMERGROUP',
  UPDATE_CUSTOMERGROUP: 'customerGroup/UPDATE_CUSTOMERGROUP',
  PARTIAL_UPDATE_CUSTOMERGROUP: 'customerGroup/PARTIAL_UPDATE_CUSTOMERGROUP',
  DELETE_CUSTOMERGROUP: 'customerGroup/DELETE_CUSTOMERGROUP',
  RESET: 'customerGroup/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomerGroup>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CustomerGroupState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerGroupState = initialState, action): CustomerGroupState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERGROUP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERGROUP):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERGROUP):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERGROUP):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERGROUP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERGROUP):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERGROUP):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERGROUP):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERGROUP):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERGROUP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERGROUP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERGROUP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERGROUP):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERGROUP):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERGROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERGROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/customer-groups';

// Actions

export const getEntities: ICrudGetAllAction<ICustomerGroup> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERGROUP_LIST,
    payload: axios.get<ICustomerGroup>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICustomerGroup> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERGROUP,
    payload: axios.get<ICustomerGroup>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICustomerGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERGROUP,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomerGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERGROUP,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICustomerGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_CUSTOMERGROUP,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomerGroup> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERGROUP,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
