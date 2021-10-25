import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductVariantTranslation, defaultValue } from 'app/shared/model/product-variant-translation.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTVARIANTTRANSLATION_LIST: 'productVariantTranslation/FETCH_PRODUCTVARIANTTRANSLATION_LIST',
  FETCH_PRODUCTVARIANTTRANSLATION: 'productVariantTranslation/FETCH_PRODUCTVARIANTTRANSLATION',
  CREATE_PRODUCTVARIANTTRANSLATION: 'productVariantTranslation/CREATE_PRODUCTVARIANTTRANSLATION',
  UPDATE_PRODUCTVARIANTTRANSLATION: 'productVariantTranslation/UPDATE_PRODUCTVARIANTTRANSLATION',
  PARTIAL_UPDATE_PRODUCTVARIANTTRANSLATION: 'productVariantTranslation/PARTIAL_UPDATE_PRODUCTVARIANTTRANSLATION',
  DELETE_PRODUCTVARIANTTRANSLATION: 'productVariantTranslation/DELETE_PRODUCTVARIANTTRANSLATION',
  RESET: 'productVariantTranslation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductVariantTranslation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductVariantTranslationState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductVariantTranslationState = initialState, action): ProductVariantTranslationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTVARIANTTRANSLATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTVARIANTTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTVARIANTTRANSLATION):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTVARIANTTRANSLATION):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTVARIANTTRANSLATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTVARIANTTRANSLATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTVARIANTTRANSLATION):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTVARIANTTRANSLATION):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTVARIANTTRANSLATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTTRANSLATION):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTVARIANTTRANSLATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTVARIANTTRANSLATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTVARIANTTRANSLATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTVARIANTTRANSLATION):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTVARIANTTRANSLATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTTRANSLATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTVARIANTTRANSLATION):
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

const apiUrl = 'api/product-variant-translations';

// Actions

export const getEntities: ICrudGetAllAction<IProductVariantTranslation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTVARIANTTRANSLATION_LIST,
    payload: axios.get<IProductVariantTranslation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductVariantTranslation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTVARIANTTRANSLATION,
    payload: axios.get<IProductVariantTranslation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductVariantTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTVARIANTTRANSLATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductVariantTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTVARIANTTRANSLATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProductVariantTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTVARIANTTRANSLATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductVariantTranslation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTVARIANTTRANSLATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
