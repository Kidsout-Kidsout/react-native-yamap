package ru.kidsout.yamap.types

import android.graphics.Color
import com.yandex.mapkit.geometry.Point

class YamapPolygonViewProps {
  var lIndex = 0
  var points: List<Point> = listOf()
  var innerRings: List<List<Point>> = listOf()
  var styling = YamapPolygonViewPropsStyling()
}

class YamapPolygonViewPropsStyling {
  var fillColor = Color.TRANSPARENT
  var strokeColor = Color.TRANSPARENT
  var strokeWidth = 0.0f
}
