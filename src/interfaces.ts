export interface Point {
  lat: number;
  lon: number;
}

export interface ScreenPoint {
  x: number;
  y: number;
}

export interface MapLoaded {
  renderObjectCount: number;
  curZoomModelsLoaded: number;
  curZoomPlacemarksLoaded: number;
  curZoomLabelsLoaded: number;
  curZoomGeometryLoaded: number;
  tileMemoryUsage: number;
  delayedGeometryLoaded: number;
  fullyAppeared: number;
  fullyLoaded: number;
}

export interface InitialRegion {
  lat: number;
  lon: number;
  zoom?: number;
  azimuth?: number;
  tilt?: number;
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

export type MasstransitVehicles =
  | 'bus'
  | 'trolleybus'
  | 'tramway'
  | 'minibus'
  | 'suburban'
  | 'underground'
  | 'ferry'
  | 'cable'
  | 'funicular';

export type MapType = 'none' | 'raster' | 'vector' | 'satellite' | 'hybrid';

export interface CameraPosition {
  zoom: number;
  tilt: number;
  azimuth: number;
  point: Point;
  finished: boolean;
}

export type VisibleRegion = {
  bottomLeft: Point;
  bottomRight: Point;
  topLeft: Point;
  topRight: Point;
};

export enum Animation {
  SMOOTH,
  LINEAR,
}
