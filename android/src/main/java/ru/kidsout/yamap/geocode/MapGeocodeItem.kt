package ru.kidsout.yamap.geocode

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Address

data class MapGeocodeItem(
  var name: String? = null,
  var descriptionText: String? = null,
  var formattedAddress: String? = null,
  var coords: Point? = null,
  var upperCorner: Point? = null,
  var lowerCorner: Point? = null,
  var components: List<Address.Component>? = null
)
