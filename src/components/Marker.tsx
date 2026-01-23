import { useMemo, type FunctionComponent, type ReactElement } from 'react';
import { type Point } from '../interfaces';
import YamapMarker from '../specs/NativeYamapMarkerView';
import type { CodegenTypes } from 'react-native';
import { usePreventedCallback } from '../utils/preventedCallback';
import { processColor } from 'react-native';
import { MarkerImage } from '../MarkerImage';

export type MarkerPropsOnPressCallback = CodegenTypes.BubblingEventHandler<{
  id: string;
}>;

export interface MarkerProps {
  id: string;
  center: Point;
  zIndex?: number;
  onPress?: MarkerPropsOnPressCallback;
  text?: string;
  textSize?: number;
  textColor?: string;
  /**
   * Note that marker view will get rasterized upon placing on map.
   * This means that you won't be able to use animated components.
   * To update marker view, you will need to re-render Marker component with a new 'marker' prop or assign a different key.
   */
  marker?: ReactElement | MarkerImage;
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

  const children = marker instanceof MarkerImage ? undefined : marker;

  return (
    <YamapMarker
      {...props}
      styling={styling}
      lIndex={props.zIndex ?? 1}
      onPress={handlePress}
      image={marker instanceof MarkerImage ? marker.src : undefined}
    >
      {children}
    </YamapMarker>
  );
};
