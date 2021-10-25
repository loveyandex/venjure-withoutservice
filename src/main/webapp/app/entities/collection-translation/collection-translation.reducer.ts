import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICollectionTranslation, defaultValue } from 'app/shared/model/collection-translation.model';

export const ACTION_TYPES = {
  FETCH_COLLECTIONTRANSLATION_LIST: 'collectionTranslation/FETCH_COLLECTIONTRANSLATION_LIST',
  FETCH_COLLECTIONTRANSLATION: 'collectionTranslation/FETCH_COLLECTIONTRANSLATION',
  CREATE_COLLECTIONTRANSLATION: 'collectionTranslation/CREATE_COLLECTIONTRANSLATION',
  UPDATE_COLLECTIONTRANSLATION: 'collectionTranslation/UPDATE_COLLECTIONTRANSLATION',
  PARTIAL_UPDATE_COLLECTIONTRANSLATION: 'collectionTranslation/PARTIAL_UPDATE_COLLECTIONTRANSLATION',
  DELETE_COLLECTIONTRANSLATION: 'collectionTranslation/DELETE_COLLECTIONTRANSLATION',
  RESET: 'collectionTranslation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICollectionTranslation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CollectionTranslationState = Readonly<typeof initialState>;

// Reducer

export default (state: CollectionTranslationState = initialState, action): CollectionTranslationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_COLLECTIONTRANSLATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COLLECTIONTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_COLLECTIONTRANSLATION):
    case REQUEST(ACTION_TYPES.UPDATE_COLLECTIONTRANSLATION):
    case REQUEST(ACTION_TYPES.DELETE_COLLECTIONTRANSLATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_COLLECTIONTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_COLLECTIONTRANSLATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COLLECTIONTRANSLATION):
    case FAILURE(ACTION_TYPES.CREATE_COLLECTIONTRANSLATION):
    case FAILURE(ACTION_TYPES.UPDATE_COLLECTIONTRANSLATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_COLLECTIONTRANSLATION):
    case FAILURE(ACTION_TYPES.DELETE_COLLECTIONTRANSLATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTIONTRANSLATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_COLLECTIONTRANSLATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_COLLECTIONTRANSLATION):
    case SUCCESS(ACTION_TYPES.UPDATE_COLLECTIONTRANSLATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_COLLECTIONTRANSLATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_COLLECTIONTRANSLATION):
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

const apiUrl = 'api/collection-translations';

// Actions

export const getEntities: ICrudGetAllAction<ICollectionTranslation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_COLLECTIONTRANSLATION_LIST,
    payload: axios.get<ICollectionTranslation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICollectionTranslation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COLLECTIONTRANSLATION,
    payload: axios.get<ICollectionTranslation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICollectionTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COLLECTIONTRANSLATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICollectionTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COLLECTIONTRANSLATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICollectionTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_COLLECTIONTRANSLATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICollectionTranslation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COLLECTIONTRANSLATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
