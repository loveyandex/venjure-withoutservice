import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAdministrator, defaultValue } from 'app/shared/model/administrator.model';

export const ACTION_TYPES = {
  FETCH_ADMINISTRATOR_LIST: 'administrator/FETCH_ADMINISTRATOR_LIST',
  FETCH_ADMINISTRATOR: 'administrator/FETCH_ADMINISTRATOR',
  CREATE_ADMINISTRATOR: 'administrator/CREATE_ADMINISTRATOR',
  UPDATE_ADMINISTRATOR: 'administrator/UPDATE_ADMINISTRATOR',
  PARTIAL_UPDATE_ADMINISTRATOR: 'administrator/PARTIAL_UPDATE_ADMINISTRATOR',
  DELETE_ADMINISTRATOR: 'administrator/DELETE_ADMINISTRATOR',
  RESET: 'administrator/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAdministrator>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AdministratorState = Readonly<typeof initialState>;

// Reducer

export default (state: AdministratorState = initialState, action): AdministratorState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ADMINISTRATOR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ADMINISTRATOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ADMINISTRATOR):
    case REQUEST(ACTION_TYPES.UPDATE_ADMINISTRATOR):
    case REQUEST(ACTION_TYPES.DELETE_ADMINISTRATOR):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ADMINISTRATOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ADMINISTRATOR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ADMINISTRATOR):
    case FAILURE(ACTION_TYPES.CREATE_ADMINISTRATOR):
    case FAILURE(ACTION_TYPES.UPDATE_ADMINISTRATOR):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ADMINISTRATOR):
    case FAILURE(ACTION_TYPES.DELETE_ADMINISTRATOR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ADMINISTRATOR_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ADMINISTRATOR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ADMINISTRATOR):
    case SUCCESS(ACTION_TYPES.UPDATE_ADMINISTRATOR):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ADMINISTRATOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ADMINISTRATOR):
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

const apiUrl = 'api/administrators';

// Actions

export const getEntities: ICrudGetAllAction<IAdministrator> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ADMINISTRATOR_LIST,
    payload: axios.get<IAdministrator>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAdministrator> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ADMINISTRATOR,
    payload: axios.get<IAdministrator>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAdministrator> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ADMINISTRATOR,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAdministrator> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ADMINISTRATOR,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IAdministrator> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ADMINISTRATOR,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAdministrator> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ADMINISTRATOR,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
