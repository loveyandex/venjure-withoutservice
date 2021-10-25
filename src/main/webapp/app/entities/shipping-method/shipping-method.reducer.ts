import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShippingMethod, defaultValue } from 'app/shared/model/shipping-method.model';

export const ACTION_TYPES = {
  FETCH_SHIPPINGMETHOD_LIST: 'shippingMethod/FETCH_SHIPPINGMETHOD_LIST',
  FETCH_SHIPPINGMETHOD: 'shippingMethod/FETCH_SHIPPINGMETHOD',
  CREATE_SHIPPINGMETHOD: 'shippingMethod/CREATE_SHIPPINGMETHOD',
  UPDATE_SHIPPINGMETHOD: 'shippingMethod/UPDATE_SHIPPINGMETHOD',
  PARTIAL_UPDATE_SHIPPINGMETHOD: 'shippingMethod/PARTIAL_UPDATE_SHIPPINGMETHOD',
  DELETE_SHIPPINGMETHOD: 'shippingMethod/DELETE_SHIPPINGMETHOD',
  RESET: 'shippingMethod/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShippingMethod>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ShippingMethodState = Readonly<typeof initialState>;

// Reducer

export default (state: ShippingMethodState = initialState, action): ShippingMethodState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHIPPINGMETHOD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHIPPINGMETHOD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SHIPPINGMETHOD):
    case REQUEST(ACTION_TYPES.UPDATE_SHIPPINGMETHOD):
    case REQUEST(ACTION_TYPES.DELETE_SHIPPINGMETHOD):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGMETHOD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SHIPPINGMETHOD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHIPPINGMETHOD):
    case FAILURE(ACTION_TYPES.CREATE_SHIPPINGMETHOD):
    case FAILURE(ACTION_TYPES.UPDATE_SHIPPINGMETHOD):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGMETHOD):
    case FAILURE(ACTION_TYPES.DELETE_SHIPPINGMETHOD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHIPPINGMETHOD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHIPPINGMETHOD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHIPPINGMETHOD):
    case SUCCESS(ACTION_TYPES.UPDATE_SHIPPINGMETHOD):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGMETHOD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHIPPINGMETHOD):
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

const apiUrl = 'api/shipping-methods';

// Actions

export const getEntities: ICrudGetAllAction<IShippingMethod> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SHIPPINGMETHOD_LIST,
    payload: axios.get<IShippingMethod>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IShippingMethod> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHIPPINGMETHOD,
    payload: axios.get<IShippingMethod>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IShippingMethod> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHIPPINGMETHOD,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShippingMethod> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHIPPINGMETHOD,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IShippingMethod> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGMETHOD,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShippingMethod> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHIPPINGMETHOD,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
