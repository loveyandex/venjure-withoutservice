import dayjs from 'dayjs';

export interface IExampleEntity {
  id?: number;
  createdat?: string;
  updatedat?: string;
  name?: string;
}

export const defaultValue: Readonly<IExampleEntity> = {};
