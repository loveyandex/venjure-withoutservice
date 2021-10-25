import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICollection, defaultValue } from 'app/shared/model/collection.model';

export const ACTION_TYPES = {
  FETCH_COLLECTION_LIST: 'collection/FETCH_COLLECTION_LIST',
  FETCH_COLLECTION: 'collection/FETCH_COLLECTION',
  CREATE_COLLECTION: 'collection/CREATE_COLLECTION',
  UPDATE_COLLECTION: 'collection/UPDATE_COLLECTION',
  PARTIAL_UPDATE_COLLECTION: 'collection/PARTIAL_UPDATE_COLLECTION',
  DELETE_COLLECTION: 'collection/DELETE_COLLECTION',
  RESET: 'collection/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICollection>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CollectionState = Readonly<typeof initialState>;

// Reducer

export default (state: CollectionState = initialState, action): CollectionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_COLLECTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COLLECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_COLLECTION):
    case REQUEST(ACTION_TYPES.UPDATE_COLLECTION):
    case REQUEST(ACTION_TYPES.DELETE_COLLECTION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_COLLECTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_COLLECTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COLLECTION):
    case FAILURE(ACTION_TYPES.CREATE_COLLECTION):
    case FAILURE(ACTION_TYPES.UPDATE_COLLECTION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_COLLECTION):
    case FAILURE(ACTION_TYPES.DELETE_COLLECTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_COLLECTION):
    case SUCCESS(ACTION_TYPES.UPDATE_COLLECTION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_COLLECTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_COLLECTION):
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

const apiUrl = 'api/collections';

// Actions

export const getEntities: ICrudGetAllAction<ICollection> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_COLLECTION_LIST,
    payload: axios.get<ICollection>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICollection> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COLLECTION,
    payload: axios.get<ICollection>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICollection> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COLLECTION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICollection> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COLLECTION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICollection> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_COLLECTION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICollection> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COLLECTION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
