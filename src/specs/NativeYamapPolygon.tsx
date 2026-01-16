import {
  codegenNativeComponent,
  type ViewProps,
  type CodegenTypes,
} from 'react-native';

export interface NativeProps extends ViewProps {
  fillColor?: string;
  strokeColor?: string;
  strokeWidth?: CodegenTypes.Double;
  zIndex?: CodegenTypes.Int32;
  onPress?: CodegenTypes.DirectEventHandler<{}>;
  points: { lat: CodegenTypes.Double; lon: CodegenTypes.Double }[];
  innerRings?: { lat: CodegenTypes.Double; lon: CodegenTypes.Double }[][];
}

export default codegenNativeComponent<NativeProps>('YamapPolygon');
