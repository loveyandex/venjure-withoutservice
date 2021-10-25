import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFacetValueTranslation, defaultValue } from 'app/shared/model/facet-value-translation.model';

export const ACTION_TYPES = {
  FETCH_FACETVALUETRANSLATION_LIST: 'facetValueTranslation/FETCH_FACETVALUETRANSLATION_LIST',
  FETCH_FACETVALUETRANSLATION: 'facetValueTranslation/FETCH_FACETVALUETRANSLATION',
  CREATE_FACETVALUETRANSLATION: 'facetValueTranslation/CREATE_FACETVALUETRANSLATION',
  UPDATE_FACETVALUETRANSLATION: 'facetValueTranslation/UPDATE_FACETVALUETRANSLATION',
  PARTIAL_UPDATE_FACETVALUETRANSLATION: 'facetValueTranslation/PARTIAL_UPDATE_FACETVALUETRANSLATION',
  DELETE_FACETVALUETRANSLATION: 'facetValueTranslation/DELETE_FACETVALUETRANSLATION',
  RESET: 'facetValueTranslation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFacetValueTranslation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FacetValueTranslationState = Readonly<typeof initialState>;

// Reducer

export default (state: FacetValueTranslationState = initialState, action): FacetValueTranslationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FACETVALUETRANSLATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FACETVALUETRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FACETVALUETRANSLATION):
    case REQUEST(ACTION_TYPES.UPDATE_FACETVALUETRANSLATION):
    case REQUEST(ACTION_TYPES.DELETE_FACETVALUETRANSLATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_FACETVALUETRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FACETVALUETRANSLATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FACETVALUETRANSLATION):
    case FAILURE(ACTION_TYPES.CREATE_FACETVALUETRANSLATION):
    case FAILURE(ACTION_TYPES.UPDATE_FACETVALUETRANSLATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_FACETVALUETRANSLATION):
    case FAILURE(ACTION_TYPES.DELETE_FACETVALUETRANSLATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACETVALUETRANSLATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACETVALUETRANSLATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FACETVALUETRANSLATION):
    case SUCCESS(ACTION_TYPES.UPDATE_FACETVALUETRANSLATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_FACETVALUETRANSLATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FACETVALUETRANSLATION):
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

const apiUrl = 'api/facet-value-translations';

// Actions

export const getEntities: ICrudGetAllAction<IFacetValueTranslation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FACETVALUETRANSLATION_LIST,
    payload: axios.get<IFacetValueTranslation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFacetValueTranslation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FACETVALUETRANSLATION,
    payload: axios.get<IFacetValueTranslation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFacetValueTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FACETVALUETRANSLATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFacetValueTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACETVALUETRANSLATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IFacetValueTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_FACETVALUETRANSLATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFacetValueTranslation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FACETVALUETRANSLATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
