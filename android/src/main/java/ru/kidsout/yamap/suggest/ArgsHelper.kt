package ru.kidsout.yamap.suggest

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap

class ArgsHelper {
  fun createSuggestsMapFrom(data: List<MapSuggestItem>): WritableArray {
    val result = Arguments.createArray()
    for (item in data) {
      result.pushMap(createSuggestMapFrom(item))
    }
    return result
  }

  private fun createSuggestMapFrom(data: MapSuggestItem): WritableMap {
    val result = Arguments.createMap()
    result.putString("title", data.title)
    result.putString("subtitle", data.subtitle)
    result.putString("uri", data.uri)
    result.putString("searchText", data.searchText)
    result.putString("displayText", data.displayText)
    return result
  }
}
