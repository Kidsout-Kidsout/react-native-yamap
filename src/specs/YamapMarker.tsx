import type { HostComponent, ViewProps } from 'react-native';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';
import codegenNativeCommands from 'react-native/Libraries/Utilities/codegenNativeCommands';
import type { DirectEventHandler } from 'react-native/Libraries/Types/CodegenTypes';
import type { Int32, Double } from 'react-native/Libraries/Types/CodegenTypes';
import type { Ref } from 'react';

interface Point {
  lat: Double;
  lon: Double;
}

export interface YamapMarkerNativeProps extends ViewProps {
  zIndex?: Int32;
  scale?: Double;
  onPress?: DirectEventHandler<{}>;
  point: Point;
  source?: string; // resolved URI
  anchor?: { x: Double; y: Double };
  visible?: boolean;
}

interface NativeCommands {
  animatedMoveTo: (
    ref: Ref<HostComponent<YamapMarkerNativeProps>>,
    latitude: Double,
    longitude: Double,
    duration: Double
  ) => void;
  animatedRotateTo: (
    ref: Ref<HostComponent<YamapMarkerNativeProps>>,
    angle: Double,
    duration: Double
  ) => void;
}

export const Commands = codegenNativeCommands<NativeCommands>({
  supportedCommands: ['animatedMoveTo', 'animatedRotateTo'],
});

export default codegenNativeComponent<YamapMarkerNativeProps>('YamapMarker');
