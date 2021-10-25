import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductVariantAsset, defaultValue } from 'app/shared/model/product-variant-asset.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTVARIANTASSET_LIST: 'productVariantAsset/FETCH_PRODUCTVARIANTASSET_LIST',
  FETCH_PRODUCTVARIANTASSET: 'productVariantAsset/FETCH_PRODUCTVARIANTASSET',
  CREATE_PRODUCTVARIANTASSET: 'productVariantAsset/CREATE_PRODUCTVARIANTASSET',
  UPDATE_PRODUCTVARIANTASSET: 'productVariantAsset/UPDATE_PRODUCTVARIANTASSET',
  PARTIAL_UPDATE_PRODUCTVARIANTASSET: 'productVariantAsset/PARTIAL_UPDATE_PRODUCTVARIANTASSET',
  DELETE_PRODUCTVARIANTASSET: 'productVariantAsset/DELETE_PRODUCTVARIANTASSET',
  RESET: 'productVariantAsset/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductVariantAsset>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductVariantAssetState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductVariantAssetState = initialState, action): ProductVariantAssetState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTVARIANTASSET_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTVARIANTASSET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTVARIANTASSET):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTVARIANTASSET):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTVARIANTASSET):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTASSET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTVARIANTASSET_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTVARIANTASSET):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTVARIANTASSET):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTVARIANTASSET):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTASSET):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTVARIANTASSET):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTVARIANTASSET_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTVARIANTASSET):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTVARIANTASSET):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTVARIANTASSET):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTASSET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTVARIANTASSET):
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

const apiUrl = 'api/product-variant-assets';

// Actions

export const getEntities: ICrudGetAllAction<IProductVariantAsset> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTVARIANTASSET_LIST,
    payload: axios.get<IProductVariantAsset>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductVariantAsset> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTVARIANTASSET,
    payload: axios.get<IProductVariantAsset>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductVariantAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTVARIANTASSET,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductVariantAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTVARIANTASSET,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProductVariantAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTASSET,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductVariantAsset> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTVARIANTASSET,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
