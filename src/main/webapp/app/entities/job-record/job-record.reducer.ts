import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IJobRecord, defaultValue } from 'app/shared/model/job-record.model';

export const ACTION_TYPES = {
  FETCH_JOBRECORD_LIST: 'jobRecord/FETCH_JOBRECORD_LIST',
  FETCH_JOBRECORD: 'jobRecord/FETCH_JOBRECORD',
  CREATE_JOBRECORD: 'jobRecord/CREATE_JOBRECORD',
  UPDATE_JOBRECORD: 'jobRecord/UPDATE_JOBRECORD',
  PARTIAL_UPDATE_JOBRECORD: 'jobRecord/PARTIAL_UPDATE_JOBRECORD',
  DELETE_JOBRECORD: 'jobRecord/DELETE_JOBRECORD',
  RESET: 'jobRecord/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IJobRecord>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type JobRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: JobRecordState = initialState, action): JobRecordState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_JOBRECORD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_JOBRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_JOBRECORD):
    case REQUEST(ACTION_TYPES.UPDATE_JOBRECORD):
    case REQUEST(ACTION_TYPES.DELETE_JOBRECORD):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_JOBRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_JOBRECORD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_JOBRECORD):
    case FAILURE(ACTION_TYPES.CREATE_JOBRECORD):
    case FAILURE(ACTION_TYPES.UPDATE_JOBRECORD):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_JOBRECORD):
    case FAILURE(ACTION_TYPES.DELETE_JOBRECORD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_JOBRECORD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_JOBRECORD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_JOBRECORD):
    case SUCCESS(ACTION_TYPES.UPDATE_JOBRECORD):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_JOBRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_JOBRECORD):
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

const apiUrl = 'api/job-records';

// Actions

export const getEntities: ICrudGetAllAction<IJobRecord> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_JOBRECORD_LIST,
    payload: axios.get<IJobRecord>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IJobRecord> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_JOBRECORD,
    payload: axios.get<IJobRecord>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IJobRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_JOBRECORD,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IJobRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_JOBRECORD,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IJobRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_JOBRECORD,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IJobRecord> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_JOBRECORD,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
