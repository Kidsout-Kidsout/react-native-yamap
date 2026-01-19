import { type FunctionComponent, type ReactElement } from 'react';
import { type Point } from '../interfaces';
import YamapMarker from '../specs/NativeYamapMarkerView';
import type { CodegenTypes } from 'react-native';
import { usePreventedCallback } from '../utils/preventedCallback';

export interface MarkerProps {
  id: string;
  center: Point;
  zIndex?: number;
  onPress?: CodegenTypes.BubblingEventHandler<{ id: string }>;
  text?: string;
  marker?: ReactElement;
}

export const Marker: FunctionComponent<MarkerProps> = ({
  marker,
  onPress,
  ...props
}) => {
  const handlePress = usePreventedCallback(onPress);

  return (
    <YamapMarker {...props} lIndex={props.zIndex ?? 1} onPress={handlePress}>
      {marker}
    </YamapMarker>
  );
};
