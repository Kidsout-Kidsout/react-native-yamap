package ru.vvdev.yamap

import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.runtime.i18n.I18nManagerFactory
import com.facebook.react.bridge.UiThreadUtil.runOnUiThread

class RNYamapModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  companion object {
    private const val REACT_CLASS = "yamap"
  }

  override fun getName(): String {
    return REACT_CLASS
  }

  override fun getConstants(): Map<String, Any> {
    return emptyMap()
  }

  @ReactMethod
  fun init(apiKey: String, promise: Promise) {
    runOnUiThread {
      var apiKeyException: Throwable? = null
      try {
        try {
          MapKitFactory.setApiKey(apiKey)
        } catch (exception: Throwable) {
          apiKeyException = exception
        }

        MapKitFactory.initialize(reactContext)
        MapKitFactory.getInstance().onStart()
        promise.resolve(null)
      } catch (exception: Exception) {
        apiKeyException?.let {
          promise.reject(it)
          return@runOnUiThread
        }
        promise.reject(exception)
      }
    }
  }

  @ReactMethod
  fun setLocale(locale: String, successCb: Callback, errorCb: Callback) {
    runOnUiThread {
      MapKitFactory.setLocale(locale)
      successCb.invoke()
    }
  }

  @ReactMethod
  fun getLocale(successCb: Callback, errorCb: Callback) {
    runOnUiThread {
      val locale = I18nManagerFactory.getLocale()
      successCb.invoke(locale)
    }
  }

  @ReactMethod
  fun resetLocale(successCb: Callback, errorCb: Callback) {
    runOnUiThread {
      I18nManagerFactory.setLocale(null)
      successCb.invoke()
    }
  }

  private fun emitDeviceEvent(eventName: String, eventData: WritableMap?) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(eventName, eventData)
  }
}
