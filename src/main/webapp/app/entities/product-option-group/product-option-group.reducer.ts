import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProductOptionGroup, defaultValue } from 'app/shared/model/product-option-group.model';

export const ACTION_TYPES = {
  FETCH_PRODUCTOPTIONGROUP_LIST: 'productOptionGroup/FETCH_PRODUCTOPTIONGROUP_LIST',
  FETCH_PRODUCTOPTIONGROUP: 'productOptionGroup/FETCH_PRODUCTOPTIONGROUP',
  CREATE_PRODUCTOPTIONGROUP: 'productOptionGroup/CREATE_PRODUCTOPTIONGROUP',
  UPDATE_PRODUCTOPTIONGROUP: 'productOptionGroup/UPDATE_PRODUCTOPTIONGROUP',
  PARTIAL_UPDATE_PRODUCTOPTIONGROUP: 'productOptionGroup/PARTIAL_UPDATE_PRODUCTOPTIONGROUP',
  DELETE_PRODUCTOPTIONGROUP: 'productOptionGroup/DELETE_PRODUCTOPTIONGROUP',
  RESET: 'productOptionGroup/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProductOptionGroup>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ProductOptionGroupState = Readonly<typeof initialState>;

// Reducer

export default (state: ProductOptionGroupState = initialState, action): ProductOptionGroupState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTOPTIONGROUP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PRODUCTOPTIONGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PRODUCTOPTIONGROUP):
    case REQUEST(ACTION_TYPES.UPDATE_PRODUCTOPTIONGROUP):
    case REQUEST(ACTION_TYPES.DELETE_PRODUCTOPTIONGROUP):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTIONGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTOPTIONGROUP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PRODUCTOPTIONGROUP):
    case FAILURE(ACTION_TYPES.CREATE_PRODUCTOPTIONGROUP):
    case FAILURE(ACTION_TYPES.UPDATE_PRODUCTOPTIONGROUP):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTIONGROUP):
    case FAILURE(ACTION_TYPES.DELETE_PRODUCTOPTIONGROUP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTOPTIONGROUP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PRODUCTOPTIONGROUP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PRODUCTOPTIONGROUP):
    case SUCCESS(ACTION_TYPES.UPDATE_PRODUCTOPTIONGROUP):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTIONGROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PRODUCTOPTIONGROUP):
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

const apiUrl = 'api/product-option-groups';

// Actions

export const getEntities: ICrudGetAllAction<IProductOptionGroup> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTOPTIONGROUP_LIST,
    payload: axios.get<IProductOptionGroup>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IProductOptionGroup> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PRODUCTOPTIONGROUP,
    payload: axios.get<IProductOptionGroup>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProductOptionGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PRODUCTOPTIONGROUP,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProductOptionGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PRODUCTOPTIONGROUP,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IProductOptionGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PRODUCTOPTIONGROUP,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProductOptionGroup> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PRODUCTOPTIONGROUP,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
