import {
  TurboModuleRegistry,
  type TurboModule,
  type CodegenTypes,
} from 'react-native';

type AddressComponent =
  | 'unknown'
  | 'country'
  | 'region'
  | 'province'
  | 'area'
  | 'locality'
  | 'district'
  | 'street'
  | 'house'
  | 'entrance'
  | 'route'
  | 'station'
  | 'metro'
  | 'railway'
  | 'vegetation'
  | 'hydro'
  | 'airport'
  | 'other';

export type YamapGeocodeResult = {
  name: string;
  descriptionText: string;
  formattedAddress: string;
  coords: { lat: CodegenTypes.Double; lon: CodegenTypes.Double };
  upperCorner: { lat: CodegenTypes.Double; lon: CodegenTypes.Double };
  lowerCorner: { lat: CodegenTypes.Double; lon: CodegenTypes.Double };
  components: { name: string; kinds: AddressComponent[] }[];
};

export interface Spec extends TurboModule {
  geocode(query: string): Promise<YamapGeocodeResult | undefined>;
  geocodeUri(uri: string): Promise<YamapGeocodeResult | undefined>;
  geocodePoint(coords: number[]): Promise<YamapGeocodeResult | undefined>;
}

export default TurboModuleRegistry.get<Spec>('YamapGeocode');
