import dayjs from 'dayjs';
import { IChannel } from 'app/shared/model/channel.model';
import { IFacetTranslation } from 'app/shared/model/facet-translation.model';
import { IFacetValue } from 'app/shared/model/facet-value.model';

export interface IFacet {
  id?: number;
  createdat?: string;
  updatedat?: string;
  isprivate?: boolean;
  code?: string;
  channels?: IChannel[] | null;
  facetTranslations?: IFacetTranslation[] | null;
  facetValues?: IFacetValue[] | null;
}

export const defaultValue: Readonly<IFacet> = {
  isprivate: false,
};
