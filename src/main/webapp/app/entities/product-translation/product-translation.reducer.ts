import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductTranslation, defaultValue } from 'app/shared/model/product-translation.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTTRANSLATION_LIST: 'productTranslation/FETCH_PRODUCTTRANSLATION_LIST',
  FETCH_PRODUCTTRANSLATION: 'productTranslation/FETCH_PRODUCTTRANSLATION',
  CREATE_PRODUCTTRANSLATION: 'productTranslation/CREATE_PRODUCTTRANSLATION',
  UPDATE_PRODUCTTRANSLATION: 'productTranslation/UPDATE_PRODUCTTRANSLATION',
  PARTIAL_UPDATE_PRODUCTTRANSLATION: 'productTranslation/PARTIAL_UPDATE_PRODUCTTRANSLATION',
  DELETE_PRODUCTTRANSLATION: 'productTranslation/DELETE_PRODUCTTRANSLATION',
  RESET: 'productTranslation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductTranslation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductTranslationState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductTranslationState = initialState, action): ProductTranslationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTTRANSLATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTTRANSLATION):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTTRANSLATION):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTTRANSLATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTTRANSLATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTTRANSLATION):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTTRANSLATION):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTTRANSLATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTTRANSLATION):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTTRANSLATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTTRANSLATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTTRANSLATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTTRANSLATION):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTTRANSLATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTTRANSLATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTTRANSLATION):
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

const apiUrl = 'api/product-translations';

// Actions

export const getEntities: ICrudGetAllAction<IProductTranslation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTTRANSLATION_LIST,
    payload: axios.get<IProductTranslation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductTranslation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTTRANSLATION,
    payload: axios.get<IProductTranslation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTTRANSLATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTTRANSLATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProductTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTTRANSLATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductTranslation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTTRANSLATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
