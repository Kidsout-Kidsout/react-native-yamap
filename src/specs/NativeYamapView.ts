import {
  type HostComponent,
  type ViewProps,
  codegenNativeCommands,
  codegenNativeComponent,
  type CodegenTypes,
} from 'react-native';

export interface CameraPosition {
  zoom: CodegenTypes.Double;
  tilt: CodegenTypes.Double;
  azimuth: CodegenTypes.Double;
  point: {
    lat: CodegenTypes.Double;
    lon: CodegenTypes.Double;
  };
}

export interface CameraPositionEvent extends CameraPosition {
  reason?: 'application' | 'gesture';
  finished?: boolean;
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

export interface VisibleRegion {
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

export interface MapPointEvent {
  lat: CodegenTypes.Double;
  lon: CodegenTypes.Double;
}

export interface CameraMoveNativeEvent {
  cid: string;
  completed: boolean;
}

export interface CameraPositionReceivedEvent extends CameraPosition {
  cid: string;
}

export interface VisibleRegionReceivedEvent extends VisibleRegion {
  cid: string;
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

  onMapLoaded?: CodegenTypes.DirectEventHandler<MapLoadedEvent>;
  onMapPress?: CodegenTypes.DirectEventHandler<MapPointEvent>;
  onMapLongPress?: CodegenTypes.DirectEventHandler<MapPointEvent>;
  onCameraPositionChange?: CodegenTypes.DirectEventHandler<CameraPositionEvent>;

  onCommandSetCenterReceived: CodegenTypes.DirectEventHandler<CameraMoveNativeEvent>;
  onCommandSetBoundsReceived: CodegenTypes.DirectEventHandler<CameraMoveNativeEvent>;
  onCommandSetZoomReceived: CodegenTypes.DirectEventHandler<CameraMoveNativeEvent>;
  onCommandGetCameraPositionReceived: CodegenTypes.DirectEventHandler<CameraPositionReceivedEvent>;
  onCommandGetVisibleRegionReceived: CodegenTypes.DirectEventHandler<VisibleRegionReceivedEvent>;
}

interface NativeCommands {
  commandSetCenter: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    cid: string,
    lat: CodegenTypes.Double,
    lon: CodegenTypes.Double,
    zoom: CodegenTypes.Double,
    azimuth: CodegenTypes.Double,
    tilt: CodegenTypes.Double,
    offset: CodegenTypes.Double,

    animationType: CodegenTypes.Int32, // 0 - linear, 1 - smooth
    animationDuration: CodegenTypes.Double
  ) => void;
  commandSetBounds: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    cid: string,
    bottomLeftPointLat: CodegenTypes.Double,
    bottomLeftPointLon: CodegenTypes.Double,
    topRightPointLat: CodegenTypes.Double,
    topRightPointLon: CodegenTypes.Double,
    minZoom: CodegenTypes.Double,
    maxZoom: CodegenTypes.Double,
    offset: CodegenTypes.Double,

    animationType: CodegenTypes.Int32, // 0 - linear, 1 - smooth
    animationDuration: CodegenTypes.Double
  ) => void;
  commandSetZoom: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    cid: string,
    zoom: CodegenTypes.Double,
    offset: CodegenTypes.Double,

    animationType: CodegenTypes.Int32, // 0 - linear, 1 - smooth
    animationDuration: CodegenTypes.Double
  ) => void;
  commandGetCameraPosition: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    cid: string
  ) => void;
  commandGetVisibleRegion: (
    ref: React.ElementRef<HostComponent<NativeProps>>,
    cid: string
  ) => void;
}

export const Commands: NativeCommands = codegenNativeCommands<NativeCommands>({
  supportedCommands: [
    'commandSetCenter',
    'commandSetBounds',
    'commandSetZoom',
    'commandGetCameraPosition',
    'commandGetVisibleRegion',
  ],
});

export default codegenNativeComponent<NativeProps>('YamapView');
