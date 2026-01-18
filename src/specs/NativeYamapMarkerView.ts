import {
  codegenNativeComponent,
  type CodegenTypes,
  type ViewProps,
} from 'react-native';

export interface NativeProps extends ViewProps {
  text?: string;
  lIndex?: CodegenTypes.Int32;
  onPress?: CodegenTypes.BubblingEventHandler<{}>;
  center: { lat: CodegenTypes.Double; lon: CodegenTypes.Double };
}

export default codegenNativeComponent<NativeProps>('YamapMarkerView');
