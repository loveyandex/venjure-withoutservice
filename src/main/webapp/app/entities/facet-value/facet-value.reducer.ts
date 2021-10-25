import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFacetValue, defaultValue } from 'app/shared/model/facet-value.model';

export const ACTION_TYPES = {
  FETCH_FACETVALUE_LIST: 'facetValue/FETCH_FACETVALUE_LIST',
  FETCH_FACETVALUE: 'facetValue/FETCH_FACETVALUE',
  CREATE_FACETVALUE: 'facetValue/CREATE_FACETVALUE',
  UPDATE_FACETVALUE: 'facetValue/UPDATE_FACETVALUE',
  PARTIAL_UPDATE_FACETVALUE: 'facetValue/PARTIAL_UPDATE_FACETVALUE',
  DELETE_FACETVALUE: 'facetValue/DELETE_FACETVALUE',
  RESET: 'facetValue/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFacetValue>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FacetValueState = Readonly<typeof initialState>;

// Reducer

export default (state: FacetValueState = initialState, action): FacetValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FACETVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FACETVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FACETVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_FACETVALUE):
    case REQUEST(ACTION_TYPES.DELETE_FACETVALUE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_FACETVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FACETVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FACETVALUE):
    case FAILURE(ACTION_TYPES.CREATE_FACETVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_FACETVALUE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_FACETVALUE):
    case FAILURE(ACTION_TYPES.DELETE_FACETVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACETVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACETVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FACETVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_FACETVALUE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_FACETVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FACETVALUE):
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

const apiUrl = 'api/facet-values';

// Actions

export const getEntities: ICrudGetAllAction<IFacetValue> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FACETVALUE_LIST,
    payload: axios.get<IFacetValue>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFacetValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FACETVALUE,
    payload: axios.get<IFacetValue>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFacetValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FACETVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFacetValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACETVALUE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IFacetValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_FACETVALUE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFacetValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FACETVALUE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
