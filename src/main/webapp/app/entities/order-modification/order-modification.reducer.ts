import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrderModification, defaultValue } from 'app/shared/model/order-modification.model';

export const ACTION_TYPES = {
  FETCH_ORDERMODIFICATION_LIST: 'orderModification/FETCH_ORDERMODIFICATION_LIST',
  FETCH_ORDERMODIFICATION: 'orderModification/FETCH_ORDERMODIFICATION',
  CREATE_ORDERMODIFICATION: 'orderModification/CREATE_ORDERMODIFICATION',
  UPDATE_ORDERMODIFICATION: 'orderModification/UPDATE_ORDERMODIFICATION',
  PARTIAL_UPDATE_ORDERMODIFICATION: 'orderModification/PARTIAL_UPDATE_ORDERMODIFICATION',
  DELETE_ORDERMODIFICATION: 'orderModification/DELETE_ORDERMODIFICATION',
  RESET: 'orderModification/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrderModification>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type OrderModificationState = Readonly<typeof initialState>;

// Reducer

export default (state: OrderModificationState = initialState, action): OrderModificationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ORDERMODIFICATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORDERMODIFICATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ORDERMODIFICATION):
    case REQUEST(ACTION_TYPES.UPDATE_ORDERMODIFICATION):
    case REQUEST(ACTION_TYPES.DELETE_ORDERMODIFICATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ORDERMODIFICATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ORDERMODIFICATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORDERMODIFICATION):
    case FAILURE(ACTION_TYPES.CREATE_ORDERMODIFICATION):
    case FAILURE(ACTION_TYPES.UPDATE_ORDERMODIFICATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ORDERMODIFICATION):
    case FAILURE(ACTION_TYPES.DELETE_ORDERMODIFICATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERMODIFICATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERMODIFICATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORDERMODIFICATION):
    case SUCCESS(ACTION_TYPES.UPDATE_ORDERMODIFICATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ORDERMODIFICATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORDERMODIFICATION):
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

const apiUrl = 'api/order-modifications';

// Actions

export const getEntities: ICrudGetAllAction<IOrderModification> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERMODIFICATION_LIST,
    payload: axios.get<IOrderModification>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IOrderModification> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERMODIFICATION,
    payload: axios.get<IOrderModification>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IOrderModification> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORDERMODIFICATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrderModification> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORDERMODIFICATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IOrderModification> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ORDERMODIFICATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrderModification> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORDERMODIFICATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
