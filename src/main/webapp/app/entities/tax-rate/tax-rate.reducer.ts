import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITaxRate, defaultValue } from 'app/shared/model/tax-rate.model';

export const ACTION_TYPES = {
  FETCH_TAXRATE_LIST: 'taxRate/FETCH_TAXRATE_LIST',
  FETCH_TAXRATE: 'taxRate/FETCH_TAXRATE',
  CREATE_TAXRATE: 'taxRate/CREATE_TAXRATE',
  UPDATE_TAXRATE: 'taxRate/UPDATE_TAXRATE',
  PARTIAL_UPDATE_TAXRATE: 'taxRate/PARTIAL_UPDATE_TAXRATE',
  DELETE_TAXRATE: 'taxRate/DELETE_TAXRATE',
  RESET: 'taxRate/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITaxRate>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type TaxRateState = Readonly<typeof initialState>;

// Reducer

export default (state: TaxRateState = initialState, action): TaxRateState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TAXRATE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TAXRATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TAXRATE):
    case REQUEST(ACTION_TYPES.UPDATE_TAXRATE):
    case REQUEST(ACTION_TYPES.DELETE_TAXRATE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_TAXRATE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_TAXRATE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TAXRATE):
    case FAILURE(ACTION_TYPES.CREATE_TAXRATE):
    case FAILURE(ACTION_TYPES.UPDATE_TAXRATE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_TAXRATE):
    case FAILURE(ACTION_TYPES.DELETE_TAXRATE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAXRATE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAXRATE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TAXRATE):
    case SUCCESS(ACTION_TYPES.UPDATE_TAXRATE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_TAXRATE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TAXRATE):
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

const apiUrl = 'api/tax-rates';

// Actions

export const getEntities: ICrudGetAllAction<ITaxRate> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TAXRATE_LIST,
    payload: axios.get<ITaxRate>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ITaxRate> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TAXRATE,
    payload: axios.get<ITaxRate>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITaxRate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TAXRATE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITaxRate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TAXRATE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ITaxRate> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_TAXRATE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITaxRate> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TAXRATE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
