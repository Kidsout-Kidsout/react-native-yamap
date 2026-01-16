import React, { type PropsWithChildren } from 'react';
import {
  type ViewProps,
  type ImageSourcePropType,
  type NativeSyntheticEvent,
} from 'react-native';
// @ts-ignore
import { resolveAssetSource } from 'react-native';
import CallbacksManager from '../utils/CallbacksManager';
import {
  type Point,
  type ScreenPoint,
  type DrivingInfo,
  type MasstransitInfo,
  type RoutesFoundEvent,
  type Vehicles,
  type CameraPosition,
  type VisibleRegion,
  type InitialRegion,
  type MapType,
  Animation,
  type MapLoaded,
} from '../interfaces';
import { processColorProps } from '../utils';
import NativeModule from '../specs/NativeYamap';
import YamapNativeComponent, {
  type NativeProps as YamapNativeProps,
} from '../specs/NativeYamapView';
import { invariant } from '../utils/invariant';

const getModule = () =>
  NativeModule ?? invariant('Yamap native module is not linked.');

export interface YaMapProps extends PropsWithChildren<ViewProps> {
  userLocationIcon?: ImageSourcePropType;
  withClusters?: boolean;
  clusterColor?: string;
  showUserPosition?: boolean;
  nightMode?: boolean;
  mapStyle?: string;
  mapType?: MapType;
  onCameraPositionChange?: (
    event: NativeSyntheticEvent<CameraPosition>
  ) => void;
  onCameraPositionChangeEnd?: (
    event: NativeSyntheticEvent<CameraPosition>
  ) => void;
  onMapPress?: (event: NativeSyntheticEvent<Point>) => void;
  onMapLongPress?: (event: NativeSyntheticEvent<Point>) => void;
  onMapLoaded?: (event: NativeSyntheticEvent<MapLoaded>) => void;
  userLocationAccuracyFillColor?: string;
  userLocationAccuracyStrokeColor?: string;
  userLocationAccuracyStrokeWidth?: number;
  scrollGesturesEnabled?: boolean;
  zoomGesturesEnabled?: boolean;
  tiltGesturesEnabled?: boolean;
  rotateGesturesEnabled?: boolean;
  fastTapEnabled?: boolean;
  initialRegion?: InitialRegion;
  maxFps?: number;
}

export class YaMap extends React.Component<YaMapProps> {
  static defaultProps = {
    showUserPosition: true,
    clusterColor: 'red',
    maxFps: 60,
  };

  private mapRef = React.createRef<any>();
  private hasLaidOut = false;

  static ALL_MASSTRANSIT_VEHICLES: Vehicles[] = [
    'bus',
    'trolleybus',
    'tramway',
    'minibus',
    'suburban',
    'underground',
    'ferry',
    'cable',
    'funicular',
  ];

  public static init(apiKey: string): Promise<void> {
    const NativeYamapModule = getModule();
    return NativeYamapModule.init(apiKey);
  }

  public static setLocale(locale: string): Promise<void> {
    const NativeYamapModule = getModule();
    return NativeYamapModule.setLocale(locale);
  }

  public static getLocale(): Promise<string> {
    const NativeYamapModule = getModule();
    return NativeYamapModule.getLocale();
  }

  public static resetLocale(): Promise<void> {
    const NativeYamapModule = getModule();
    return NativeYamapModule.resetLocale();
  }

  public findRoutes(
    points: Point[],
    vehicles: Vehicles[],
    callback: (event: RoutesFoundEvent<DrivingInfo | MasstransitInfo>) => void
  ) {
    this._findRoutes(points, vehicles, callback);
  }

  public findMasstransitRoutes(
    points: Point[],
    callback: (event: RoutesFoundEvent<MasstransitInfo>) => void
  ) {
    this._findRoutes(points, YaMap.ALL_MASSTRANSIT_VEHICLES, callback);
  }

  public findPedestrianRoutes(
    points: Point[],
    callback: (event: RoutesFoundEvent<MasstransitInfo>) => void
  ) {
    this._findRoutes(points, [], callback);
  }

  public findDrivingRoutes(
    points: Point[],
    callback: (event: RoutesFoundEvent<DrivingInfo>) => void
  ) {
    this._findRoutes(points, ['car'], callback);
  }

  public fitAllMarkers() {
    if (this.mapRef.current) {
      YamapCommands.fitAllMarkers(this.mapRef.current);
    }
  }

  public setTrafficVisible(isVisible: boolean) {
    if (this.mapRef.current) {
      YamapCommands.setTrafficVisible(this.mapRef.current, isVisible);
    }
  }

  public fitMarkers(points: Point[]) {
    if (this.mapRef.current) {
      YamapCommands.fitMarkers(this.mapRef.current, points);
    }
  }

  public setCenter(
    center: { lon: number; lat: number; zoom?: number },
    zoom: number = center.zoom || 10,
    azimuth: number = 0,
    tilt: number = 0,
    duration: number = 0,
    animation: Animation = Animation.SMOOTH
  ) {
    if (this.mapRef.current) {
      YamapCommands.setCenter(
        this.mapRef.current,
        center.lat,
        center.lon,
        zoom,
        azimuth,
        tilt,
        duration,
        animation
      );
    }
  }

