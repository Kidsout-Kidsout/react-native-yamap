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
import { YaMap, Circle, Polygon } from '@kidsout-kidsout/react-native-yamap';
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
  const [overlay, overlayControl] = useBoolean('Overlay', false);
  const [clustered, clusteredControl] = useBoolean('Clustered', false);
  const [type, typeControl] = useListToggle('Marker', [
    'circle',
    'polygon',
    'marker',
  ]);
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
  ) : (
    <MapDemo
      ref={mapRef}
      handlePositionChange={handlePositionChange}
      type={type}
      key={renderCount}
      nightMode={nightMode}
      overlay={overlay}
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
          {overlayControl}
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
  type: 'circle' | 'polygon' | 'marker';
  handlePositionChange?: () => void;
  overlay: boolean;
  ref?: Ref<YaMap>;
}> = ({ nightMode, type, handlePositionChange, overlay, ref }) => {
  const localRef = useRef<YaMap>(null);
  const point = useMemo(() => ({ lat: 55.7522, lon: 37.6156 }), []);
  const rref = useMergedRefs<YaMap>(localRef, ref);

  useEffect(() => {
    sleep(500).then(() => {
      localRef.current?.setCenter(point, 14);
    });
  }, [point]);

  const inner =
    type === 'polygon' ? (
      <Polygon
        points={[
          { lat: point.lat - 0.8, lon: point.lon + 1 },
          { lat: point.lat + 1, lon: point.lon + 1.2 },
          { lat: point.lat + 0.8, lon: point.lon - 1 },
          { lat: point.lat - 1, lon: point.lon - 1.2 },
        ]}
        fillColor="#f43c098f"
        strokeColor="#095bf4d4"
        strokeWidth={2}
        onPress={() => {
          // eslint-disable-next-line no-alert
          alert('Object pressed');
        }}
      />
    ) : (
      <Circle
        center={point}
        radius={20000}
        fillColor="#f43c098f"
        strokeColor="#095bf4d4"
        strokeWidth={2}
        onPress={() => {
          // eslint-disable-next-line no-alert
          alert('Object pressed');
        }}
      />
    );

  const overlayElement = overlay ? (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text
        // eslint-disable-next-line react-native/no-inline-styles
        style={{
          backgroundColor: 'rgba(0,0,0,0.2)',
          color: 'black',
          textAlign: 'center',
          pointerEvents: 'none',
          padding: 10,
          margin: 20,
        }}
      >
        Custom overlay
      </Text>
    </View>
  ) : null;

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
      {overlayElement}
    </YaMap>
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
    flexWrap: 'wrap',
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
