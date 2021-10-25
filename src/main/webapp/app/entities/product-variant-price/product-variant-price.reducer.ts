import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductVariantPrice, defaultValue } from 'app/shared/model/product-variant-price.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTVARIANTPRICE_LIST: 'productVariantPrice/FETCH_PRODUCTVARIANTPRICE_LIST',
  FETCH_PRODUCTVARIANTPRICE: 'productVariantPrice/FETCH_PRODUCTVARIANTPRICE',
  CREATE_PRODUCTVARIANTPRICE: 'productVariantPrice/CREATE_PRODUCTVARIANTPRICE',
  UPDATE_PRODUCTVARIANTPRICE: 'productVariantPrice/UPDATE_PRODUCTVARIANTPRICE',
  PARTIAL_UPDATE_PRODUCTVARIANTPRICE: 'productVariantPrice/PARTIAL_UPDATE_PRODUCTVARIANTPRICE',
  DELETE_PRODUCTVARIANTPRICE: 'productVariantPrice/DELETE_PRODUCTVARIANTPRICE',
  RESET: 'productVariantPrice/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductVariantPrice>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductVariantPriceState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductVariantPriceState = initialState, action): ProductVariantPriceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTVARIANTPRICE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTVARIANTPRICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTVARIANTPRICE):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTVARIANTPRICE):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTVARIANTPRICE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTPRICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTVARIANTPRICE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTVARIANTPRICE):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTVARIANTPRICE):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTVARIANTPRICE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTPRICE):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTVARIANTPRICE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTVARIANTPRICE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTVARIANTPRICE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTVARIANTPRICE):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTVARIANTPRICE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTPRICE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTVARIANTPRICE):
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

const apiUrl = 'api/product-variant-prices';

// Actions

export const getEntities: ICrudGetAllAction<IProductVariantPrice> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTVARIANTPRICE_LIST,
    payload: axios.get<IProductVariantPrice>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductVariantPrice> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTVARIANTPRICE,
    payload: axios.get<IProductVariantPrice>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductVariantPrice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTVARIANTPRICE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductVariantPrice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTVARIANTPRICE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProductVariantPrice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTPRICE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductVariantPrice> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTVARIANTPRICE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
