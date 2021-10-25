import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IJorder, defaultValue } from 'app/shared/model/jorder.model';

export const ACTION_TYPES = {
  FETCH_JORDER_LIST: 'jorder/FETCH_JORDER_LIST',
  FETCH_JORDER: 'jorder/FETCH_JORDER',
  CREATE_JORDER: 'jorder/CREATE_JORDER',
  UPDATE_JORDER: 'jorder/UPDATE_JORDER',
  PARTIAL_UPDATE_JORDER: 'jorder/PARTIAL_UPDATE_JORDER',
  DELETE_JORDER: 'jorder/DELETE_JORDER',
  RESET: 'jorder/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IJorder>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type JorderState = Readonly<typeof initialState>;

// Reducer

export default (state: JorderState = initialState, action): JorderState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_JORDER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_JORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_JORDER):
    case REQUEST(ACTION_TYPES.UPDATE_JORDER):
    case REQUEST(ACTION_TYPES.DELETE_JORDER):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_JORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_JORDER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_JORDER):
    case FAILURE(ACTION_TYPES.CREATE_JORDER):
    case FAILURE(ACTION_TYPES.UPDATE_JORDER):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_JORDER):
    case FAILURE(ACTION_TYPES.DELETE_JORDER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_JORDER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_JORDER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_JORDER):
    case SUCCESS(ACTION_TYPES.UPDATE_JORDER):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_JORDER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_JORDER):
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

const apiUrl = 'api/jorders';

// Actions

export const getEntities: ICrudGetAllAction<IJorder> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_JORDER_LIST,
    payload: axios.get<IJorder>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IJorder> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_JORDER,
    payload: axios.get<IJorder>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IJorder> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_JORDER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IJorder> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_JORDER,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IJorder> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_JORDER,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IJorder> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_JORDER,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
