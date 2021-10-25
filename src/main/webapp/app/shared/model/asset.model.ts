import dayjs from 'dayjs';

export interface IAsset {
  id?: number;
  createdat?: string;
  updatedat?: string;
  name?: string;
  type?: string;
  mimetype?: string;
  width?: number;
  height?: number;
  filesize?: number;
  source?: string;
  preview?: string;
  focalpoint?: string | null;
}

export const defaultValue: Readonly<IAsset> = {};
