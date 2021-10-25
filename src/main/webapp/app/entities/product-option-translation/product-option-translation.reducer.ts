import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductOptionTranslation, defaultValue } from 'app/shared/model/product-option-translation.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTOPTIONTRANSLATION_LIST: 'productOptionTranslation/FETCH_PRODUCTOPTIONTRANSLATION_LIST',
  FETCH_PRODUCTOPTIONTRANSLATION: 'productOptionTranslation/FETCH_PRODUCTOPTIONTRANSLATION',
  CREATE_PRODUCTOPTIONTRANSLATION: 'productOptionTranslation/CREATE_PRODUCTOPTIONTRANSLATION',
  UPDATE_PRODUCTOPTIONTRANSLATION: 'productOptionTranslation/UPDATE_PRODUCTOPTIONTRANSLATION',
  PARTIAL_UPDATE_PRODUCTOPTIONTRANSLATION: 'productOptionTranslation/PARTIAL_UPDATE_PRODUCTOPTIONTRANSLATION',
  DELETE_PRODUCTOPTIONTRANSLATION: 'productOptionTranslation/DELETE_PRODUCTOPTIONTRANSLATION',
  RESET: 'productOptionTranslation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductOptionTranslation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductOptionTranslationState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductOptionTranslationState = initialState, action): ProductOptionTranslationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTOPTIONTRANSLATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTOPTIONTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTOPTIONTRANSLATION):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTOPTIONTRANSLATION):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTOPTIONTRANSLATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTIONTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTOPTIONTRANSLATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTOPTIONTRANSLATION):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTOPTIONTRANSLATION):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTOPTIONTRANSLATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTIONTRANSLATION):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTOPTIONTRANSLATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTOPTIONTRANSLATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTOPTIONTRANSLATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTOPTIONTRANSLATION):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTOPTIONTRANSLATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTIONTRANSLATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTOPTIONTRANSLATION):
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

const apiUrl = 'api/product-option-translations';

// Actions

export const getEntities: ICrudGetAllAction<IProductOptionTranslation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTOPTIONTRANSLATION_LIST,
    payload: axios.get<IProductOptionTranslation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductOptionTranslation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTOPTIONTRANSLATION,
    payload: axios.get<IProductOptionTranslation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductOptionTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTOPTIONTRANSLATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductOptionTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTOPTIONTRANSLATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProductOptionTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTIONTRANSLATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductOptionTranslation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTOPTIONTRANSLATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
