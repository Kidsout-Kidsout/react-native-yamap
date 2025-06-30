package ru.vvdev.yamap

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import ru.vvdev.yamap.yageocode.RNYandexGeocodeModule
import ru.vvdev.yamap.suggest.RNYandexSuggestModule

class RNYamapPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return listOf(
      RNYamapModule(reactContext),
      RNYandexGeocodeModule(reactContext),
      RNYandexSuggestModule(reactContext)
    )
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return listOf(
      YamapViewManager(),
      YamapPolygonManager(),
      YamapPolylineManager(),
      YamapMarkerManager(),
      YamapCircleManager()
    )
  }
}
