import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFacetTranslation, defaultValue } from 'app/shared/model/facet-translation.model';

export const ACTION_TYPES = {
  FETCH_FACETTRANSLATION_LIST: 'facetTranslation/FETCH_FACETTRANSLATION_LIST',
  FETCH_FACETTRANSLATION: 'facetTranslation/FETCH_FACETTRANSLATION',
  CREATE_FACETTRANSLATION: 'facetTranslation/CREATE_FACETTRANSLATION',
  UPDATE_FACETTRANSLATION: 'facetTranslation/UPDATE_FACETTRANSLATION',
  PARTIAL_UPDATE_FACETTRANSLATION: 'facetTranslation/PARTIAL_UPDATE_FACETTRANSLATION',
  DELETE_FACETTRANSLATION: 'facetTranslation/DELETE_FACETTRANSLATION',
  RESET: 'facetTranslation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFacetTranslation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FacetTranslationState = Readonly<typeof initialState>;

// Reducer

export default (state: FacetTranslationState = initialState, action): FacetTranslationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FACETTRANSLATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FACETTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FACETTRANSLATION):
    case REQUEST(ACTION_TYPES.UPDATE_FACETTRANSLATION):
    case REQUEST(ACTION_TYPES.DELETE_FACETTRANSLATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_FACETTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FACETTRANSLATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FACETTRANSLATION):
    case FAILURE(ACTION_TYPES.CREATE_FACETTRANSLATION):
    case FAILURE(ACTION_TYPES.UPDATE_FACETTRANSLATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_FACETTRANSLATION):
    case FAILURE(ACTION_TYPES.DELETE_FACETTRANSLATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACETTRANSLATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACETTRANSLATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FACETTRANSLATION):
    case SUCCESS(ACTION_TYPES.UPDATE_FACETTRANSLATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_FACETTRANSLATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FACETTRANSLATION):
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

const apiUrl = 'api/facet-translations';

// Actions

export const getEntities: ICrudGetAllAction<IFacetTranslation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FACETTRANSLATION_LIST,
    payload: axios.get<IFacetTranslation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFacetTranslation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FACETTRANSLATION,
    payload: axios.get<IFacetTranslation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFacetTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FACETTRANSLATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFacetTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACETTRANSLATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IFacetTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_FACETTRANSLATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFacetTranslation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FACETTRANSLATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
