import dayjs from 'dayjs';
import { ICountry } from 'app/shared/model/country.model';

export interface ICountryTranslation {
  id?: number;
  createdat?: string;
  updatedat?: string;
  languagecode?: string;
  name?: string;
  base?: ICountry | null;
}

export const defaultValue: Readonly<ICountryTranslation> = {};
