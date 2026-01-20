package ru.kidsout.yamap

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.module.annotations.ReactModule
import com.yandex.mapkit.geometry.Point
import ru.kidsout.yamap.geocode.ArgsHelper
import ru.kidsout.yamap.geocode.MapGeocodeClient
import ru.kidsout.yamap.geocode.MapGeocodeItem

@ReactModule(name = YamapModule.NAME)
class YamapGeocodeModule(reactContext: ReactApplicationContext) :
  NativeYamapGeocodeSpec(reactContext) {
  private val argsHelper = ArgsHelper()
  private var geocodeClient: MapGeocodeClient = MapGeocodeClient()

  override fun getName(): String {
    return NAME
  }

  override fun geocode(query: String, promise: Promise) {
    runSafe(promise) {
      geocodeClient.geocode(
        query,
        createResolveCallback(promise),
        createRejectCallback(promise)
      )
    }
  }

  override fun geocodeUri(uri: String, promise: Promise) {
    runSafe(promise) {
      geocodeClient.geocodeUri(
        uri,
        createResolveCallback(promise),
        createRejectCallback(promise)
      )
    }
  }

  override fun geocodePoint(coords: ReadableArray, promise: Promise) {
    if (coords.size() < 2) {
      promise.reject(ERR_NO_REQUEST_ARG, "geocodePoint request: coordinates are not provided")
      return
    }

    runSafe(promise) {
      val point = Point(coords.getDouble(0), coords.getDouble(1))
      geocodeClient.geocodePoint(
        point,
        createResolveCallback(promise),
        createRejectCallback(promise)
      )
    }
  }

  private fun runSafe(promise: Promise, block: () -> Unit) {
    try {
      reactApplicationContext.currentActivity!!.runOnUiThread {
        try {
          block()
        } catch (error: Exception) {
          promise.reject(ERR_GEOCODE_FAILED, error)
        }
      }
    } catch (error: Exception) {
      promise.reject(ERR_GEOCODE_FAILED, error)
    }
  }

  private fun createResolveCallback(promise: Promise): (MapGeocodeItem) -> Unit {
    return { arg ->
      try {
        promise.resolve(argsHelper.createResultItemFrom(arg))
      } catch (error: Exception) {
        promise.reject(ERR_GEOCODE_FAILED, error)
      }
    }
  }

  private fun createRejectCallback(promise: Promise): (Throwable) -> Unit {
    return { err ->
      promise.reject(ERR_GEOCODE_FAILED, err)
    }
  }

  companion object {
    private const val ERR_NO_REQUEST_ARG = "YANDEX_GEOCODE_ERR_NO_REQUEST_ARG"
    private const val ERR_GEOCODE_FAILED = "YANDEX_GEOCODE_ERR_GEOCODE_FAILED"
    const val NAME = "YamapGeocode"
  }
}
