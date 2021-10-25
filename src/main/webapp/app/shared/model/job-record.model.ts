import dayjs from 'dayjs';

export interface IJobRecord {
  id?: number;
  createdat?: string;
  updatedat?: string;
  queuename?: string;
  data?: string | null;
  state?: string;
  progress?: number;
  result?: string | null;
  error?: string | null;
  startedat?: string | null;
  settledat?: string | null;
  issettled?: boolean;
  retries?: number;
  attempts?: number;
}

export const defaultValue: Readonly<IJobRecord> = {
  issettled: false,
};
