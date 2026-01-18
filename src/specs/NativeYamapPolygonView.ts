import {
  codegenNativeComponent,
  type CodegenTypes,
  type ProcessedColorValue,
  type ViewProps,
} from 'react-native';

type Point = { lat: CodegenTypes.Double; lon: CodegenTypes.Double };

export interface NativeProps extends ViewProps {
  fillColor?: ProcessedColorValue;
  strokeColor?: ProcessedColorValue;
  strokeWidth?: CodegenTypes.Double;
  lIndex?: CodegenTypes.Int32;
  onPress?: CodegenTypes.BubblingEventHandler<{}>;
  points: ReadonlyArray<Point>;
  innerRings?: ReadonlyArray<ReadonlyArray<Point>>;
}

export default codegenNativeComponent<NativeProps>('YamapPolygonView');
