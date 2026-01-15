import {
  codegenNativeComponent,
  type CodegenTypes,
  type ProcessedColorValue,
  type ViewProps,
} from 'react-native';

type Point = { lat: CodegenTypes.Double; lon: CodegenTypes.Double };

export interface NativeProps extends ViewProps {
  lIndex?: CodegenTypes.Int32;
  points: ReadonlyArray<Point>;
  innerRings?: ReadonlyArray<ReadonlyArray<Point>>;
  styling: {
    fillColor?: ProcessedColorValue;
    strokeColor?: ProcessedColorValue;
    strokeWidth?: CodegenTypes.Double;
  };
  onPress?: CodegenTypes.BubblingEventHandler<{}>;
}

export default codegenNativeComponent<NativeProps>('YamapPolygonView');
