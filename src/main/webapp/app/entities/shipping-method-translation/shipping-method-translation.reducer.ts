import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShippingMethodTranslation, defaultValue } from 'app/shared/model/shipping-method-translation.model';

export const ACTION_TYPES = {
  FETCH_SHIPPINGMETHODTRANSLATION_LIST: 'shippingMethodTranslation/FETCH_SHIPPINGMETHODTRANSLATION_LIST',
  FETCH_SHIPPINGMETHODTRANSLATION: 'shippingMethodTranslation/FETCH_SHIPPINGMETHODTRANSLATION',
  CREATE_SHIPPINGMETHODTRANSLATION: 'shippingMethodTranslation/CREATE_SHIPPINGMETHODTRANSLATION',
  UPDATE_SHIPPINGMETHODTRANSLATION: 'shippingMethodTranslation/UPDATE_SHIPPINGMETHODTRANSLATION',
  PARTIAL_UPDATE_SHIPPINGMETHODTRANSLATION: 'shippingMethodTranslation/PARTIAL_UPDATE_SHIPPINGMETHODTRANSLATION',
  DELETE_SHIPPINGMETHODTRANSLATION: 'shippingMethodTranslation/DELETE_SHIPPINGMETHODTRANSLATION',
  RESET: 'shippingMethodTranslation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShippingMethodTranslation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ShippingMethodTranslationState = Readonly<typeof initialState>;

// Reducer

export default (state: ShippingMethodTranslationState = initialState, action): ShippingMethodTranslationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHIPPINGMETHODTRANSLATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHIPPINGMETHODTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SHIPPINGMETHODTRANSLATION):
    case REQUEST(ACTION_TYPES.UPDATE_SHIPPINGMETHODTRANSLATION):
    case REQUEST(ACTION_TYPES.DELETE_SHIPPINGMETHODTRANSLATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGMETHODTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SHIPPINGMETHODTRANSLATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHIPPINGMETHODTRANSLATION):
    case FAILURE(ACTION_TYPES.CREATE_SHIPPINGMETHODTRANSLATION):
    case FAILURE(ACTION_TYPES.UPDATE_SHIPPINGMETHODTRANSLATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGMETHODTRANSLATION):
    case FAILURE(ACTION_TYPES.DELETE_SHIPPINGMETHODTRANSLATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHIPPINGMETHODTRANSLATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHIPPINGMETHODTRANSLATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHIPPINGMETHODTRANSLATION):
    case SUCCESS(ACTION_TYPES.UPDATE_SHIPPINGMETHODTRANSLATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGMETHODTRANSLATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHIPPINGMETHODTRANSLATION):
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

const apiUrl = 'api/shipping-method-translations';

// Actions

export const getEntities: ICrudGetAllAction<IShippingMethodTranslation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SHIPPINGMETHODTRANSLATION_LIST,
    payload: axios.get<IShippingMethodTranslation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IShippingMethodTranslation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHIPPINGMETHODTRANSLATION,
    payload: axios.get<IShippingMethodTranslation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IShippingMethodTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHIPPINGMETHODTRANSLATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShippingMethodTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHIPPINGMETHODTRANSLATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IShippingMethodTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SHIPPINGMETHODTRANSLATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShippingMethodTranslation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHIPPINGMETHODTRANSLATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
