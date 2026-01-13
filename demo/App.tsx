/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import { StatusBar, StyleSheet, useColorScheme, View } from 'react-native';
import YaMap, { Circle } from '@kidsout-kidsout/react-native-yamap';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { useEffect, useMemo, useState } from 'react';

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
  const point = useMemo(() => ({ lat: 55.7522, lon: 37.6156 }), []);

  useEffect(() => {
    YaMap.init('a45f8ca9-a1e9-42c4-8853-cdfb1709ff36').then(() => {
      setRegistered(true);
    });
  }, []);

  if (!registered) return null;

  return (
    <View style={styles.container}>
      <YaMap
        style={styles.container}
        mapType="raster"
        rotateGesturesEnabled={false}
        showUserPosition={false}
      >
        <Circle
          center={point}
          fillColor="red"
          radius={250}
          strokeColor="blue"
          strokeWidth={1.5}
        />
      </YaMap>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

export default App;
