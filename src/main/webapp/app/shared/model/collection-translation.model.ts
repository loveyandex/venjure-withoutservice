import dayjs from 'dayjs';
import { ICollection } from 'app/shared/model/collection.model';

export interface ICollectionTranslation {
  id?: number;
  createdat?: string;
  updatedat?: string;
  languagecode?: string;
  name?: string;
  slug?: string;
  description?: string;
  base?: ICollection | null;
}

export const defaultValue: Readonly<ICollectionTranslation> = {};
