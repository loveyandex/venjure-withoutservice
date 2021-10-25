import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IExampleEntity, defaultValue } from 'app/shared/model/example-entity.model';

export const ACTION_TYPES = {
  FETCH_EXAMPLEENTITY_LIST: 'exampleEntity/FETCH_EXAMPLEENTITY_LIST',
  FETCH_EXAMPLEENTITY: 'exampleEntity/FETCH_EXAMPLEENTITY',
  CREATE_EXAMPLEENTITY: 'exampleEntity/CREATE_EXAMPLEENTITY',
  UPDATE_EXAMPLEENTITY: 'exampleEntity/UPDATE_EXAMPLEENTITY',
  PARTIAL_UPDATE_EXAMPLEENTITY: 'exampleEntity/PARTIAL_UPDATE_EXAMPLEENTITY',
  DELETE_EXAMPLEENTITY: 'exampleEntity/DELETE_EXAMPLEENTITY',
  RESET: 'exampleEntity/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IExampleEntity>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ExampleEntityState = Readonly<typeof initialState>;

// Reducer

export default (state: ExampleEntityState = initialState, action): ExampleEntityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EXAMPLEENTITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EXAMPLEENTITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_EXAMPLEENTITY):
    case REQUEST(ACTION_TYPES.UPDATE_EXAMPLEENTITY):
    case REQUEST(ACTION_TYPES.DELETE_EXAMPLEENTITY):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_EXAMPLEENTITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_EXAMPLEENTITY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EXAMPLEENTITY):
    case FAILURE(ACTION_TYPES.CREATE_EXAMPLEENTITY):
    case FAILURE(ACTION_TYPES.UPDATE_EXAMPLEENTITY):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_EXAMPLEENTITY):
    case FAILURE(ACTION_TYPES.DELETE_EXAMPLEENTITY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXAMPLEENTITY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXAMPLEENTITY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_EXAMPLEENTITY):
    case SUCCESS(ACTION_TYPES.UPDATE_EXAMPLEENTITY):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_EXAMPLEENTITY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_EXAMPLEENTITY):
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

const apiUrl = 'api/example-entities';

// Actions

export const getEntities: ICrudGetAllAction<IExampleEntity> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EXAMPLEENTITY_LIST,
    payload: axios.get<IExampleEntity>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IExampleEntity> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EXAMPLEENTITY,
    payload: axios.get<IExampleEntity>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IExampleEntity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EXAMPLEENTITY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IExampleEntity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EXAMPLEENTITY,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IExampleEntity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_EXAMPLEENTITY,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IExampleEntity> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EXAMPLEENTITY,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
