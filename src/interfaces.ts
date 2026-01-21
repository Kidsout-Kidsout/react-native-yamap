export type Animation = { type: 'smooth' | 'linear'; duration: number };

export interface Point {
  lat: number;
  lon: number;
}

export type AddressComponent =
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

export type MapType = 'none' | 'raster' | 'vector' | 'satellite' | 'hybrid';

export interface CameraPosition {
  point: Point;
  zoom: number;
  tilt: number;
  azimuth: number;
}

export type VisibleRegion = {
  bottomLeft: Point;
  bottomRight: Point;
  topLeft: Point;
  topRight: Point;
};

export type YamapGeocodeResult = {
  name: string;
  descriptionText: string;
  formattedAddress: string;
  coords: Point;
  upperCorner: Point;
  lowerCorner: Point;
  components: { name: string; kinds: AddressComponent[] }[];
};

export type YamapSuggestResult = {
  title: string;
  subtitle?: string;
  uri?: string;
  searchText: string;
  displayText?: string;
};

export type YamapSuggestResultWithCoords = YamapSuggestResult & Partial<Point>;

export enum YamapSuggestTypes {
  YMKSuggestTypeUnspecified = 0b00,
  YMKSuggestTypeGeo = 0b01,
  // eslint-disable-next-line no-bitwise
  YMKSuggestTypeBiz = 0b01 << 1,
  // eslint-disable-next-line no-bitwise
  YMKSuggestTypeTransit = 0b01 << 2,
}

export type YamapSuggestOptions = {
  userPosition?: Point;
  suggestWords?: boolean;
  suggestTypes?: YamapSuggestTypes[];
};
