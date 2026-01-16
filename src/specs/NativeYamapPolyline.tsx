import {
  codegenNativeComponent,
  type ViewProps,
  type CodegenTypes,
} from 'react-native';

export interface NativeProps extends ViewProps {
  strokeColor?: string;
  outlineColor?: string;
  strokeWidth?: CodegenTypes.Double;
  outlineWidth?: CodegenTypes.Double;
  dashLength?: CodegenTypes.Double;
  dashOffset?: CodegenTypes.Double;
  gapLength?: CodegenTypes.Double;
  zIndex?: CodegenTypes.Int32;
  onPress?: CodegenTypes.DirectEventHandler<{}>;
  points: { lat: CodegenTypes.Double; lon: CodegenTypes.Double }[];
}

export default codegenNativeComponent<NativeProps>('YamapPolyline');
