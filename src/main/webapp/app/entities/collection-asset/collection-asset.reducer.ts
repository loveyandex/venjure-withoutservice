import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICollectionAsset, defaultValue } from 'app/shared/model/collection-asset.model';

export const ACTION_TYPES = {
  FETCH_COLLECTIONASSET_LIST: 'collectionAsset/FETCH_COLLECTIONASSET_LIST',
  FETCH_COLLECTIONASSET: 'collectionAsset/FETCH_COLLECTIONASSET',
  CREATE_COLLECTIONASSET: 'collectionAsset/CREATE_COLLECTIONASSET',
  UPDATE_COLLECTIONASSET: 'collectionAsset/UPDATE_COLLECTIONASSET',
  PARTIAL_UPDATE_COLLECTIONASSET: 'collectionAsset/PARTIAL_UPDATE_COLLECTIONASSET',
  DELETE_COLLECTIONASSET: 'collectionAsset/DELETE_COLLECTIONASSET',
  RESET: 'collectionAsset/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICollectionAsset>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CollectionAssetState = Readonly<typeof initialState>;

// Reducer

export default (state: CollectionAssetState = initialState, action): CollectionAssetState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_COLLECTIONASSET_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COLLECTIONASSET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_COLLECTIONASSET):
    case REQUEST(ACTION_TYPES.UPDATE_COLLECTIONASSET):
    case REQUEST(ACTION_TYPES.DELETE_COLLECTIONASSET):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_COLLECTIONASSET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_COLLECTIONASSET_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COLLECTIONASSET):
    case FAILURE(ACTION_TYPES.CREATE_COLLECTIONASSET):
    case FAILURE(ACTION_TYPES.UPDATE_COLLECTIONASSET):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_COLLECTIONASSET):
    case FAILURE(ACTION_TYPES.DELETE_COLLECTIONASSET):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTIONASSET_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTIONASSET):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_COLLECTIONASSET):
    case SUCCESS(ACTION_TYPES.UPDATE_COLLECTIONASSET):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_COLLECTIONASSET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_COLLECTIONASSET):
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

const apiUrl = 'api/collection-assets';

// Actions

export const getEntities: ICrudGetAllAction<ICollectionAsset> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_COLLECTIONASSET_LIST,
    payload: axios.get<ICollectionAsset>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICollectionAsset> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COLLECTIONASSET,
    payload: axios.get<ICollectionAsset>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICollectionAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COLLECTIONASSET,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICollectionAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COLLECTIONASSET,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICollectionAsset> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_COLLECTIONASSET,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICollectionAsset> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COLLECTIONASSET,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
