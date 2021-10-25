import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFulfillment, defaultValue } from 'app/shared/model/fulfillment.model';

export const ACTION_TYPES = {
  FETCH_FULFILLMENT_LIST: 'fulfillment/FETCH_FULFILLMENT_LIST',
  FETCH_FULFILLMENT: 'fulfillment/FETCH_FULFILLMENT',
  CREATE_FULFILLMENT: 'fulfillment/CREATE_FULFILLMENT',
  UPDATE_FULFILLMENT: 'fulfillment/UPDATE_FULFILLMENT',
  PARTIAL_UPDATE_FULFILLMENT: 'fulfillment/PARTIAL_UPDATE_FULFILLMENT',
  DELETE_FULFILLMENT: 'fulfillment/DELETE_FULFILLMENT',
  RESET: 'fulfillment/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFulfillment>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FulfillmentState = Readonly<typeof initialState>;

// Reducer

export default (state: FulfillmentState = initialState, action): FulfillmentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FULFILLMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FULFILLMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FULFILLMENT):
    case REQUEST(ACTION_TYPES.UPDATE_FULFILLMENT):
    case REQUEST(ACTION_TYPES.DELETE_FULFILLMENT):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_FULFILLMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FULFILLMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FULFILLMENT):
    case FAILURE(ACTION_TYPES.CREATE_FULFILLMENT):
    case FAILURE(ACTION_TYPES.UPDATE_FULFILLMENT):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_FULFILLMENT):
    case FAILURE(ACTION_TYPES.DELETE_FULFILLMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FULFILLMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FULFILLMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FULFILLMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_FULFILLMENT):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_FULFILLMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FULFILLMENT):
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

const apiUrl = 'api/fulfillments';

// Actions

export const getEntities: ICrudGetAllAction<IFulfillment> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FULFILLMENT_LIST,
    payload: axios.get<IFulfillment>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFulfillment> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FULFILLMENT,
    payload: axios.get<IFulfillment>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFulfillment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FULFILLMENT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFulfillment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FULFILLMENT,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IFulfillment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_FULFILLMENT,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFulfillment> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FULFILLMENT,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
