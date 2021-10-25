import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICountryTranslation, defaultValue } from 'app/shared/model/country-translation.model';

export const ACTION_TYPES = {
  FETCH_COUNTRYTRANSLATION_LIST: 'countryTranslation/FETCH_COUNTRYTRANSLATION_LIST',
  FETCH_COUNTRYTRANSLATION: 'countryTranslation/FETCH_COUNTRYTRANSLATION',
  CREATE_COUNTRYTRANSLATION: 'countryTranslation/CREATE_COUNTRYTRANSLATION',
  UPDATE_COUNTRYTRANSLATION: 'countryTranslation/UPDATE_COUNTRYTRANSLATION',
  PARTIAL_UPDATE_COUNTRYTRANSLATION: 'countryTranslation/PARTIAL_UPDATE_COUNTRYTRANSLATION',
  DELETE_COUNTRYTRANSLATION: 'countryTranslation/DELETE_COUNTRYTRANSLATION',
  RESET: 'countryTranslation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICountryTranslation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CountryTranslationState = Readonly<typeof initialState>;

// Reducer

export default (state: CountryTranslationState = initialState, action): CountryTranslationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_COUNTRYTRANSLATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COUNTRYTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_COUNTRYTRANSLATION):
    case REQUEST(ACTION_TYPES.UPDATE_COUNTRYTRANSLATION):
    case REQUEST(ACTION_TYPES.DELETE_COUNTRYTRANSLATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_COUNTRYTRANSLATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_COUNTRYTRANSLATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COUNTRYTRANSLATION):
    case FAILURE(ACTION_TYPES.CREATE_COUNTRYTRANSLATION):
    case FAILURE(ACTION_TYPES.UPDATE_COUNTRYTRANSLATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_COUNTRYTRANSLATION):
    case FAILURE(ACTION_TYPES.DELETE_COUNTRYTRANSLATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_COUNTRYTRANSLATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_COUNTRYTRANSLATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_COUNTRYTRANSLATION):
    case SUCCESS(ACTION_TYPES.UPDATE_COUNTRYTRANSLATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_COUNTRYTRANSLATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_COUNTRYTRANSLATION):
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

const apiUrl = 'api/country-translations';

// Actions

export const getEntities: ICrudGetAllAction<ICountryTranslation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_COUNTRYTRANSLATION_LIST,
    payload: axios.get<ICountryTranslation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICountryTranslation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COUNTRYTRANSLATION,
    payload: axios.get<ICountryTranslation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICountryTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COUNTRYTRANSLATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICountryTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COUNTRYTRANSLATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICountryTranslation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_COUNTRYTRANSLATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICountryTranslation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COUNTRYTRANSLATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