  public setBounds(
    bottomLeft: { lon: number; lat: number },
    topRight: { lon: number; lat: number },
    offset: number = 0,
    duration: number = 0,
    animation: Animation = Animation.SMOOTH
  ) {
    if (this.mapRef.current) {
      YamapCommands.setBounds(
        this.mapRef.current,
        bottomLeft.lat,
        bottomLeft.lon,
        topRight.lat,
        topRight.lon,
        offset,
        duration,
        animation
      );
    }
  }

  public setZoom(
    zoom: number,
    duration: number = 0,
    animation: Animation = Animation.SMOOTH
  ) {
    if (this.mapRef.current) {
      YamapCommands.setZoom(this.mapRef.current, zoom, duration, animation);
    }
  }

  public getCameraPosition(callback: (position: CameraPosition) => void) {
    const cbId = CallbacksManager.addCallback(callback);
    if (this.mapRef.current) {
      YamapCommands.getCameraPosition(this.mapRef.current, cbId);
    }
  }

  public getVisibleRegion(callback: (VisibleRegion: VisibleRegion) => void) {
    const cbId = CallbacksManager.addCallback(callback);
    if (this.mapRef.current) {
      YamapCommands.getVisibleRegion(this.mapRef.current, cbId);
    }
  }

  public getScreenPoints(
    points: Point[],
    callback: (screenPoint: ScreenPoint) => void
  ) {
    const cbId = CallbacksManager.addCallback(callback);
    if (this.mapRef.current) {
      YamapCommands.getScreenPoints(this.mapRef.current, points, cbId);
    }
  }

  public getWorldPoints(
    points: ScreenPoint[],
    callback: (point: Point) => void
  ) {
    const cbId = CallbacksManager.addCallback(callback);
    if (this.mapRef.current) {
      YamapCommands.getWorldPoints(this.mapRef.current, points, cbId);
    }
  }

  private _findRoutes(
    points: Point[],
    vehicles: Vehicles[],
    callback:
      | ((event: RoutesFoundEvent<DrivingInfo | MasstransitInfo>) => void)
      | ((event: RoutesFoundEvent<DrivingInfo>) => void)
      | ((event: RoutesFoundEvent<MasstransitInfo>) => void)
  ) {
    const cbId = CallbacksManager.addCallback(callback);
    if (this.mapRef.current) {
      YamapCommands.findRoutes(this.mapRef.current, points, vehicles, cbId);
    }
  }

  private processRoute(
    event: NativeSyntheticEvent<
      { id: string } & RoutesFoundEvent<DrivingInfo | MasstransitInfo>
    >
  ) {
    const { id, ...routes } = event.nativeEvent;
    CallbacksManager.call(id, routes);
  }

  private processCameraPosition(
    event: NativeSyntheticEvent<{ id: string } & CameraPosition>
  ) {
    const { id, ...point } = event.nativeEvent;
    CallbacksManager.call(id, point);
  }

  private processVisibleRegion(
    event: NativeSyntheticEvent<{ id: string } & VisibleRegion>
  ) {
    const { id, ...visibleRegion } = event.nativeEvent;
    CallbacksManager.call(id, visibleRegion);
  }

  private processWorldToScreenPointsReceived(
    event: NativeSyntheticEvent<{ id: string } & ScreenPoint[]>
  ) {
    const { id, ...screenPoints } = event.nativeEvent;
    CallbacksManager.call(id, screenPoints);
  }

  private processScreenToWorldPointsReceived(
    event: NativeSyntheticEvent<{ id: string } & Point[]>
  ) {
    const { id, ...worldPoints } = event.nativeEvent;
    CallbacksManager.call(id, worldPoints);
  }

  private resolveImageUri(img: ImageSourcePropType) {
    return img ? resolveAssetSource(img).uri : '';
  }

  private getProps(): Partial<YamapNativeProps> & ViewProps {
    const props: any = {
      ...this.props,
      onRouteFound: this.processRoute,
      onCameraPositionReceived: this.processCameraPosition,
      onVisibleRegionReceived: this.processVisibleRegion,
      onWorldToScreenPointsReceived: this.processWorldToScreenPointsReceived,
      onScreenToWorldPointsReceived: this.processScreenToWorldPointsReceived,
      userLocationIcon: this.props.userLocationIcon
        ? this.resolveImageUri(this.props.userLocationIcon)
        : undefined,
    };

    processColorProps(props, 'clusterColor' as keyof YaMapProps);
    processColorProps(
      props,
      'userLocationAccuracyFillColor' as keyof YaMapProps
    );
    processColorProps(
      props,
      'userLocationAccuracyStrokeColor' as keyof YaMapProps
    );

    return props;
  }

  private handleLayout: ViewProps['onLayout'] = (e) => {
    const { width, height } = e.nativeEvent.layout || { width: 0, height: 0 };
    if (!this.hasLaidOut && width > 0 && height > 0) {
      this.hasLaidOut = true;
    }
    // Relay to user-provided onLayout if present
    if (typeof this.props.onLayout === 'function') {
      this.props.onLayout(e);
    }
  };

  render() {
    return (
      <YamapNativeComponent
        {...this.getProps()}
        onLayout={this.handleLayout}
        ref={this.mapRef}
      />
    );
  }
}
