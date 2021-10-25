import dayjs from 'dayjs';
import { IFacet } from 'app/shared/model/facet.model';

export interface IFacetTranslation {
  id?: number;
  createdat?: string;
  updatedat?: string;
  languagecode?: string;
  name?: string;
  base?: IFacet | null;
}

export const defaultValue: Readonly<IFacetTranslation> = {};
