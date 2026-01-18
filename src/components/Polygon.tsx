import { type FunctionComponent } from 'react';
import { type Point } from '../interfaces';

import YamapPolygon from '../specs/NativeYamapPolygonView';
import { processColor } from 'react-native';

export interface PolygonProps {
  fillColor?: string;
  strokeColor?: string;
  strokeWidth?: number;
  zIndex?: number;
  onPress?: () => void;
  points: Point[];
  innerRings?: Point[][];
}

export const Polygon: FunctionComponent<PolygonProps> = (props) => {
  return (
    <YamapPolygon
      {...props}
      fillColor={processColor(props.fillColor) ?? undefined}
      strokeColor={processColor(props.strokeColor) ?? undefined}
      lIndex={props.zIndex ?? 1}
    />
  );
};
