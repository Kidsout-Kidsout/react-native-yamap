package ru.kidsout.yamap

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.UiThreadUtil
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
    UiThreadUtil.runOnUiThread {
      try {
        MapKitFactory.setApiKey(apiKey!!)
        MapKitFactory.initialize(reactApplicationContext)
        MapKitFactory.getInstance().onStart()
        promise?.resolve(null)
      } catch (error: Throwable) {
        promise?.reject(ERR_INIT_FAILED, error)
      }
    }
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
    private const val ERR_INIT_FAILED = "YAMAP_ERR_INIT_FAILED"
    const val NAME = "Yamap"
  }
}
