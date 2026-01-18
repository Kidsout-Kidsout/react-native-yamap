import {
  useImperativeHandle,
  type FunctionComponent,
  type PropsWithChildren,
  type Ref,
} from 'react';
import { type ViewProps } from 'react-native';
import { type MapType } from '../interfaces';
import YamapNativeComponent, {
  type NativeProps as YamapNativeProps,
} from '../specs/NativeYamapView';

export type YamapRef = {};

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
} & Pick<
    YamapNativeProps,
    'onCameraPositionChange' | 'onMapPress' | 'onMapLongPress' | 'onMapLoaded'
  >;

export const Yamap: FunctionComponent<YamapProps> = ({
  ref,
  nightMode = false,
  mapType = 'vector',
  scrollGesturesEnabled = true,
  zoomGesturesEnabled = true,
  maxFps = 60,
  ...props
}) => {
  useImperativeHandle(ref, () => ({}), []);

  return (
    <YamapNativeComponent
      nightMode={nightMode}
      mapType={mapType}
      scrollGesturesEnabled={scrollGesturesEnabled}
      zoomGesturesEnabled={zoomGesturesEnabled}
      maxFps={maxFps}
      {...props}
    />
  );
};
