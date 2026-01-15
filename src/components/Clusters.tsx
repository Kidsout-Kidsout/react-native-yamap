import {
  useEffect,
  useMemo,
  useState,
  type FunctionComponent,
  type ReactNode,
} from 'react';
import YamapClusters from '../specs/NativeYamapClustersView';
import type { CodegenTypes } from 'react-native';
import { usePreventedCallback } from '../utils/preventedCallback';
import { processColor } from 'react-native';

export interface ClustersProps {
  radius?: number;
  minZoom?: number;
  clusterStyle?: {
    fontSize?: CodegenTypes.Double;
    fontColor?: string;
    fillColor?: string;
    strokeColor?: string;
    strokeWidth?: CodegenTypes.Double;
    padding?: CodegenTypes.Double;
  };
  onPress?: CodegenTypes.BubblingEventHandler<{ ids: string[] }>;
  children?: ReactNode;
}

export const Clusters: FunctionComponent<ClustersProps> = ({
  radius = 100,
  minZoom = 15,
  clusterStyle,
  onPress,
  children,
  ...props
}) => {
  const [ch, setChildren] = useState<ReactNode>(undefined);
  const handlePress = usePreventedCallback(onPress);

  const stylehash = JSON.stringify(clusterStyle);

  const clStyle = useMemo(
    () => ({
      ...defaultClusterStyle,
      ...clusterStyle,
      fontColor:
        processColor(
          clusterStyle?.fontColor ?? defaultClusterStyle.fontColor
        ) ?? undefined,
      fillColor:
        processColor(
          clusterStyle?.fillColor ?? defaultClusterStyle.fillColor
        ) ?? undefined,
      strokeColor:
        processColor(
          clusterStyle?.strokeColor ?? defaultClusterStyle.strokeColor
        ) ?? undefined,
    }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [stylehash]
  );

  useEffect(() => {
    setTimeout(() => {
      setChildren(children);
    }, 10);
  }, [children]);

  return (
    <YamapClusters
      {...props}
      radius={radius}
      minZoom={minZoom}
      clusterStyle={clStyle}
      onPress={handlePress}
    >
      {ch}
    </YamapClusters>
  );
};

const defaultClusterStyle = {
  fillColor: 'white',
  strokeColor: '#e54f1de8',
  fontColor: '#000',
  fontSize: 14,
  strokeWidth: 2,
  padding: 5,
};
