const path = require('path');
const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');

/**
 * Metro configuration
 * https://reactnative.dev/docs/metro
 *
 * @type {import('@react-native/metro-config').MetroConfig}
 */
const config = {
  resolver: {
    unstable_enableSymlinks: true, // Enable this beta feature
    extraNodeModules: {
      '@kidsout-kidsout/react-native-yamap': path.join(__dirname, '/../'),
    },
  },
  watchFolders: [path.resolve(__dirname, '../')],
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
