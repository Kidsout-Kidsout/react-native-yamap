/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import {
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';
import { YaMap, Circle } from '@kidsout-kidsout/react-native-yamap';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import {
  FunctionComponent,
  Ref,
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
} from 'react';
import { useMergedRefs, sleep, useBoolean, useListToggle } from './utils';
import { BUTTON_COLOR } from './constants';

function App() {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <SafeAreaProvider>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <AppContent />
    </SafeAreaProvider>
  );
}

function AppContent() {
  const [registered, setRegistered] = useState(false);
  const [renderCount, setRenderCount] = useState(0);
  const nightMode = useColorScheme() === 'dark';
  const [clustered, clusteredControl] = useBoolean('Clustered', false);
  const [type, typeControl] = useListToggle('Marker', ['marker', 'circle']);
  const mapRef = useRef<YaMap>(null);
  const [position, setPosition] = useState<string>('');

  const handlePositionChange = useCallback(() => {
    mapRef.current?.getCameraPosition((p) => {
      setPosition(
        `lat: ${p.point.lat.toFixed(5)}, lon: ${p.point.lon.toFixed(
          5
        )}, zoom: ${p.zoom.toFixed(2)}`
      );
    });
  }, []);

  useEffect(() => {
    YaMap.init('a45f8ca9-a1e9-42c4-8853-cdfb1709ff36').then(() => {
      setRegistered(true);
    });
  }, []);

  const map = !registered ? (
    <Text>Loading map...</Text>
  ) : clustered ? (
    <ClusterMapDemo
      ref={mapRef}
      handlePositionChange={handlePositionChange}
      type={type}
      key={renderCount}
      nightMode={nightMode}
    />
  ) : (
    <MapDemo
      ref={mapRef}
      handlePositionChange={handlePositionChange}
      type={type}
      key={renderCount}
      nightMode={nightMode}
    />
  );

  return (
    <View style={styles.container}>
      <SafeAreaView style={styles.content}>
        <View style={styles.row}>
          <Text>Yamap Demo</Text>
          <Text
            style={styles.button}
            onPress={() => setRenderCount((c) => c + 1)}
          >
            Rerender
          </Text>
          {clusteredControl}
          {typeControl}
        </View>
        {map}
        <Text>State: {position}</Text>
      </SafeAreaView>
    </View>
  );
}

const MapDemo: FunctionComponent<{
  nightMode: boolean;
  type: 'marker' | 'circle';
  handlePositionChange?: () => void;
  ref?: Ref<YaMap>;
}> = ({ nightMode, type, handlePositionChange, ref }) => {
  const localRef = useRef<YaMap>(null);
  const point = useMemo(() => ({ lat: 55.7522, lon: 37.6156 }), []);
  const rref = useMergedRefs<YaMap>(localRef, ref);

  useEffect(() => {
    sleep(500).then(() => {
      localRef.current?.setCenter(point, 14);
    });
  }, [point]);

  const inner = [
    <Circle
      key={1}
      center={point}
      radius={20000}
      fillColor="#f43c098f"
      strokeColor="#095bf4d4"
      strokeWidth={2}
      onPress={() => {
        // eslint-disable-next-line no-alert
        alert('Circle pressed');
      }}
    />,
  ];

  const map = (
    <YaMap
      ref={rref}
      onCameraPositionChange={handlePositionChange}
      style={styles.map}
      mapType="raster"
      nightMode={nightMode}
      rotateGesturesEnabled={false}
      showUserPosition={false}
    >
      {inner}
    </YaMap>
  );

  return <View style={styles.mapContainer}>{map}</View>;
};

const ClusterMapDemo: FunctionComponent<{
  nightMode: boolean;
  type: 'marker' | 'circle';
  handlePositionChange?: () => void;
  ref?: Ref<YaMap>;
}> = ({ nightMode, type, handlePositionChange, ref }) => {
  const localRef = useRef<YaMap>(null);
  const point = useMemo(() => ({ lat: 55.7522, lon: 37.6156 }), []);
  const points = useMemo(() => {
    return new Array(100).fill(0).map((_, index) => ({
      index,
      lat: 55.75 + (Math.random() - 0.5) * 0.1,
      lon: 37.6 + (Math.random() - 0.5) * 0.1,
    }));
  }, []);
  const rref = useMergedRefs<YaMap>(localRef, ref);

  useEffect(() => {
    sleep(500).then(() => {
      localRef.current?.setCenter(point, 14);
    });
  }, [point]);

  const map = (
    <YaMap
      withClusters={true}
      ref={rref}
      onCameraPositionChange={handlePositionChange}
      style={styles.map}
      mapType="raster"
      nightMode={nightMode}
      rotateGesturesEnabled={false}
      showUserPosition={false}
    />
  );

  return <View style={styles.mapContainer}>{map}</View>;
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  content: {
    padding: 10,
    flex: 1,
    gap: 10,
  },
  mapContainer: {
    flexGrow: 1,
    flexShrink: 1,
    width: '100%',
  },
  map: {
    height: '100%',
    width: '100%',
    backgroundColor: 'lightgray',
  },
  button: {
    color: BUTTON_COLOR,
  },
});

export default App;
