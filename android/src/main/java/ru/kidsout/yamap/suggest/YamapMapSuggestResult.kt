package ru.kidsout.yamap.suggest

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap

data class YamapMapSuggestResult(
  var title: String = "",
  var subtitle: String?,
  var uri: String?,
  var searchText: String,
  var displayText: String
) {
  fun toMap(): ReadableMap {
    val result = Arguments.createMap()
    result.putString("title", title)
    result.putString("subtitle", subtitle)
    result.putString("uri", uri)
    result.putString("searchText", searchText)
    result.putString("displayText", displayText)
    return result
  }
}
