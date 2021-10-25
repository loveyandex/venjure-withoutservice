import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPogt, defaultValue } from 'app/shared/model/pogt.model';

export const ACTION_TYPES = {
  FETCH_POGT_LIST: 'pogt/FETCH_POGT_LIST',
  FETCH_POGT: 'pogt/FETCH_POGT',
  CREATE_POGT: 'pogt/CREATE_POGT',
  UPDATE_POGT: 'pogt/UPDATE_POGT',
  PARTIAL_UPDATE_POGT: 'pogt/PARTIAL_UPDATE_POGT',
  DELETE_POGT: 'pogt/DELETE_POGT',
  RESET: 'pogt/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPogt>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type PogtState = Readonly<typeof initialState>;

// Reducer

export default (state: PogtState = initialState, action): PogtState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_POGT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POGT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_POGT):
    case REQUEST(ACTION_TYPES.UPDATE_POGT):
    case REQUEST(ACTION_TYPES.DELETE_POGT):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_POGT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_POGT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POGT):
    case FAILURE(ACTION_TYPES.CREATE_POGT):
    case FAILURE(ACTION_TYPES.UPDATE_POGT):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_POGT):
    case FAILURE(ACTION_TYPES.DELETE_POGT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_POGT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_POGT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_POGT):
    case SUCCESS(ACTION_TYPES.UPDATE_POGT):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_POGT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_POGT):
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

const apiUrl = 'api/pogts';

// Actions

export const getEntities: ICrudGetAllAction<IPogt> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_POGT_LIST,
    payload: axios.get<IPogt>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IPogt> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POGT,
    payload: axios.get<IPogt>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPogt> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POGT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPogt> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POGT,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IPogt> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_POGT,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPogt> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POGT,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
