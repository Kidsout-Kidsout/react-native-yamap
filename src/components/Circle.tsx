import React from 'react';
import { processColorProps } from '../utils';
import { type Point } from '../interfaces';

export interface CircleProps {
  fillColor?: string;
  strokeColor?: string;
  strokeWidth?: number;
  zIndex?: number;
  onPress?: () => void;
  center: Point;
  radius: number;
  children?: undefined;
}

import YamapCircle from '../specs/YamapCircle';

export class Circle extends React.Component<CircleProps> {
  static defaultProps = {};

  render() {
    const props = { ...this.props };

    processColorProps(props, 'fillColor' as keyof CircleProps);
    processColorProps(props, 'strokeColor' as keyof CircleProps);

    return <YamapCircle {...props} />;
  }
}
