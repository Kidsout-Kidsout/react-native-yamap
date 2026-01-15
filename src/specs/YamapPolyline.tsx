import type { ViewProps, ColorValue } from "react-native";
import codegenNativeComponent from "react-native/Libraries/Utilities/codegenNativeComponent";
import type {
  DirectEventHandler,
  Int32,
  Double,
} from "react-native/Libraries/Types/CodegenTypes";

type Point = { lat: Double; lon: Double };

export interface NativeProps extends ViewProps {
  strokeColor?: ColorValue;
  outlineColor?: ColorValue;
  strokeWidth?: Double;
  outlineWidth?: Double;
  dashLength?: Double;
  dashOffset?: Double;
  gapLength?: Double;
  zIndex?: Int32;
  onPress?: DirectEventHandler<{}>;
  points: ReadonlyArray<Point>;
}

export default codegenNativeComponent<NativeProps>("YamapPolyline");
