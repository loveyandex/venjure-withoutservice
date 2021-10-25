import dayjs from 'dayjs';

export interface ITag {
  id?: number;
  createdat?: string;
  updatedat?: string;
  value?: string;
}

export const defaultValue: Readonly<ITag> = {};
