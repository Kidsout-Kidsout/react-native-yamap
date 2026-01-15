/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import { StatusBar, Text, useColorScheme } from 'react-native';
import { YamapConfig } from '@kidsout-kidsout/react-native-yamap';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { useEffect, useState } from 'react';
import { AppContent } from './Demo';

function App() {
  const isDarkMode = useColorScheme() === 'dark';
  const [registered, setRegistered] = useState(false);

  useEffect(() => {
    YamapConfig.init('a45f8ca9-a1e9-42c4-8853-cdfb1709ff36').then(() => {
      setRegistered(true);
    });
  }, []);

  return (
    <SafeAreaProvider>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      {registered ? <AppContent /> : <Text>Loading...</Text>}
    </SafeAreaProvider>
  );
}

export default App;
