package ru.vvdev.yamap.yageocode

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Address

class YandexGeocodeRNArgsHelper {
  fun createResultItemFrom(data: MapGeocodeItem): WritableMap {
    val result = Arguments.createMap()
    val components = Arguments.createArray()
    result.putString("name", data.name)
    result.putString("descriptionText", data.descriptionText)
    result.putString("formattedAddress", data.formattedAddress)
    result.putMap("coords", data.coords?.let { pointToMap(it) })
    result.putMap("upperCorner", data.upperCorner?.let { pointToMap(it) })
    result.putMap("lowerCorner", data.lowerCorner?.let { pointToMap(it) })

    for (component in data.components ?: listOf()) {
      val item = Arguments.createMap()
      val kinds = Arguments.createArray()
      item.putString("name", component.name)
      for (kind in component.kinds) {
        kinds.pushString(getKindString(kind))
      }
      item.putArray("kinds", kinds)
      components.pushMap(item)
    }

    result.putArray("components", components)
    return result
  }

  private fun getKindString(kind: Address.Component.Kind): String {
    return when (kind) {
      Address.Component.Kind.COUNTRY -> "country"
      Address.Component.Kind.REGION -> "region"
      Address.Component.Kind.PROVINCE -> "area"
      Address.Component.Kind.AREA -> "province"
      Address.Component.Kind.LOCALITY -> "locality"
      Address.Component.Kind.DISTRICT -> "district"
      Address.Component.Kind.STREET -> "street"
      Address.Component.Kind.HOUSE -> "house"
      Address.Component.Kind.ENTRANCE -> "entrance"
      Address.Component.Kind.ROUTE -> "route"
      Address.Component.Kind.STATION -> "station"
      Address.Component.Kind.METRO_STATION -> "metro"
      Address.Component.Kind.RAILWAY_STATION -> "railway"
      Address.Component.Kind.VEGETATION -> "vegetation"
      Address.Component.Kind.HYDRO -> "hydro"
      Address.Component.Kind.AIRPORT -> "airport"
      Address.Component.Kind.OTHER -> "other"
      else -> "unknown"
    }
  }

  private fun pointToMap(point: Point): WritableMap {
    val map = Arguments.createMap()
    map.putDouble("lat", point.latitude)
    map.putDouble("lon", point.longitude)
    return map
  }
}
