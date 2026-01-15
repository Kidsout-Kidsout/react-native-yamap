package ru.kidsout.yamap.geocode

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Address

data class YamapGeocodeResult(
  var name: String,
  var descriptionText: String,
  var formattedAddress: String,
  var coords: Point,
  var box: BoundingBox,
  var components: List<Address.Component>,
) {
  fun toMap(): ReadableMap {
    val result = Arguments.createMap()
    val components = Arguments.createArray()
    result.putString("name", name)
    result.putString("descriptionText", descriptionText)
    result.putString("formattedAddress", formattedAddress)
    result.putMap("coords", pointToMap(coords))
    result.putMap("upperCorner", pointToMap(box.northEast))
    result.putMap("lowerCorner", pointToMap(box.southWest))

    for (component in this.components) {
      val item = Arguments.createMap()
      item.putString("name", component.name)

      val kinds = Arguments.createArray()
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
