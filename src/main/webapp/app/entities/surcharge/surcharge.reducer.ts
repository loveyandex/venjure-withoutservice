import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISurcharge, defaultValue } from 'app/shared/model/surcharge.model';

export const ACTION_TYPES = {
  FETCH_SURCHARGE_LIST: 'surcharge/FETCH_SURCHARGE_LIST',
  FETCH_SURCHARGE: 'surcharge/FETCH_SURCHARGE',
  CREATE_SURCHARGE: 'surcharge/CREATE_SURCHARGE',
  UPDATE_SURCHARGE: 'surcharge/UPDATE_SURCHARGE',
  PARTIAL_UPDATE_SURCHARGE: 'surcharge/PARTIAL_UPDATE_SURCHARGE',
  DELETE_SURCHARGE: 'surcharge/DELETE_SURCHARGE',
  RESET: 'surcharge/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISurcharge>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SurchargeState = Readonly<typeof initialState>;

// Reducer

export default (state: SurchargeState = initialState, action): SurchargeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SURCHARGE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SURCHARGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SURCHARGE):
    case REQUEST(ACTION_TYPES.UPDATE_SURCHARGE):
    case REQUEST(ACTION_TYPES.DELETE_SURCHARGE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SURCHARGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SURCHARGE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SURCHARGE):
    case FAILURE(ACTION_TYPES.CREATE_SURCHARGE):
    case FAILURE(ACTION_TYPES.UPDATE_SURCHARGE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SURCHARGE):
    case FAILURE(ACTION_TYPES.DELETE_SURCHARGE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SURCHARGE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SURCHARGE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SURCHARGE):
    case SUCCESS(ACTION_TYPES.UPDATE_SURCHARGE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SURCHARGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SURCHARGE):
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

const apiUrl = 'api/surcharges';

// Actions

export const getEntities: ICrudGetAllAction<ISurcharge> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SURCHARGE_LIST,
    payload: axios.get<ISurcharge>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISurcharge> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SURCHARGE,
    payload: axios.get<ISurcharge>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISurcharge> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SURCHARGE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISurcharge> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SURCHARGE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ISurcharge> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SURCHARGE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISurcharge> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SURCHARGE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
