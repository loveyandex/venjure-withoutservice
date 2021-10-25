import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShippingLine, defaultValue } from 'app/shared/model/shipping-line.model';

export const ACTION_TYPES = {
  FETCH_SHIPPINGLINE_LIST: 'shippingLine/FETCH_SHIPPINGLINE_LIST',
  FETCH_SHIPPINGLINE: 'shippingLine/FETCH_SHIPPINGLINE',
  CREATE_SHIPPINGLINE: 'shippingLine/CREATE_SHIPPINGLINE',
  UPDATE_SHIPPINGLINE: 'shippingLine/UPDATE_SHIPPINGLINE',
  PARTIAL_UPDATE_SHIPPINGLINE: 'shippingLine/PARTIAL_UPDATE_SHIPPINGLINE',
  DELETE_SHIPPINGLINE: 'shippingLine/DELETE_SHIPPINGLINE',
  RESET: 'shippingLine/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShippingLine>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ShippingLineState = Readonly<typeof initialState>;

// Reducer

export default (state: ShippingLineState = initialState, action): ShippingLineState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHIPPINGLINE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHIPPINGLINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SHIPPINGLINE):
    case REQUEST(ACTION_TYPES.UPDATE_SHIPPINGLINE):
    case REQUEST(ACTION_TYPES.DELETE_SHIPPINGLINE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGLINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SHIPPINGLINE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHIPPINGLINE):
    case FAILURE(ACTION_TYPES.CREATE_SHIPPINGLINE):
    case FAILURE(ACTION_TYPES.UPDATE_SHIPPINGLINE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGLINE):
    case FAILURE(ACTION_TYPES.DELETE_SHIPPINGLINE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHIPPINGLINE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHIPPINGLINE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHIPPINGLINE):
    case SUCCESS(ACTION_TYPES.UPDATE_SHIPPINGLINE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGLINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHIPPINGLINE):
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

const apiUrl = 'api/shipping-lines';

// Actions

export const getEntities: ICrudGetAllAction<IShippingLine> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SHIPPINGLINE_LIST,
    payload: axios.get<IShippingLine>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IShippingLine> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHIPPINGLINE,
    payload: axios.get<IShippingLine>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IShippingLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHIPPINGLINE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShippingLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHIPPINGLINE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IShippingLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGLINE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShippingLine> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHIPPINGLINE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
