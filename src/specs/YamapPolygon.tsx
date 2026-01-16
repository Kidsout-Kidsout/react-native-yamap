import type { ViewProps, ColorValue } from 'react-native';
import { codegenNativeComponent } from 'react-native';
import type {
  DirectEventHandler,
  Int32,
  Double,
} from 'react-native/Libraries/Types/CodegenTypesNamespace';

type Point = { lat: Double; lon: Double };

export interface NativeProps extends ViewProps {
  fillColor?: ColorValue;
  strokeColor?: ColorValue;
  strokeWidth?: Double;
  zIndex?: Int32;
  onPress?: DirectEventHandler<{}>;
  points: ReadonlyArray<Point>;
  innerRings?: ReadonlyArray<ReadonlyArray<Point>>;
}

export default codegenNativeComponent<NativeProps>('YamapPolygon');
