import * as React from 'react';
import {
  type HostComponent,
  type ViewProps,
  codegenNativeComponent,
  codegenNativeCommands,
  type CodegenTypes,
} from 'react-native';

export interface YamapMarkerNativeProps extends ViewProps {
  zIndex?: CodegenTypes.Int32;
  scale?: CodegenTypes.Double;
  onPress?: CodegenTypes.DirectEventHandler<{}>;
  point: { lat: CodegenTypes.Double; lon: CodegenTypes.Double };
  source?: string;
  anchor?: { x: CodegenTypes.Double; y: CodegenTypes.Double };
  visible?: boolean;
}

interface NativeCommands {
  animatedMoveTo: (
    ref: React.ElementRef<HostComponent<YamapMarkerNativeProps>>,
    latitude: CodegenTypes.Double,
    longitude: CodegenTypes.Double,
    duration: CodegenTypes.Double
  ) => void;
  animatedRotateTo: (
    ref: React.ElementRef<HostComponent<YamapMarkerNativeProps>>,
    angle: CodegenTypes.Double,
    duration: CodegenTypes.Double
  ) => void;
}

export const Commands = codegenNativeCommands<NativeCommands>({
  supportedCommands: ['animatedMoveTo', 'animatedRotateTo'],
});

export default codegenNativeComponent<YamapMarkerNativeProps>('YamapMarker');
