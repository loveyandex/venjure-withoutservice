import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStockMovement, defaultValue } from 'app/shared/model/stock-movement.model';

export const ACTION_TYPES = {
  FETCH_STOCKMOVEMENT_LIST: 'stockMovement/FETCH_STOCKMOVEMENT_LIST',
  FETCH_STOCKMOVEMENT: 'stockMovement/FETCH_STOCKMOVEMENT',
  CREATE_STOCKMOVEMENT: 'stockMovement/CREATE_STOCKMOVEMENT',
  UPDATE_STOCKMOVEMENT: 'stockMovement/UPDATE_STOCKMOVEMENT',
  PARTIAL_UPDATE_STOCKMOVEMENT: 'stockMovement/PARTIAL_UPDATE_STOCKMOVEMENT',
  DELETE_STOCKMOVEMENT: 'stockMovement/DELETE_STOCKMOVEMENT',
  RESET: 'stockMovement/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStockMovement>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type StockMovementState = Readonly<typeof initialState>;

// Reducer

export default (state: StockMovementState = initialState, action): StockMovementState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STOCKMOVEMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STOCKMOVEMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_STOCKMOVEMENT):
    case REQUEST(ACTION_TYPES.UPDATE_STOCKMOVEMENT):
    case REQUEST(ACTION_TYPES.DELETE_STOCKMOVEMENT):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_STOCKMOVEMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_STOCKMOVEMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STOCKMOVEMENT):
    case FAILURE(ACTION_TYPES.CREATE_STOCKMOVEMENT):
    case FAILURE(ACTION_TYPES.UPDATE_STOCKMOVEMENT):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_STOCKMOVEMENT):
    case FAILURE(ACTION_TYPES.DELETE_STOCKMOVEMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_STOCKMOVEMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_STOCKMOVEMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_STOCKMOVEMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_STOCKMOVEMENT):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_STOCKMOVEMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_STOCKMOVEMENT):
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

const apiUrl = 'api/stock-movements';

// Actions

export const getEntities: ICrudGetAllAction<IStockMovement> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_STOCKMOVEMENT_LIST,
    payload: axios.get<IStockMovement>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IStockMovement> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STOCKMOVEMENT,
    payload: axios.get<IStockMovement>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IStockMovement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STOCKMOVEMENT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStockMovement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STOCKMOVEMENT,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IStockMovement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_STOCKMOVEMENT,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStockMovement> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STOCKMOVEMENT,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
