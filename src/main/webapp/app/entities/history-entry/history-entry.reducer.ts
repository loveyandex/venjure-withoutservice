import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHistoryEntry, defaultValue } from 'app/shared/model/history-entry.model';

export const ACTION_TYPES = {
  FETCH_HISTORYENTRY_LIST: 'historyEntry/FETCH_HISTORYENTRY_LIST',
  FETCH_HISTORYENTRY: 'historyEntry/FETCH_HISTORYENTRY',
  CREATE_HISTORYENTRY: 'historyEntry/CREATE_HISTORYENTRY',
  UPDATE_HISTORYENTRY: 'historyEntry/UPDATE_HISTORYENTRY',
  PARTIAL_UPDATE_HISTORYENTRY: 'historyEntry/PARTIAL_UPDATE_HISTORYENTRY',
  DELETE_HISTORYENTRY: 'historyEntry/DELETE_HISTORYENTRY',
  RESET: 'historyEntry/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHistoryEntry>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type HistoryEntryState = Readonly<typeof initialState>;

// Reducer

export default (state: HistoryEntryState = initialState, action): HistoryEntryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_HISTORYENTRY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HISTORYENTRY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_HISTORYENTRY):
    case REQUEST(ACTION_TYPES.UPDATE_HISTORYENTRY):
    case REQUEST(ACTION_TYPES.DELETE_HISTORYENTRY):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_HISTORYENTRY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_HISTORYENTRY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HISTORYENTRY):
    case FAILURE(ACTION_TYPES.CREATE_HISTORYENTRY):
    case FAILURE(ACTION_TYPES.UPDATE_HISTORYENTRY):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_HISTORYENTRY):
    case FAILURE(ACTION_TYPES.DELETE_HISTORYENTRY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_HISTORYENTRY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_HISTORYENTRY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_HISTORYENTRY):
    case SUCCESS(ACTION_TYPES.UPDATE_HISTORYENTRY):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_HISTORYENTRY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_HISTORYENTRY):
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

const apiUrl = 'api/history-entries';

// Actions

export const getEntities: ICrudGetAllAction<IHistoryEntry> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORYENTRY_LIST,
    payload: axios.get<IHistoryEntry>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IHistoryEntry> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORYENTRY,
    payload: axios.get<IHistoryEntry>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IHistoryEntry> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HISTORYENTRY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHistoryEntry> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HISTORYENTRY,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IHistoryEntry> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_HISTORYENTRY,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHistoryEntry> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HISTORYENTRY,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
