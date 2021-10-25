import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAsset, defaultValue } from 'app/shared/model/asset.model';

export const ACTION_TYPES = {
  FETCH_ASSET_LIST: 'asset/FETCH_ASSET_LIST',
  FETCH_ASSET: 'asset/FETCH_ASSET',
  CREATE_ASSET: 'asset/CREATE_ASSET',
  UPDATE_ASSET: 'asset/UPDATE_ASSET',
  PARTIAL_UPDATE_ASSET: 'asset/PARTIAL_UPDATE_ASSET',
  DELETE_ASSET: 'asset/DELETE_ASSET',
  RESET: 'asset/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAsset>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AssetState = Readonly<typeof initialState>;

// Reducer

export default (state: AssetState = initialState, action): AssetState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ASSET_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ASSET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ASSET):
    case REQUEST(ACTION_TYPES.UPDATE_ASSET):
    case REQUEST(ACTION_TYPES.DELETE_ASSET):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ASSET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ASSET_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ASSET):
    case FAILURE(ACTION_TYPES.CREATE_ASSET):
    case FAILURE(ACTION_TYPES.UPDATE_ASSET):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ASSET):
    case FAILURE(ACTION_TYPES.DELETE_ASSET):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ASSET_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ASSET):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ASSET):
    case SUCCESS(ACTION_TYPES.UPDATE_ASSET):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ASSET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ASSET):
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

const apiUrl = 'api/assets';

// Actions

export const getEntities: ICrudGetAllAction<IAsset> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ASSET_LIST,
    payload: axios.get<IAsset>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAsset> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ASSET,
    payload: axios.get<IAsset>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ASSET,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ASSET,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ASSET,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAsset> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ASSET,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
