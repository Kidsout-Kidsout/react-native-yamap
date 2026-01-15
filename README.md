# @kidsout-kidsout/react-native-yamap

Modern Yandex.Maps SDK bindings for React Native with New Architecture.

This library provides a typed React Native wrapper around the native Yandex MapKit SDK on iOS and Android, including a `Yamap` view, overlays (markers, circles, polygons, clusters), geocoding and search suggestions.

## Installation

Install the package from npm:

```bash
yarn add @kidsout-kidsout/react-native-yamap
# or
npm install @kidsout-kidsout/react-native-yamap
```

### iOS

1. Install CocoaPods dependencies in the example / your app:

	 ```bash
	 cd ios
	 pod install
	 ```

2. Make sure you have Yandex MapKit in your Podfile as required by the podspec (see the latest MapKit installation instructions from Yandex).

3. Rebuild the iOS app from Xcode or via CLI.

### Android

1. Ensure you use Kotlin and AndroidX (React Native 0.71+ default).
2. Add the Yandex MapKit maven repository and dependency according to the current Yandex MapKit documentation.
3. Rebuild the Android app with Gradle.

## Setup

### iOS: initialize Yandex MapKit in AppDelegate

In your iOS app, configure `YandexMapsMobile` once during application startup (for example in `AppDelegate.swift`):

```swift
import YandexMapsMobile

func application(
	_ application: UIApplication,
	didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
) -> Bool {
	let ymapsKey = "YOUR_YANDEX_MAPKIT_API_KEY"

	YMKMapKit.setLocale("ru_RU") // or your preferred locale
	YMKMapKit.setApiKey(ymapsKey)
	YMKMapKit.sharedInstance().onStart()

	// ... your existing React Native setup

	return true
}
```

You can use the example implementation in
`example/ios/KidsoutYamapExample/AppDelegate.swift` as a reference.

### JS: initialize YamapConfig

On the JavaScript side, call `YamapConfig.init` once before you render the map
components (for example in your root component):

```tsx
import { useEffect, useState } from 'react';
import { Text } from 'react-native';
import { YamapConfig } from '@kidsout-kidsout/react-native-yamap';

function App() {
	const [registered, setRegistered] = useState(false);

	useEffect(() => {
		YamapConfig.init('YOUR_YANDEX_MAPKIT_API_KEY').then(() => {
			setRegistered(true);
		});
	}, []);

	if (!registered) {
		return <Text>Loading...</Text>;
	}

	return (
		// your app content with <Yamap /> inside
	);
}
```

See the full example in `example/src/App.tsx`.

## Basic usage

```tsx
import React, { useRef } from 'react';
import { View, StyleSheet } from 'react-native';
import {
	Yamap,
	Marker,
	Circle,
	Polygon,
	Clusters,
	type YamapRef,
	YamapConfig,
} from '@kidsout-kidsout/react-native-yamap';

export const MapScreen = () => {
	const mapRef = useRef<YamapRef | null>(null);

	return (
		<View style={styles.container}>
			<Yamap
				ref={mapRef}
				style={StyleSheet.absoluteFill}
				nightMode={false}
				onMapLoaded={() => {
					// Example: move camera when map is ready
					mapRef.current?.setCenter({
						center: { lat: 55.751244, lon: 37.618423 },
						zoom: 14,
					});
				}}
			>
				<Marker
					id="moscow-center"
					center={{ lat: 55.751244, lon: 37.618423 }}
					text="Moscow"
				/>
			</Yamap>
		</View>
	);
};

const styles = StyleSheet.create({
	container: {
		flex: 1,
	},
});
```

## API Overview

### Components

- `Yamap` – main map view.
- `Marker` – point marker with optional text or custom React element.
- `Circle` – circle overlay.
- `Polygon` – polygon overlay.
- `Clusters` – clustering container for markers.

All components are exported from the root module:

```ts
import {
	Yamap,
	Marker,
	Circle,
	Polygon,
	Clusters,
} from '@kidsout-kidsout/react-native-yamap';
```

### Yamap

`Yamap` accepts standard `View` props plus:

- `nightMode?: boolean`
- `mapType?: 'vector' | 'raster' | ...` (see `MapType` in `interfaces`)
- `scrollGesturesEnabled?: boolean`
- `zoomGesturesEnabled?: boolean`
- `tiltGesturesEnabled?: boolean`
- `rotateGesturesEnabled?: boolean`
- `fastTapEnabled?: boolean`
- `maxFps?: number`
- `onCameraPositionChange?`
- `onMapPress?`
- `onMapLongPress?`
- `onMapLoaded?`

With a ref of type `YamapRef` you can control the camera:

- `setCenter({ center, zoom?, azimuth?, tilt?, offset?, animation? })`
- `setBounds({ rectangle, minZoom?, maxZoom?, offset?, animation? })`
- `setZoom({ zoom, offset?, animation? })`
- `fitPoints({ points, minZoom?, maxZoom?, offset?, animation? })`
- `getCameraPosition()`
- `getVisibleRegion()`

### Marker

```tsx
<Marker
	id="unique-id"
	center={{ lat, lon }}
	zIndex={1}
	text="Label"
	textSize={12}
	textColor="#000000"
	onPress={(e) => {}}
	marker={<CustomMarkerView />}
/>
```

> Note: marker children are rasterized by the native SDK; animated components inside the marker are not supported.

### Config & Native modules

- `YamapConfig` – low-level access to the underlying native module (e.g., for API keys and global config).
- `Geocode` – wrapper around Yandex geocoding.
- `Suggest` – wrapper for Yandex search suggestions.

Example suggest usage:

```ts
import { Suggest, SuggestTypes } from '@kidsout-kidsout/react-native-yamap';

const results = await Suggest.suggestWithCoords('Moscow', {
	suggestTypes: [SuggestTypes.YMKSuggestTypeGeo],
});
```

## Example app

This repository contains an example app under `example/`.

```bash
cd example
npm i --workspaces=false
npm run ios   # or yarn android
```

Use it as a reference for integrating the library into your own project.

## License

MIT © Kidsout

