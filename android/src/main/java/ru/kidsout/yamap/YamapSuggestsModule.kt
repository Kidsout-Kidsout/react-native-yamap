package ru.kidsout.yamap.suggest

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.UiThreadUtil.runOnUiThread
import com.facebook.react.module.annotations.ReactModule
import ru.kidsout.yamap.NativeYamapSuggestsSpec

@ReactModule(name = YamapSuggestsModule.NAME)
class YamapSuggestsModule(reactContext: ReactApplicationContext) :
  NativeYamapSuggestsSpec(reactContext) {

  private var suggestClient = SuggestClient()
  private val argsHelper = ArgsHelper()

  override fun getName(): String = NAME

  override fun suggest(query: String, promise: Promise) {
    if (query.isBlank()) {
      promise.reject(ERR_NO_REQUEST_ARG, "suggest request: text arg is not provided")
      return
    }

    runSafe(promise) {
      suggestClient.suggest(
        query,
        createResolveCallback(promise),
        createRejectCallback(promise)
      )
    }
  }

  override fun suggestWithOptions(query: String, options: ReadableMap, promise: Promise) {
    if (query.isBlank()) {
      promise.reject(ERR_NO_REQUEST_ARG, "suggest request: text arg is not provided")
      return
    }

    runSafe(promise) {
      suggestClient.suggest(
        query,
        options,
        createResolveCallback(promise),
        createRejectCallback(promise)
      )
    }
  }

  override fun reset(promise: Promise) {
    runSafe(promise) {
      suggestClient.resetSuggest()
      promise.resolve(null)
    }
  }

  private fun runSafe(promise: Promise, block: () -> Unit) {
    runOnUiThread {
      try {
        block()
      } catch (error: Exception) {
        promise.reject(ERR_SUGGEST_FAILED, error)
      }
    }
  }

  private fun createResolveCallback(promise: Promise): (List<MapSuggestItem>) -> Unit {
    return { result ->
      try {
        promise.resolve(argsHelper.createSuggestsMapFrom(result))
      } catch (error: Exception) {
        promise.reject(ERR_SUGGEST_FAILED, error)
      }
    }
  }

  private fun createRejectCallback(promise: Promise): (Throwable) -> Unit {
    return { e ->
      promise.reject(ERR_SUGGEST_FAILED, e)
    }
  }

  companion object {
    private const val ERR_NO_REQUEST_ARG = "YANDEX_SUGGEST_ERR_NO_REQUEST_ARG"
    private const val ERR_SUGGEST_FAILED = "YANDEX_SUGGEST_ERR_SUGGEST_FAILED"
    const val NAME = "YamapSuggests"
  }
}
