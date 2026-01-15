import type { ViewProps, ColorValue } from 'react-native';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';
import type {
  DirectEventHandler,
  Int32,
  Double,
} from 'react-native/Libraries/Types/CodegenTypes';

type Point = { lat: Double; lon: Double };

export interface NativeProps extends ViewProps {
  fillColor?: ColorValue;
  strokeColor?: ColorValue;
  strokeWidth?: Double;
  zIndex?: Int32;
  onPress?: DirectEventHandler<{}>;
  center: Point;
  radius: Double;
}

export default codegenNativeComponent<NativeProps>('YamapCircle');
