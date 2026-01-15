import {
  codegenNativeComponent,
  type CodegenTypes,
  type ProcessedColorValue,
  type ViewProps,
} from 'react-native';

export interface NativeProps extends ViewProps {
  radius?: CodegenTypes.Double;
  minZoom?: CodegenTypes.Double;
  clusterStyle: {
    fontSize?: CodegenTypes.Double;
    fontColor?: ProcessedColorValue;
    fillColor?: ProcessedColorValue;
    strokeColor?: ProcessedColorValue;
    strokeWidth?: CodegenTypes.Double;
    padding?: CodegenTypes.Double;
  };
  onPress?: CodegenTypes.BubblingEventHandler<{ ids: string[] }>;
}

export default codegenNativeComponent<NativeProps>('YamapClustersView');
