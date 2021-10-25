import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPromotion, defaultValue } from 'app/shared/model/promotion.model';

export const ACTION_TYPES = {
  FETCH_PROMOTION_LIST: 'promotion/FETCH_PROMOTION_LIST',
  FETCH_PROMOTION: 'promotion/FETCH_PROMOTION',
  CREATE_PROMOTION: 'promotion/CREATE_PROMOTION',
  UPDATE_PROMOTION: 'promotion/UPDATE_PROMOTION',
  PARTIAL_UPDATE_PROMOTION: 'promotion/PARTIAL_UPDATE_PROMOTION',
  DELETE_PROMOTION: 'promotion/DELETE_PROMOTION',
  RESET: 'promotion/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPromotion>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type PromotionState = Readonly<typeof initialState>;

// Reducer

export default (state: PromotionState = initialState, action): PromotionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PROMOTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PROMOTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PROMOTION):
    case REQUEST(ACTION_TYPES.UPDATE_PROMOTION):
    case REQUEST(ACTION_TYPES.DELETE_PROMOTION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PROMOTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PROMOTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PROMOTION):
    case FAILURE(ACTION_TYPES.CREATE_PROMOTION):
    case FAILURE(ACTION_TYPES.UPDATE_PROMOTION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PROMOTION):
    case FAILURE(ACTION_TYPES.DELETE_PROMOTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROMOTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROMOTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PROMOTION):
    case SUCCESS(ACTION_TYPES.UPDATE_PROMOTION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PROMOTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PROMOTION):
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

const apiUrl = 'api/promotions';

// Actions

export const getEntities: ICrudGetAllAction<IPromotion> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PROMOTION_LIST,
    payload: axios.get<IPromotion>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IPromotion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PROMOTION,
    payload: axios.get<IPromotion>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPromotion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PROMOTION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPromotion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PROMOTION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IPromotion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PROMOTION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPromotion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PROMOTION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
