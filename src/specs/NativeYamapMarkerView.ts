import {
  codegenNativeComponent,
  type CodegenTypes,
  type ProcessedColorValue,
  type ViewProps,
} from 'react-native';

export interface NativeProps extends ViewProps {
  id: string;
  text?: string;
  styling: {
    fontSize?: CodegenTypes.Double;
    fontColor?: ProcessedColorValue;
  };
  lIndex?: CodegenTypes.Int32;
  onPress?: CodegenTypes.BubblingEventHandler<{ id: string }>;
  center: { lat: CodegenTypes.Double; lon: CodegenTypes.Double };
}

export default codegenNativeComponent<NativeProps>('YamapMarkerView');
