import { TurboModuleRegistry, type TurboModule } from 'react-native';
import { type Double } from 'react-native/Libraries/Types/CodegenTypesNamespace';

type Point = {
  lat: Double;
  lon: Double;
};

export type YamapSuggest = {
  title: string;
  subtitle?: string;
  uri?: string;
  searchText: string;
  displayText?: string;
};

export enum SuggestTypes {
  YMKSuggestTypeUnspecified = 0b00,
  YMKSuggestTypeGeo = 0b01,
  // eslint-disable-next-line no-bitwise
  YMKSuggestTypeBiz = 0b01 << 1,
  // eslint-disable-next-line no-bitwise
  YMKSuggestTypeTransit = 0b01 << 2,
}

export type SuggestOptions = {
  userPosition?: Point;
  suggestWords?: boolean;
  suggestTypes?: SuggestTypes[];
};

export interface Spec extends TurboModule {
  suggest(query: string): Promise<YamapSuggest[]>;
  suggestWithOptions(
    query: string,
    options: SuggestOptions
  ): Promise<YamapSuggest[]>;
  reset(): Promise<void>;
}

export default TurboModuleRegistry.get<Spec>('YamapSuggests');
