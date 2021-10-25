import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductAsset, defaultValue } from 'app/shared/model/product-asset.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTASSET_LIST: 'productAsset/FETCH_PRODUCTASSET_LIST',
  FETCH_PRODUCTASSET: 'productAsset/FETCH_PRODUCTASSET',
  CREATE_PRODUCTASSET: 'productAsset/CREATE_PRODUCTASSET',
  UPDATE_PRODUCTASSET: 'productAsset/UPDATE_PRODUCTASSET',
  PARTIAL_UPDATE_PRODUCTASSET: 'productAsset/PARTIAL_UPDATE_PRODUCTASSET',
  DELETE_PRODUCTASSET: 'productAsset/DELETE_PRODUCTASSET',
  RESET: 'productAsset/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductAsset>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductAssetState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductAssetState = initialState, action): ProductAssetState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTASSET_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTASSET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTASSET):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTASSET):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTASSET):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTASSET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTASSET_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTASSET):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTASSET):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTASSET):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTASSET):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTASSET):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTASSET_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTASSET):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTASSET):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTASSET):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTASSET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTASSET):
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

const apiUrl = 'api/product-assets';

// Actions

export const getEntities: ICrudGetAllAction<IProductAsset> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTASSET_LIST,
    payload: axios.get<IProductAsset>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductAsset> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTASSET,
    payload: axios.get<IProductAsset>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTASSET,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTASSET,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProductAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTASSET,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductAsset> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTASSET,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
