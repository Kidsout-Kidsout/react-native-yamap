import { useMemo, type FunctionComponent, type ReactElement } from 'react';
import { type Point } from '../interfaces';
import YamapMarker from '../specs/NativeYamapMarkerView';
import type { CodegenTypes } from 'react-native';
import { usePreventedCallback } from '../utils/preventedCallback';
import { processColor } from 'react-native';

export interface MarkerProps {
  id: string;
  center: Point;
  zIndex?: number;
  onPress?: CodegenTypes.BubblingEventHandler<{ id: string }>;
  text?: string;
  textSize?: number;
  textColor?: string;
  marker?: ReactElement;
}

export const Marker: FunctionComponent<MarkerProps> = ({
  marker,
  onPress,
  textColor,
  textSize,
  ...props
}) => {
  const handlePress = usePreventedCallback(onPress);

  const styling = useMemo(
    () => ({
      fontSize: textSize ?? 10,
      fontColor:
        processColor(textColor) ?? processColor('#000000') ?? undefined,
    }),
    [textColor, textSize]
  );

  return (
    <YamapMarker
      {...props}
      styling={styling}
      lIndex={props.zIndex ?? 1}
      onPress={handlePress}
    >
      {marker}
    </YamapMarker>
  );
};
