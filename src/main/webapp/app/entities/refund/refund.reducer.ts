import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRefund, defaultValue } from 'app/shared/model/refund.model';

export const ACTION_TYPES = {
  FETCH_REFUND_LIST: 'refund/FETCH_REFUND_LIST',
  FETCH_REFUND: 'refund/FETCH_REFUND',
  CREATE_REFUND: 'refund/CREATE_REFUND',
  UPDATE_REFUND: 'refund/UPDATE_REFUND',
  PARTIAL_UPDATE_REFUND: 'refund/PARTIAL_UPDATE_REFUND',
  DELETE_REFUND: 'refund/DELETE_REFUND',
  RESET: 'refund/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRefund>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type RefundState = Readonly<typeof initialState>;

// Reducer

export default (state: RefundState = initialState, action): RefundState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_REFUND_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REFUND):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_REFUND):
    case REQUEST(ACTION_TYPES.UPDATE_REFUND):
    case REQUEST(ACTION_TYPES.DELETE_REFUND):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_REFUND):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_REFUND_LIST):
    case FAILURE(ACTION_TYPES.FETCH_REFUND):
    case FAILURE(ACTION_TYPES.CREATE_REFUND):
    case FAILURE(ACTION_TYPES.UPDATE_REFUND):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_REFUND):
    case FAILURE(ACTION_TYPES.DELETE_REFUND):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_REFUND_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_REFUND):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_REFUND):
    case SUCCESS(ACTION_TYPES.UPDATE_REFUND):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_REFUND):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_REFUND):
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

const apiUrl = 'api/refunds';

// Actions

export const getEntities: ICrudGetAllAction<IRefund> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_REFUND_LIST,
    payload: axios.get<IRefund>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IRefund> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_REFUND,
    payload: axios.get<IRefund>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IRefund> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_REFUND,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRefund> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_REFUND,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IRefund> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_REFUND,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRefund> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_REFUND,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
