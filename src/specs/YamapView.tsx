import type { HostComponent, ViewProps } from 'react-native';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';
import codegenNativeCommands from 'react-native/Libraries/Utilities/codegenNativeCommands';
import type {
  BubblingEventHandler,
  Int32,
  Double,
  WithDefault,
  UnsafeMixed,
} from 'react-native/Libraries/Types/CodegenTypes';
import * as React from 'react';

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

interface CameraPositionEvent {
  zoom: Double;
  tilt: Double;
  azimuth: Double;
  finished?: boolean;
  reason?: string;
  point: {
    lat: Double;
    lon: Double;
  };
}

interface CameraPositionCallbackEvent extends CameraPositionEvent {
  id: string;
}

interface RoutesEventPayload {
  id: string;
  status: string;
  routes: UnsafeMixed;
}

interface VisibleRegionEvent {
  id: string;
  bottomLeft: {
    lat: Double;
    lon: Double;
  };
  bottomRight: {
    lat: Double;
    lon: Double;
  };
  topLeft: {
    lat: Double;
    lon: Double;
  };
  topRight: {
    lat: Double;
    lon: Double;
  };
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
  fitAllMarkers: (ref: React.ElementRef<HostComponent<NativeProps>>) => void;
  setTrafficVisible: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    isVisible: boolean
  ) => void;
  fitMarkers: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    points: Point[]
  ) => void;
  setCenter: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    latitude: Double,
    longitude: Double,
    zoom: Double,
    azimuth: Double,
    tilt: Double,
    duration: Double,
    animation: Animation
  ) => void;
  setBounds: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    bottomLeftLatitude: Double,
    bottomLeftLongitude: Double,
    topRightLatitude: Double,
    topRightLongitude: Double,
    offset: Double,
    duration: Double,
    animation: Animation
  ) => void;
  setZoom: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    zoom: Double,
    duration: Double,
    animation: Animation
  ) => void;
  getCameraPosition: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    callbackId: string
  ) => void;
  getVisibleRegion: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    callbackId: string
  ) => void;
  getScreenPoints: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    points: Point[],
    callbackId: string
  ) => void;
  getWorldPoints: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    points: ScreenPoint[],
    callbackId: string
  ) => void;
  findRoutes: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
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
