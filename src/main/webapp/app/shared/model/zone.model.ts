import dayjs from 'dayjs';
import { ICountry } from 'app/shared/model/country.model';

export interface IZone {
  id?: number;
  createdat?: string;
  updatedat?: string;
  name?: string;
  countries?: ICountry[] | null;
}

export const defaultValue: Readonly<IZone> = {};
