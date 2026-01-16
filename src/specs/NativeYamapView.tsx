import * as React from 'react';
import {
  type HostComponent,
  type ViewProps,
  codegenNativeCommands,
  codegenNativeComponent,
  type CodegenTypes,
} from 'react-native';

interface CameraPosition {
  zoom: CodegenTypes.Double;
  tilt: CodegenTypes.Double;
  azimuth: CodegenTypes.Double;
  point: {
    lat: CodegenTypes.Double;
    lon: CodegenTypes.Double;
  };
  finished?: boolean;
}

interface VisibleRegion {
  bottomLeft: {
    lat: CodegenTypes.Double;
    lon: CodegenTypes.Double;
  };
  bottomRight: {
    lat: CodegenTypes.Double;
    lon: CodegenTypes.Double;
  };
  topLeft: {
    lat: CodegenTypes.Double;
    lon: CodegenTypes.Double;
  };
  topRight: {
    lat: CodegenTypes.Double;
    lon: CodegenTypes.Double;
  };
}

interface InitialRegion {
  lat: CodegenTypes.Double;
  lon: CodegenTypes.Double;
  zoom?: CodegenTypes.Double;
  azimuth?: CodegenTypes.Double;
  tilt?: CodegenTypes.Double;
}

interface MapLoaded {
  renderObjectCount: CodegenTypes.Int32;
  curZoomModelsLoaded: CodegenTypes.Int32;
  curZoomPlacemarksLoaded: CodegenTypes.Int32;
  curZoomLabelsLoaded: CodegenTypes.Int32;
  curZoomGeometryLoaded: CodegenTypes.Int32;
  tileMemoryUsage: CodegenTypes.Int32;
  delayedGeometryLoaded: CodegenTypes.Int32;
  fullyAppeared: CodegenTypes.Int32;
  fullyLoaded: CodegenTypes.Int32;
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

type Animation = CodegenTypes.Int32; // SMOOTH | LINEAR

interface CameraPositionEvent extends CameraPosition {
  reason?: string;
}

interface CameraPositionCallbackEvent extends CameraPositionEvent {
  id: string;
}

interface RoutesEventPayload {
  id: string;
  status: string;
  routes: CodegenTypes.UnsafeMixed;
}

interface VisibleRegionEvent extends VisibleRegion {
  id: string;
}

interface WorldToScreenPointsEvent {
  id: string;
  screenPoints: {
    x: CodegenTypes.Double;
    y: CodegenTypes.Double;
  }[];
}

interface ScreenToWorldPointsEvent {
  id: string;
  worldPoints: {
    lat: CodegenTypes.Double;
    lon: CodegenTypes.Double;
  }[];
}

interface MapPointEvent {
  lat: CodegenTypes.Double;
  lon: CodegenTypes.Double;
}

export interface NativeProps extends ViewProps {
  userLocationIcon?: string; // resolved URI
  withClusters?: boolean;
  clusterColor?: CodegenTypes.Int32;
  showUserPosition?: boolean;
  nightMode?: boolean;
  mapStyle?: string;
  mapType?: CodegenTypes.WithDefault<'none' | 'raster' | 'vector', 'vector'>;
  userLocationAccuracyFillColor?: CodegenTypes.Int32;
  userLocationAccuracyStrokeColor?: CodegenTypes.Int32;
  userLocationAccuracyStrokeWidth?: CodegenTypes.Double;
  scrollGesturesEnabled?: boolean;
  zoomGesturesEnabled?: boolean;
  tiltGesturesEnabled?: boolean;
  rotateGesturesEnabled?: boolean;
  fastTapEnabled?: boolean;
  initialRegion?: InitialRegion;
  maxFps?: CodegenTypes.Int32;

  onCameraPositionChange?: CodegenTypes.BubblingEventHandler<CameraPositionEvent>;
  onCameraPositionChangeEnd?: CodegenTypes.BubblingEventHandler<CameraPositionEvent>;
  onMapPress?: CodegenTypes.BubblingEventHandler<MapPointEvent>;
  onMapLongPress?: CodegenTypes.BubblingEventHandler<MapPointEvent>;
  onMapLoaded?: CodegenTypes.BubblingEventHandler<MapLoaded>;

  // Callback-emitted events for command responses
  onRouteFound?: CodegenTypes.BubblingEventHandler<RoutesEventPayload>;
  onCameraPositionReceived?: CodegenTypes.BubblingEventHandler<CameraPositionCallbackEvent>;
  onVisibleRegionReceived?: CodegenTypes.BubblingEventHandler<VisibleRegionEvent>;
  onWorldToScreenPointsReceived?: CodegenTypes.BubblingEventHandler<WorldToScreenPointsEvent>;
  onScreenToWorldPointsReceived?: CodegenTypes.BubblingEventHandler<ScreenToWorldPointsEvent>;
}

interface NativeCommands {
  fitAllMarkers: (ref: React.ElementRef<HostComponent<NativeProps>>) => void;
  setTrafficVisible: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    isVisible: boolean
  ) => void;
  fitMarkers: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    points: CodegenTypes.UnsafeMixed[]
  ) => void;
  setCenter: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    latitude: CodegenTypes.Double,
    longitude: CodegenTypes.Double,
    zoom: CodegenTypes.Double,
    azimuth: CodegenTypes.Double,
    tilt: CodegenTypes.Double,
    duration: CodegenTypes.Double,
    animation: Animation
  ) => void;
  setBounds: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    bottomLeftLatitude: CodegenTypes.Double,
    bottomLeftLongitude: CodegenTypes.Double,
    topRightLatitude: CodegenTypes.Double,
    topRightLongitude: CodegenTypes.Double,
    offset: CodegenTypes.Double,
    duration: CodegenTypes.Double,
    animation: Animation
  ) => void;
  setZoom: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    zoom: CodegenTypes.Double,
    duration: CodegenTypes.Double,
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
    points: CodegenTypes.UnsafeMixed[],
    callbackId: string
  ) => void;
  getWorldPoints: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    points: CodegenTypes.UnsafeMixed[],
    callbackId: string
  ) => void;
  findRoutes: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    points: CodegenTypes.UnsafeMixed[],
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
