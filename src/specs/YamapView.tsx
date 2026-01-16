import type { HostComponent, ViewProps } from 'react-native';
import { codegenNativeCommands, codegenNativeComponent } from 'react-native';
import type {
  BubblingEventHandler,
  Int32,
  Double,
  WithDefault,
  UnsafeMixed,
} from 'react-native/Libraries/Types/CodegenTypesNamespace';
import type { Ref } from 'react';

interface Point {
  lat: Double;
  lon: Double;
}

interface ScreenPoint {
  x: Double;
  y: Double;
}

interface CameraPosition {
  zoom: Double;
  tilt: Double;
  azimuth: Double;
  point: Point;
  finished?: boolean;
}

interface VisibleRegion {
  bottomLeft: Point;
  bottomRight: Point;
  topLeft: Point;
  topRight: Point;
}

interface InitialRegion {
  lat: Double;
  lon: Double;
  zoom?: Double;
  azimuth?: Double;
  tilt?: Double;
}

interface MapLoaded {
  renderObjectCount: Int32;
  curZoomModelsLoaded: Int32;
  curZoomPlacemarksLoaded: Int32;
  curZoomLabelsLoaded: Int32;
  curZoomGeometryLoaded: Int32;
  tileMemoryUsage: Int32;
  delayedGeometryLoaded: Int32;
  fullyAppeared: Int32;
  fullyLoaded: Int32;
}

type Vehicles =
  | 'bus'
  | 'trolleybus'
  | 'tramway'
  | 'minibus'
  | 'suburban'
  | 'underground'
  | 'ferry'
  | 'cable'
  | 'funicular'
  | 'walk'
  | 'car';

type Animation = Int32; // SMOOTH | LINEAR

interface CameraPositionEvent extends CameraPosition {
  reason?: string;
}

interface CameraPositionCallbackEvent extends CameraPositionEvent {
  id: string;
}

interface RoutesEventPayload {
  id: string;
  status: string;
  routes: UnsafeMixed;
}

interface VisibleRegionEvent extends VisibleRegion {
  id: string;
}

interface WorldToScreenPointsEvent {
  id: string;
  screenPoints: {
    x: Double;
    y: Double;
  }[];
}

interface ScreenToWorldPointsEvent {
  id: string;
  worldPoints: {
    lat: Double;
    lon: Double;
  }[];
}

interface MapPointEvent {
  lat: Double;
  lon: Double;
}

export interface NativeProps extends ViewProps {
  userLocationIcon?: string; // resolved URI
  withClusters?: boolean;
  clusterColor?: Int32;
  showUserPosition?: boolean;
  nightMode?: boolean;
  mapStyle?: string;
  mapType?: WithDefault<'none' | 'raster' | 'vector', 'vector'>;
  userLocationAccuracyFillColor?: Int32;
  userLocationAccuracyStrokeColor?: Int32;
  userLocationAccuracyStrokeWidth?: Double;
  scrollGesturesEnabled?: boolean;
  zoomGesturesEnabled?: boolean;
  tiltGesturesEnabled?: boolean;
  rotateGesturesEnabled?: boolean;
  fastTapEnabled?: boolean;
  initialRegion?: InitialRegion;
  maxFps?: Int32;

  onCameraPositionChange?: BubblingEventHandler<CameraPositionEvent>;
  onCameraPositionChangeEnd?: BubblingEventHandler<CameraPositionEvent>;
  onMapPress?: BubblingEventHandler<MapPointEvent>;
  onMapLongPress?: BubblingEventHandler<MapPointEvent>;
  onMapLoaded?: BubblingEventHandler<MapLoaded>;

  // Callback-emitted events for command responses
  onRouteFound?: BubblingEventHandler<RoutesEventPayload>;
  onCameraPositionReceived?: BubblingEventHandler<CameraPositionCallbackEvent>;
  onVisibleRegionReceived?: BubblingEventHandler<VisibleRegionEvent>;
  onWorldToScreenPointsReceived?: BubblingEventHandler<WorldToScreenPointsEvent>;
  onScreenToWorldPointsReceived?: BubblingEventHandler<ScreenToWorldPointsEvent>;
}

interface NativeCommands {
  fitAllMarkers: (ref: Ref<HostComponent<NativeProps>>) => void;
  setTrafficVisible: (
    ref: Ref<HostComponent<NativeProps>>,
    isVisible: boolean
  ) => void;
  fitMarkers: (ref: Ref<HostComponent<NativeProps>>, points: Point[]) => void;
  setCenter: (
    ref: Ref<HostComponent<NativeProps>>,
    latitude: Double,
    longitude: Double,
    zoom: Double,
    azimuth: Double,
    tilt: Double,
    duration: Double,
    animation: Animation
  ) => void;
  setBounds: (
    ref: Ref<HostComponent<NativeProps>>,
    bottomLeftLatitude: Double,
    bottomLeftLongitude: Double,
    topRightLatitude: Double,
    topRightLongitude: Double,
    offset: Double,
    duration: Double,
    animation: Animation
  ) => void;
  setZoom: (
    ref: Ref<HostComponent<NativeProps>>,
    zoom: Double,
    duration: Double,
    animation: Animation
  ) => void;
  getCameraPosition: (
    ref: Ref<HostComponent<NativeProps>>,
    callbackId: string
  ) => void;
  getVisibleRegion: (
    ref: Ref<HostComponent<NativeProps>>,
    callbackId: string
  ) => void;
  getScreenPoints: (
    ref: Ref<HostComponent<NativeProps>>,
    points: Point[],
    callbackId: string
  ) => void;
  getWorldPoints: (
    ref: Ref<HostComponent<NativeProps>>,
    points: ScreenPoint[],
    callbackId: string
  ) => void;
  findRoutes: (
    ref: Ref<HostComponent<NativeProps>>,
    points: Point[],
    vehicles: Vehicles[],
    id: string
  ) => void;
}

export const Commands: NativeCommands = codegenNativeCommands<NativeCommands>({
  supportedCommands: [
    'fitAllMarkers',
    'setTrafficVisible',
    'fitMarkers',
    'setCenter',
    'setBounds',
    'setZoom',
    'getCameraPosition',
    'getVisibleRegion',
    'getScreenPoints',
    'getWorldPoints',
    'findRoutes',
  ],
});

export default codegenNativeComponent<NativeProps>('YamapView');
