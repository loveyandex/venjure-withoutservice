import dayjs from 'dayjs';
import { ICountryTranslation } from 'app/shared/model/country-translation.model';
import { IZone } from 'app/shared/model/zone.model';

export interface ICountry {
  id?: number;
  createdat?: string;
  updatedat?: string;
  code?: string;
  enabled?: boolean;
  countryTranslations?: ICountryTranslation[] | null;
  zones?: IZone[] | null;
}

export const defaultValue: Readonly<ICountry> = {
  enabled: false,
};
