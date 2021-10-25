import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductOption, defaultValue } from 'app/shared/model/product-option.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTOPTION_LIST: 'productOption/FETCH_PRODUCTOPTION_LIST',
  FETCH_PRODUCTOPTION: 'productOption/FETCH_PRODUCTOPTION',
  CREATE_PRODUCTOPTION: 'productOption/CREATE_PRODUCTOPTION',
  UPDATE_PRODUCTOPTION: 'productOption/UPDATE_PRODUCTOPTION',
  PARTIAL_UPDATE_PRODUCTOPTION: 'productOption/PARTIAL_UPDATE_PRODUCTOPTION',
  DELETE_PRODUCTOPTION: 'productOption/DELETE_PRODUCTOPTION',
  RESET: 'productOption/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductOption>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductOptionState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductOptionState = initialState, action): ProductOptionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTOPTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTOPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTOPTION):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTOPTION):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTOPTION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTOPTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTOPTION):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTOPTION):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTOPTION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTION):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTOPTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTOPTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTOPTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTOPTION):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTOPTION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTOPTION):
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

const apiUrl = 'api/product-options';

// Actions

export const getEntities: ICrudGetAllAction<IProductOption> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTOPTION_LIST,
    payload: axios.get<IProductOption>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductOption> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTOPTION,
    payload: axios.get<IProductOption>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductOption> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTOPTION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductOption> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTOPTION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProductOption> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductOption> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTOPTION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
