package ru.kidsout.yamap.types

import android.graphics.Color
import com.yandex.mapkit.geometry.Point

class YamapMarkerViewProps {
  var markerId: String = ""
  var text: String = ""
  var lIndex: Int = 1
  var center: Point = Point(0.0, 0.0)
  var styling = YamapMarkerViewPropsStyling()
  var image: YamapMarkerViewPropsImage? = null
}

class YamapMarkerViewPropsStyling {
  var fontSize: Float = 10.0f
  var fontColor: Int = Color.BLACK
}

class YamapMarkerViewPropsImage {
  var uri: String
  var scale: Int
  var width: Int?
  var height: Int?

  constructor(uri: String, scale: Int, width: Int?, height: Int?) {
    this.uri = uri
    this.scale = scale
    this.width = width
    this.height = height
  }
}
