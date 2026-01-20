package ru.kidsout.yamap

import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.yandex.mapkit.MapKitFactory
import com.yandex.runtime.i18n.I18nManagerFactory

@ReactModule(name = YamapModule.NAME)
class YamapModule(reactContext: ReactApplicationContext) :
  NativeYamapSpec(reactContext) {
  val originalLocale: String by lazy {
    I18nManagerFactory.getLocale()
  }

  override fun getName(): String {
    return NAME
  }

  override fun init(apiKey: String?, promise: Promise?) {
    Log.i("YamapModule", "sms1 INITED with key ${apiKey}")
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
    promise?.resolve(null)
  }

  companion object {
    const val NAME = "Yamap"
  }
}
