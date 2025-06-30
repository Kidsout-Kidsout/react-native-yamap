package ru.vvdev.yamap.yageocode

import android.content.Context
import androidx.annotation.Nullable
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.UiThreadUtil.runOnUiThread
import com.yandex.mapkit.geometry.Point
import ru.vvdev.yamap.utils.Callback

class RNYandexGeocodeModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  companion object {
    private const val ERR_NO_REQUEST_ARG = "YANDEX_SUGGEST_ERR_NO_REQUEST_ARG"
    private const val ERR_GEOCODE_FAILED = "YANDEX_GEOCODE_ERR_GEOCODE_FAILED"
  }

  @Nullable
  private var geocodeClient: MapGeocodeClient? = null
  private val argsHelper = YandexGeocodeRNArgsHelper()

  override fun getName(): String {
    return "YamapGeocode"
  }

  @ReactMethod
  fun geocode(text: String?, promise: Promise) {
    if (text == null) {
      promise.reject(ERR_NO_REQUEST_ARG, "Suggest request: text arg is not provided")
      return
    }
    runOnUiThread {
      getGeocodeClient(reactApplicationContext).geocode(text,
        object : Callback<MapGeocodeItem> {
          override fun invoke(result: MapGeocodeItem) {
            try {
              promise.resolve(argsHelper.createResultItemFrom(result))
            } catch (e: Exception) {
              promise.reject(ERR_GEOCODE_FAILED, "Suggest request: ${e.message}")
            }
          }
        },
        object : Callback<Throwable> {
          override fun invoke(e: Throwable) {
            promise.reject(ERR_GEOCODE_FAILED, "Suggest request: ${e.message}")
          }
        })
    }
  }

  @ReactMethod
  fun geocodeUri(uri: String, promise: Promise) {
    runOnUiThread {
      getGeocodeClient(reactApplicationContext).geocodeUri(uri,
        object : Callback<MapGeocodeItem> {
          override fun invoke(result: MapGeocodeItem) {
            try {
              promise.resolve(argsHelper.createResultItemFrom(result))
            } catch (e: Exception) {
              promise.reject(ERR_GEOCODE_FAILED, "Suggest request: ${e.message}")
            }
          }
        },
        object : Callback<Throwable> {
          override fun invoke(e: Throwable) {
            promise.reject(ERR_GEOCODE_FAILED, "Suggest request: ${e.message}")
          }
        })
    }
  }

  @ReactMethod
  fun geocodePoint(point: ReadableArray?, promise: Promise) {
    if (point == null) {
      promise.reject(ERR_NO_REQUEST_ARG, "Suggest request: text arg is not provided")
      return
    }
    val ypoint = Point(point.getDouble(0), point.getDouble(1))
    runOnUiThread {
      getGeocodeClient(reactApplicationContext).geocodePoint(ypoint,
        object : Callback<MapGeocodeItem> {
          override fun invoke(result: MapGeocodeItem) {
            try {
              promise.resolve(argsHelper.createResultItemFrom(result))
            } catch (e: Exception) {
              promise.reject(ERR_GEOCODE_FAILED, "Suggest request: ${e.message}")
            }
          }
        },
        object : Callback<Throwable> {
          override fun invoke(e: Throwable) {
            promise.reject(ERR_GEOCODE_FAILED, "Suggest request: ${e.message}")
          }
        })
    }
  }

  private fun getGeocodeClient(context: Context): MapGeocodeClient {
    if (geocodeClient == null) {
      geocodeClient = YandexMapGeocodeClient(context)
    }
    return geocodeClient!!
  }
}
