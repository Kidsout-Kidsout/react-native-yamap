import { useMemo, type FunctionComponent } from 'react';
import { type Point } from '../interfaces';
import YamapCircle from '../specs/NativeYamapCircleView';
import { processColor } from 'react-native';
import { usePreventedCallback } from '../utils/preventedCallback';

export type CirclePropsOnPressCallback = () => void;

export interface CircleProps {
  fillColor?: string;
  strokeColor?: string;
  strokeWidth?: number;
  zIndex?: number;
  onPress?: CirclePropsOnPressCallback;
  center: Point;
  radius: number;
}

export const Circle: FunctionComponent<CircleProps> = ({
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
    <YamapCircle
      {...props}
      onPress={handlePress}
      styling={styling}
      lIndex={props.zIndex ?? 1}
    />
  );
};
