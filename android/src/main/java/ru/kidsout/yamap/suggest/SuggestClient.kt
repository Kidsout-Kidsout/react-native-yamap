package ru.kidsout.yamap.suggest

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error

class SuggestClient() {
  private val searchManager: SearchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
  private val suggestOptions = SuggestOptions()
  private var suggestSession: SuggestSession? = null

  /**
   * Для Яндекса нужно указать географическую область поиска. В дефолтном варианте мы не знаем какие
   * границы для каждого конкретного города, поэтому поиск осуществляется по всему миру.
   * Для `BoundingBox` нужно указать ширину и долготу для юго-западной точки и северо-восточной
   * в градусах. Получается, что координаты самой юго-западной точки, это
   * ширина = -90, долгота = -180, а самой северо-восточной - ширина = 90, долгота = 180
   */
  private val defaultGeometry = BoundingBox(Point(-90.0, -180.0), Point(90.0, 180.0))

  init {
    suggestOptions.suggestTypes = SearchType.GEO.value
  }

  private fun suggestHandler(
    text: String,
    options: SuggestOptions,
    onSuccess: (List<MapSuggestItem>) -> Unit,
    onError: (Throwable) -> Unit
  ) {
    if (suggestSession == null) {
      suggestSession = searchManager.createSuggestSession()
    }

    suggestSession?.suggest(
      text,
      defaultGeometry,
      options,
      object : SuggestSession.SuggestListener {
        override fun onResponse(suggestResponse: SuggestResponse) {
          val result: MutableList<MapSuggestItem> = ArrayList(suggestResponse.items.size)
          for (i in suggestResponse.items.indices) {
              val rawSuggest = suggestResponse.items[i]
              val suggest = MapSuggestItem()
              suggest.searchText = rawSuggest.searchText
              suggest.title = rawSuggest.title.text
              if (rawSuggest.subtitle != null) {
                  suggest.subtitle = rawSuggest.subtitle!!.text
              }
              suggest.uri = rawSuggest.uri
              result.add(suggest)
          }
          onSuccess.invoke(result)
        }

        override fun onError(error: Error) {
          onError.invoke(IllegalStateException("suggest error: $error"))
        }
      }
    )
  }

  fun suggest(
    text: String,
    onSuccess: (List<MapSuggestItem>) -> Unit,
    onError: (Throwable) -> Unit
  ) {
    suggestHandler(text, suggestOptions, onSuccess, onError)
  }

  fun suggest(
    text: String,
    options: ReadableMap,
    onSuccess: (List<MapSuggestItem>) -> Unit,
    onError: (Throwable) -> Unit
  ) {
    val userPositionKey = "userPosition"
    val lonKey = "lon"
    val latKey = "lat"
    val suggestWordsKey = "suggestWords"
    val suggestTypesKey = "suggestTypes"

    val pOptions = SuggestOptions()
    var suggestType = SuggestType.GEO.value

    if (options.hasKey(suggestWordsKey) && !options.isNull(suggestWordsKey)) {
      if (options.getType(suggestWordsKey) != ReadableType.Boolean) {
        onError.invoke(IllegalStateException("suggest error: $suggestWordsKey is not a Boolean"))
        return
      }
      pOptions.suggestWords = options.getBoolean(suggestWordsKey)
    }

    if (options.hasKey(userPositionKey) && !options.isNull(userPositionKey)) {
      if (options.getType(userPositionKey) != ReadableType.Map) {
        onError.invoke(IllegalStateException("suggest error: $userPositionKey is not an Object"))
        return
      }
      val userPositionMap = options.getMap(userPositionKey) ?: return

      if (!userPositionMap.hasKey(latKey) || !userPositionMap.hasKey(lonKey)) {
        onError.invoke(IllegalStateException("suggest error: $userPositionKey does not have lat or lon"))
        return
      }

      if (userPositionMap.getType(latKey) != ReadableType.Number || userPositionMap.getType(lonKey) != ReadableType.Number) {
        onError.invoke(IllegalStateException("suggest error: lat or lon is not a Number"))
        return
      }

      val lat = userPositionMap.getDouble(latKey)
      val lon = userPositionMap.getDouble(lonKey)
      val userPosition = Point(lat, lon)

      pOptions.userPosition = userPosition
    }

    if (options.hasKey(suggestTypesKey) && !options.isNull(suggestTypesKey)) {
      if (options.getType(suggestTypesKey) != ReadableType.Array) {
        onError.invoke(IllegalStateException("suggest error: $suggestTypesKey is not an Array"))
        return
      }
      suggestType = SuggestType.UNSPECIFIED.value
      val suggestTypesArray = options.getArray(suggestTypesKey) ?: return
      for (i in 0 until suggestTypesArray.size()) {
        if (suggestTypesArray.getType(i) != ReadableType.Number) {
          onError.invoke(IllegalStateException("suggest error: one or more $suggestTypesKey is not a Number"))
          return
        }
        val value = suggestTypesArray.getInt(i)
        suggestType = suggestType or value
      }
    }

    pOptions.suggestTypes = suggestType
    suggestHandler(text, pOptions, onSuccess, onError)
  }

  fun resetSuggest() {
    suggestSession?.reset()
    suggestSession = null
  }
}
