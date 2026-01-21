import {
  useCallback,
  useImperativeHandle,
  useRef,
  useState,
  type FunctionComponent,
  type PropsWithChildren,
  type Ref,
} from 'react';
import { type HostInstance, type ViewProps } from 'react-native';
import {
  type Animation,
  type CameraPosition,
  type MapType,
  type Point,
  type VisibleRegion,
} from '../interfaces';
import YamapNativeComponent, {
  type CameraMoveNativeEvent,
  type CameraPositionEvent,
  type CameraPositionReceivedEvent,
  type MapLoadedEvent,
  type MapPointEvent,
  type NativeProps,
  type VisibleRegionReceivedEvent,
  Commands,
} from '../specs/NativeYamapView';
import { CallbacksManager } from '../utils/CallbacksManager';

export type YamapRef = {
  setCenter: (p: {
    center: Point;
    zoom?: number;
    azimuth?: number;
    tilt?: number;
    offset?: number;
    animation?: Animation;
  }) => Promise<{ completed: boolean }>;
  setBounds: (p: {
    rectangle: { bottomLeft: Point; topRight: Point };
    minZoom?: number;
    maxZoom?: number;
    offset?: number;
    animation?: Animation;
  }) => Promise<{ completed: boolean }>;
  setZoom: (p: {
    zoom: number;
    offset?: number;
    animation?: Animation;
  }) => Promise<{ completed: boolean }>;
  fitPoints: (p: {
    points: Point[];
    minZoom?: number;
    maxZoom?: number;
    offset?: number;
    animation?: Animation;
  }) => Promise<{ completed: boolean }>;
  getCameraPosition: () => Promise<CameraPosition>;
  getVisibleRegion: () => Promise<VisibleRegion>;
};

export type YamapPropsOnCameraPositionChangeResult = CameraPositionEvent;
export type YamapPropsOnCameraPositionChangeCallback = Exclude<
  NativeProps['onCameraPositionChange'],
  undefined
>;

export type YamapPropsOnMapPressResult = MapPointEvent;
export type YamapPropsOnMapPressCallback = Exclude<
  NativeProps['onMapPress'],
  undefined
>;

export type YamapPropsOnMapLongPressResult = MapPointEvent;
export type YamapPropsOnMapLongPressCallback = Exclude<
  NativeProps['onMapLongPress'],
  undefined
>;

export type YamapPropsOnMapLoadedResult = MapLoadedEvent;
export type YamapPropsOnMapLoadedCallback = Exclude<
  NativeProps['onMapLoaded'],
  undefined
>;

export type YamapProps = PropsWithChildren<ViewProps> & {
  nightMode?: boolean;
  mapType?: MapType;
  scrollGesturesEnabled?: boolean;
  zoomGesturesEnabled?: boolean;
  tiltGesturesEnabled?: boolean;
  rotateGesturesEnabled?: boolean;
  fastTapEnabled?: boolean;
  maxFps?: number;
  ref?: Ref<YamapRef>;
  onCameraPositionChange?: YamapPropsOnCameraPositionChangeCallback;
  onMapPress?: YamapPropsOnMapPressCallback;
  onMapLongPress?: YamapPropsOnMapLongPressCallback;
  onMapLoaded?: YamapPropsOnMapLoadedCallback;
};

export const Yamap: FunctionComponent<YamapProps> = ({
  ref,
  nightMode = false,
  mapType = 'vector',
  scrollGesturesEnabled = true,
  zoomGesturesEnabled = true,
  maxFps = 60,
  ...props
}) => {
  const nativeRef = useRef<HostInstance>(null);

  const getRef = useCallback(() => {
    const el = nativeRef.current;
    if (!el) throw new Error('Yamap native ref is null');
    return el;
  }, []);

  const [callbackManager] = useState(() => new CallbacksManager());

  useImperativeHandle(ref, () => {
    const setBounds: YamapRef['setBounds'] = async (p) => {
      const pr = callbackManager.register<CameraMoveNativeEvent>();
      Commands.commandSetBounds(
        getRef(),
        pr.id,
        p.rectangle.bottomLeft.lat,
        p.rectangle.bottomLeft.lon,
        p.rectangle.topRight.lat,
        p.rectangle.topRight.lon,
        p.minZoom ?? 0,
        p.maxZoom ?? 0,
        p.offset ?? 0,
        p.animation?.type === 'smooth' ? 1 : 0,
        p.animation?.duration ?? 0
      );
      return pr.promise;
    };

    const fitPoints: YamapRef['fitPoints'] = async ({ points, ...p }) => {
      if (!points.length) return { completed: true };
      let minLat = null;
      let minLon = null;
      let maxLat = null;
      let maxLon = null;
      for (const point of points) {
        if (minLat == null || point.lat < minLat) minLat = point.lat;
        if (minLon == null || point.lon < minLon) minLon = point.lon;
        if (maxLat == null || point.lat > maxLat) maxLat = point.lat;
        if (maxLon == null || point.lon > maxLon) maxLon = point.lon;
      }
      return setBounds({
        rectangle: {
          bottomLeft: { lat: minLat!, lon: minLon! },
          topRight: { lat: maxLat!, lon: maxLon! },
        },
        ...p,
      });
    };

    return {
      setCenter: async (p) => {
        const pr = callbackManager.register<CameraMoveNativeEvent>();
        Commands.commandSetCenter(
          getRef(),
          pr.id,
          p.center.lat,
          p.center.lon,
          p.zoom ?? 0,
          p.azimuth ?? 0,
          p.tilt ?? 0,
          p.offset ?? 0,
          p.animation?.type === 'smooth' ? 1 : 0,
          p.animation?.duration ?? 0
        );
        return pr.promise;
      },
      setBounds,
      fitPoints,
      setZoom: async (p) => {
        const pr = callbackManager.register<CameraMoveNativeEvent>();
        Commands.commandSetZoom(
          getRef(),
          pr.id,
          p.zoom,
          p.offset ?? 0,
          p.animation?.type === 'smooth' ? 1 : 0,
          p.animation?.duration ?? 0
        );
        return pr.promise;
      },
      getCameraPosition: async () => {
        const pr = callbackManager.register<CameraPositionReceivedEvent>();
        Commands.commandGetCameraPosition(getRef(), pr.id);
        const pos = await pr.promise;
        return pos;
      },
      getVisibleRegion: async () => {
        const pr = callbackManager.register<VisibleRegionReceivedEvent>();
        Commands.commandGetVisibleRegion(getRef(), pr.id);
        return pr.promise;
      },
    };
  }, [callbackManager, getRef]);

  return (
    <YamapNativeComponent
      ref={nativeRef}
      {...props}
      nightMode={nightMode}
      mapType={mapType}
      scrollGesturesEnabled={scrollGesturesEnabled}
      zoomGesturesEnabled={zoomGesturesEnabled}
      maxFps={maxFps}
      onCommandSetCenterReceived={callbackManager.createListener(
        'onCommandSetCenterReceived'
      )}
      onCommandSetBoundsReceived={callbackManager.createListener(
        'onCommandSetBoundsReceived'
      )}
      onCommandSetZoomReceived={callbackManager.createListener(
        'onCommandSetZoomReceived'
      )}
      onCommandGetCameraPositionReceived={callbackManager.createListener(
        'onCommandGetCameraPositionReceived'
      )}
      onCommandGetVisibleRegionReceived={callbackManager.createListener(
        'onCommandGetVisibleRegionReceived'
      )}
    />
  );
};
