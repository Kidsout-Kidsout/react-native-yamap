import type { TurboModule } from "react-native/Libraries/TurboModule/RCTExport";
import { TurboModuleRegistry } from "react-native";
import { Double } from "react-native/Libraries/Types/CodegenTypes";

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
  YMKSuggestTypeBiz = 0b01 << 1,
  YMKSuggestTypeTransit = 0b01 << 2,
}

export type SuggestOptions = {
  userPosition?: Point;
  suggestWords?: boolean;
  suggestTypes?: SuggestTypes[];
};

export interface YamapSuggestSpec extends TurboModule {
  suggest(query: string): Promise<YamapSuggest[]>;
  suggestWithOptions(
    query: string,
    options: SuggestOptions
  ): Promise<YamapSuggest[]>;
  reset(): Promise<void>;
}

export default TurboModuleRegistry.get<YamapSuggestSpec>("YamapSuggests");
