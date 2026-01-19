import {
  codegenNativeComponent,
  type CodegenTypes,
  type ProcessedColorValue,
  type ViewProps,
} from 'react-native';

export interface NativeProps extends ViewProps {
  lIndex?: CodegenTypes.Int32;
  center: { lat: CodegenTypes.Double; lon: CodegenTypes.Double };
  radius: CodegenTypes.Double;
  styling: {
    fillColor?: ProcessedColorValue;
    strokeColor?: ProcessedColorValue;
    strokeWidth?: CodegenTypes.Double;
  };

  onPress?: CodegenTypes.BubblingEventHandler<{}>;
}

export default codegenNativeComponent<NativeProps>('YamapCircleView');
