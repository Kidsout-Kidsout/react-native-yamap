import { useMemo, type FunctionComponent } from 'react';
import { type Point } from '../interfaces';

import YamapPolygon from '../specs/NativeYamapPolygonView';
import { processColor } from 'react-native';
import { usePreventedCallback } from '../utils/preventedCallback';

export interface PolygonProps {
  points: Point[];

  fillColor?: string;
  strokeColor?: string;
  strokeWidth?: number;
  zIndex?: number;
  onPress?: () => void;
  innerRings?: Point[][];
}

export const Polygon: FunctionComponent<PolygonProps> = ({
  onPress,
  ...props
}) => {
  const handlePress = usePreventedCallback(onPress);

  const styling = useMemo(
    () => ({
      fillColor: processColor(props.fillColor) ?? undefined,
      strokeColor: processColor(props.strokeColor) ?? undefined,
      strokeWidth: props.strokeWidth,
    }),
    [props.fillColor, props.strokeColor, props.strokeWidth]
  );

  return (
    <YamapPolygon
      {...props}
      onPress={handlePress}
      styling={styling}
      lIndex={props.zIndex ?? 1}
    />
  );
};
