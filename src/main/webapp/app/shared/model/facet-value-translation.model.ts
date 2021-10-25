import dayjs from 'dayjs';
import { IFacetValue } from 'app/shared/model/facet-value.model';

export interface IFacetValueTranslation {
  id?: number;
  createdat?: string;
  updatedat?: string;
  languagecode?: string;
  name?: string;
  base?: IFacetValue | null;
}

export const defaultValue: Readonly<IFacetValueTranslation> = {};
