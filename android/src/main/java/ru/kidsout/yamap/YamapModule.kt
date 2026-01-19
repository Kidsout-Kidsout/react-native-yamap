package ru.kidsout.yamap

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.yandex.mapkit.MapKitFactory
import com.yandex.runtime.i18n.I18nManagerFactory

@ReactModule(name = YamapModule.NAME)
class YamapModule(reactContext: ReactApplicationContext) :
  NativeYamapSpec(reactContext) {
  val originalLocale = I18nManagerFactory.getLocale()

  override fun getName(): String {
    return NAME
  }

  override fun init(apiKey: String?, promise: Promise?) {
    MapKitFactory.setApiKey(apiKey!!)
    promise?.resolve(null)
  }

  override fun getLocale(promise: Promise?) {
    val loc = I18nManagerFactory.getLocale()
    promise?.resolve(loc)
  }

  override fun setLocale(locale: String?, promise: Promise?) {
    I18nManagerFactory.setLocale(locale)
    promise?.resolve(null)
  }

  override fun resetLocale(promise: Promise?) {
    I18nManagerFactory.setLocale(originalLocale)
  }

  companion object {
    const val NAME = "Yamap"
  }
}
