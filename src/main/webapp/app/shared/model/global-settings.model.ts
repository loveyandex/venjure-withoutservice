import dayjs from 'dayjs';

export interface IGlobalSettings {
  id?: number;
  createdat?: string;
  updatedat?: string;
  availablelanguages?: string;
  trackinventory?: boolean;
  outofstockthreshold?: number;
}

export const defaultValue: Readonly<IGlobalSettings> = {
  trackinventory: false,
};
