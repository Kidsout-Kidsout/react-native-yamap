import React, { type FunctionComponent } from 'react';
import { type Point } from '../interfaces';

export interface CircleProps {
  fillColor?: string;
  strokeColor?: string;
  strokeWidth?: number;
  zIndex?: number;
  onPress?: () => void;
  center: Point;
  radius: number;
}

import YamapCircle from '../specs/NativeYamapCircleView';
import { processColor } from 'react-native';

export const Circle: FunctionComponent<CircleProps> = (props) => {
  return (
    <YamapCircle
      {...props}
      fillColor={processColor(props.fillColor) ?? undefined}
      strokeColor={processColor(props.strokeColor) ?? undefined}
      lIndex={props.zIndex ?? 1}
    />
  );
};
