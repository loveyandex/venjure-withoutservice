import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGlobalSettings, defaultValue } from 'app/shared/model/global-settings.model';

export const ACTION_TYPES = {
  FETCH_GLOBALSETTINGS_LIST: 'globalSettings/FETCH_GLOBALSETTINGS_LIST',
  FETCH_GLOBALSETTINGS: 'globalSettings/FETCH_GLOBALSETTINGS',
  CREATE_GLOBALSETTINGS: 'globalSettings/CREATE_GLOBALSETTINGS',
  UPDATE_GLOBALSETTINGS: 'globalSettings/UPDATE_GLOBALSETTINGS',
  PARTIAL_UPDATE_GLOBALSETTINGS: 'globalSettings/PARTIAL_UPDATE_GLOBALSETTINGS',
  DELETE_GLOBALSETTINGS: 'globalSettings/DELETE_GLOBALSETTINGS',
  RESET: 'globalSettings/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGlobalSettings>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type GlobalSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: GlobalSettingsState = initialState, action): GlobalSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GLOBALSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GLOBALSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_GLOBALSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_GLOBALSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_GLOBALSETTINGS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_GLOBALSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_GLOBALSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GLOBALSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_GLOBALSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_GLOBALSETTINGS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_GLOBALSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_GLOBALSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GLOBALSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_GLOBALSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_GLOBALSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_GLOBALSETTINGS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_GLOBALSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_GLOBALSETTINGS):
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

const apiUrl = 'api/global-settings';

// Actions

export const getEntities: ICrudGetAllAction<IGlobalSettings> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GLOBALSETTINGS_LIST,
    payload: axios.get<IGlobalSettings>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IGlobalSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GLOBALSETTINGS,
    payload: axios.get<IGlobalSettings>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IGlobalSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GLOBALSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGlobalSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GLOBALSETTINGS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IGlobalSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_GLOBALSETTINGS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGlobalSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GLOBALSETTINGS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
