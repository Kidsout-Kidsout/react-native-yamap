import {
  codegenNativeComponent,
  type CodegenTypes,
  type ProcessedColorValue,
  type ViewProps,
} from 'react-native';

export interface NativeProps extends ViewProps {
  fillColor?: ProcessedColorValue;
  strokeColor?: ProcessedColorValue;
  strokeWidth?: CodegenTypes.Double;
  lIndex?: CodegenTypes.Int32;
  onPress?: CodegenTypes.BubblingEventHandler<{}>;
  center: { lat: CodegenTypes.Double; lon: CodegenTypes.Double };
  radius: CodegenTypes.Double;
}

export default codegenNativeComponent<NativeProps>('YamapCircleView');
