import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrderLine, defaultValue } from 'app/shared/model/order-line.model';

export const ACTION_TYPES = {
  FETCH_ORDERLINE_LIST: 'orderLine/FETCH_ORDERLINE_LIST',
  FETCH_ORDERLINE: 'orderLine/FETCH_ORDERLINE',
  CREATE_ORDERLINE: 'orderLine/CREATE_ORDERLINE',
  UPDATE_ORDERLINE: 'orderLine/UPDATE_ORDERLINE',
  PARTIAL_UPDATE_ORDERLINE: 'orderLine/PARTIAL_UPDATE_ORDERLINE',
  DELETE_ORDERLINE: 'orderLine/DELETE_ORDERLINE',
  RESET: 'orderLine/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrderLine>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type OrderLineState = Readonly<typeof initialState>;

// Reducer

export default (state: OrderLineState = initialState, action): OrderLineState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ORDERLINE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORDERLINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ORDERLINE):
    case REQUEST(ACTION_TYPES.UPDATE_ORDERLINE):
    case REQUEST(ACTION_TYPES.DELETE_ORDERLINE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ORDERLINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ORDERLINE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORDERLINE):
    case FAILURE(ACTION_TYPES.CREATE_ORDERLINE):
    case FAILURE(ACTION_TYPES.UPDATE_ORDERLINE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ORDERLINE):
    case FAILURE(ACTION_TYPES.DELETE_ORDERLINE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERLINE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERLINE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORDERLINE):
    case SUCCESS(ACTION_TYPES.UPDATE_ORDERLINE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ORDERLINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORDERLINE):
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

const apiUrl = 'api/order-lines';

// Actions

export const getEntities: ICrudGetAllAction<IOrderLine> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERLINE_LIST,
    payload: axios.get<IOrderLine>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IOrderLine> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERLINE,
    payload: axios.get<IOrderLine>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IOrderLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORDERLINE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrderLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORDERLINE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IOrderLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ORDERLINE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrderLine> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORDERLINE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
