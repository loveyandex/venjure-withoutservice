import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITaxCategory, defaultValue } from 'app/shared/model/tax-category.model';

export const ACTION_TYPES = {
  FETCH_TAXCATEGORY_LIST: 'taxCategory/FETCH_TAXCATEGORY_LIST',
  FETCH_TAXCATEGORY: 'taxCategory/FETCH_TAXCATEGORY',
  CREATE_TAXCATEGORY: 'taxCategory/CREATE_TAXCATEGORY',
  UPDATE_TAXCATEGORY: 'taxCategory/UPDATE_TAXCATEGORY',
  PARTIAL_UPDATE_TAXCATEGORY: 'taxCategory/PARTIAL_UPDATE_TAXCATEGORY',
  DELETE_TAXCATEGORY: 'taxCategory/DELETE_TAXCATEGORY',
  RESET: 'taxCategory/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITaxCategory>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type TaxCategoryState = Readonly<typeof initialState>;

// Reducer

export default (state: TaxCategoryState = initialState, action): TaxCategoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TAXCATEGORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TAXCATEGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TAXCATEGORY):
    case REQUEST(ACTION_TYPES.UPDATE_TAXCATEGORY):
    case REQUEST(ACTION_TYPES.DELETE_TAXCATEGORY):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_TAXCATEGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_TAXCATEGORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TAXCATEGORY):
    case FAILURE(ACTION_TYPES.CREATE_TAXCATEGORY):
    case FAILURE(ACTION_TYPES.UPDATE_TAXCATEGORY):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_TAXCATEGORY):
    case FAILURE(ACTION_TYPES.DELETE_TAXCATEGORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAXCATEGORY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAXCATEGORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TAXCATEGORY):
    case SUCCESS(ACTION_TYPES.UPDATE_TAXCATEGORY):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_TAXCATEGORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TAXCATEGORY):
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

const apiUrl = 'api/tax-categories';

// Actions

export const getEntities: ICrudGetAllAction<ITaxCategory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TAXCATEGORY_LIST,
    payload: axios.get<ITaxCategory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ITaxCategory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TAXCATEGORY,
    payload: axios.get<ITaxCategory>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITaxCategory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TAXCATEGORY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITaxCategory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TAXCATEGORY,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ITaxCategory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_TAXCATEGORY,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITaxCategory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TAXCATEGORY,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
