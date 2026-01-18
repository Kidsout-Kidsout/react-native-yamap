import { type FunctionComponent, type ReactElement } from 'react';
import { type Point } from '../interfaces';
import YamapMarker from '../specs/NativeYamapMarkerView';

export interface MarkerProps {
  center: Point;
  zIndex?: number;
  onPress?: () => void;
  text?: string;
  marker?: ReactElement;
}

export const Marker: FunctionComponent<MarkerProps> = ({
  marker,
  ...props
}) => {
  return (
    <YamapMarker {...props} lIndex={props.zIndex ?? 1}>
      {marker}
    </YamapMarker>
  );
};
