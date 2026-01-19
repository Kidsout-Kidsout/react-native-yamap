package ru.kidsout.yamap

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.module.annotations.ReactModule
import com.yandex.mapkit.MapKitFactory
import com.yandex.runtime.i18n.I18nManagerFactory

@ReactModule(name = YamapModule.NAME)
class YamapGeocodeModule(reactContext: ReactApplicationContext) :
  NativeYamapGeocodeSpec(reactContext) {
  val originalLocale = I18nManagerFactory.getLocale();

  override fun getName(): String {
    return NAME
  }

  override fun geocode(query: String?, promise: Promise?) {
    TODO("Not yet implemented")
  }

  override fun geocodePoint(coords: ReadableArray?, promise: Promise?) {
    TODO("Not yet implemented")
  }

  override fun geocodeUri(uri: String?, promise: Promise?) {
    TODO("Not yet implemented")
  }

  companion object {
    const val NAME = "YamapGeocode"
  }
}
