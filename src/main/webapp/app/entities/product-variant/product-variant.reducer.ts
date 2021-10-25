import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductVariant, defaultValue } from 'app/shared/model/product-variant.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTVARIANT_LIST: 'productVariant/FETCH_PRODUCTVARIANT_LIST',
  FETCH_PRODUCTVARIANT: 'productVariant/FETCH_PRODUCTVARIANT',
  CREATE_PRODUCTVARIANT: 'productVariant/CREATE_PRODUCTVARIANT',
  UPDATE_PRODUCTVARIANT: 'productVariant/UPDATE_PRODUCTVARIANT',
  PARTIAL_UPDATE_PRODUCTVARIANT: 'productVariant/PARTIAL_UPDATE_PRODUCTVARIANT',
  DELETE_PRODUCTVARIANT: 'productVariant/DELETE_PRODUCTVARIANT',
  RESET: 'productVariant/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductVariant>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductVariantState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductVariantState = initialState, action): ProductVariantState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTVARIANT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTVARIANT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTVARIANT):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTVARIANT):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTVARIANT):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTVARIANT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTVARIANT):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTVARIANT):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTVARIANT):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANT):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTVARIANT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTVARIANT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTVARIANT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTVARIANT):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTVARIANT):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTVARIANT):
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

const apiUrl = 'api/product-variants';

// Actions

export const getEntities: ICrudGetAllAction<IProductVariant> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTVARIANT_LIST,
    payload: axios.get<IProductVariant>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductVariant> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTVARIANT,
    payload: axios.get<IProductVariant>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductVariant> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTVARIANT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductVariant> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTVARIANT,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProductVariant> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANT,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductVariant> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTVARIANT,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
