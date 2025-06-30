package ru.vvdev.yamap.suggest

import android.content.Context
import androidx.annotation.Nullable
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import ru.vvdev.yamap.utils.Callback
import com.facebook.react.bridge.UiThreadUtil.runOnUiThread

class RNYandexSuggestModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  companion object {
    private const val ERR_NO_REQUEST_ARG = "YANDEX_SUGGEST_ERR_NO_REQUEST_ARG"
    private const val ERR_SUGGEST_FAILED = "YANDEX_SUGGEST_ERR_SUGGEST_FAILED"
  }

  @Nullable
  private var suggestClient: MapSuggestClient? = null
  private val argsHelper = YandexSuggestRNArgsHelper()

  override fun getName(): String {
    return "YamapSuggests"
  }

  @ReactMethod
  fun suggest(text: String?, promise: Promise) {
    if (text == null) {
      promise.reject(ERR_NO_REQUEST_ARG, "suggest request: text arg is not provided")
      return
    }

    runOnUiThread {
      getSuggestClient(reactApplicationContext).suggest(
        text,
        object : Callback<List<MapSuggestItem>> {
          override fun invoke(result: List<MapSuggestItem>) {
            promise.resolve(argsHelper.createSuggestsMapFrom(result))
          }
        },
        object : Callback<Throwable> {
          override fun invoke(e: Throwable) {
            promise.reject(ERR_SUGGEST_FAILED, "suggest request: ${e.message}")
          }
        }
      )
    }
  }

  @ReactMethod
  fun suggestWithOptions(text: String?, options: ReadableMap, promise: Promise) {
    if (text == null) {
      promise.reject(ERR_NO_REQUEST_ARG, "suggest request: text arg is not provided")
      return
    }

    runOnUiThread {
      getSuggestClient(reactApplicationContext).suggest(
        text, options,
        object : Callback<List<MapSuggestItem>> {
          override fun invoke(result: List<MapSuggestItem>) {
            promise.resolve(argsHelper.createSuggestsMapFrom(result))
          }
        },
        object : Callback<Throwable> {
          override fun invoke(e: Throwable) {
            promise.reject(ERR_SUGGEST_FAILED, "suggest request: ${e.message}")
          }
        }
      )
    }
  }

  @ReactMethod
  fun reset() {
    runOnUiThread {
      getSuggestClient(reactApplicationContext).resetSuggest()
    }
  }

  private fun getSuggestClient(context: Context): MapSuggestClient {
    if (suggestClient == null) {
      suggestClient = YandexMapSuggestClient(context)
    }
    return suggestClient!!
  }
}
