import {
  type HostComponent,
  type ViewProps,
  codegenNativeCommands,
  codegenNativeComponent,
  type CodegenTypes,
} from 'react-native';

export interface CameraPositionEvent {
  zoom: CodegenTypes.Double;
  tilt: CodegenTypes.Double;
  azimuth: CodegenTypes.Double;
  finished?: boolean;
  reason?: 'application' | 'gesture';
  point: {
    lat: CodegenTypes.Double;
    lon: CodegenTypes.Double;
  };
}

export interface CameraPositionCallbackEvent extends CameraPositionEvent {
  id: string;
}

export interface MapLoadedEvent {
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

export interface VisibleRegionEvent {
  id: string;
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

export interface WorldToScreenPointsEvent {
  screenPoints: {
    x: CodegenTypes.Double;
    y: CodegenTypes.Double;
  }[];
}

export interface ScreenToWorldPointsEvent {
  worldPoints: {
    lat: CodegenTypes.Double;
    lon: CodegenTypes.Double;
  }[];
}

export interface MapPointEvent {
  lat: CodegenTypes.Double;
  lon: CodegenTypes.Double;
}

export interface NativeProps extends ViewProps {
  nightMode?: boolean;
  mapType?: CodegenTypes.WithDefault<
    'none' | 'raster' | 'vector' | 'satellite' | 'hybrid',
    'vector'
  >;
  scrollGesturesEnabled?: boolean;
  zoomGesturesEnabled?: boolean;
  tiltGesturesEnabled?: boolean;
  rotateGesturesEnabled?: boolean;
  fastTapEnabled?: boolean;
  maxFps?: CodegenTypes.Int32;

  onMapLoaded?: CodegenTypes.BubblingEventHandler<MapLoadedEvent>;
  onMapPress?: CodegenTypes.BubblingEventHandler<MapPointEvent>;
  onMapLongPress?: CodegenTypes.BubblingEventHandler<MapPointEvent>;
  onCameraPositionChange?: CodegenTypes.BubblingEventHandler<CameraPositionEvent>;
}

export default codegenNativeComponent<NativeProps>('YamapView');
