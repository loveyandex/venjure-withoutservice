import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFacet, defaultValue } from 'app/shared/model/facet.model';

export const ACTION_TYPES = {
  FETCH_FACET_LIST: 'facet/FETCH_FACET_LIST',
  FETCH_FACET: 'facet/FETCH_FACET',
  CREATE_FACET: 'facet/CREATE_FACET',
  UPDATE_FACET: 'facet/UPDATE_FACET',
  PARTIAL_UPDATE_FACET: 'facet/PARTIAL_UPDATE_FACET',
  DELETE_FACET: 'facet/DELETE_FACET',
  RESET: 'facet/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFacet>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FacetState = Readonly<typeof initialState>;

// Reducer

export default (state: FacetState = initialState, action): FacetState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FACET_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FACET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FACET):
    case REQUEST(ACTION_TYPES.UPDATE_FACET):
    case REQUEST(ACTION_TYPES.DELETE_FACET):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_FACET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FACET_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FACET):
    case FAILURE(ACTION_TYPES.CREATE_FACET):
    case FAILURE(ACTION_TYPES.UPDATE_FACET):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_FACET):
    case FAILURE(ACTION_TYPES.DELETE_FACET):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACET_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACET):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FACET):
    case SUCCESS(ACTION_TYPES.UPDATE_FACET):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_FACET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FACET):
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

const apiUrl = 'api/facets';

// Actions

export const getEntities: ICrudGetAllAction<IFacet> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FACET_LIST,
    payload: axios.get<IFacet>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFacet> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FACET,
    payload: axios.get<IFacet>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFacet> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FACET,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFacet> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACET,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IFacet> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_FACET,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFacet> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FACET,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
