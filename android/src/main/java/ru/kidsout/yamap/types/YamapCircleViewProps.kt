package ru.kidsout.yamap.types

import android.graphics.Color
import com.yandex.mapkit.geometry.Point

class YamapCircleViewPropsStyling {
  var fillColor = Color.TRANSPARENT
  var strokeColor = Color.TRANSPARENT
  var strokeWidth = 0.0f
}

class YamapCircleViewProps {
  var lIndex = 0
  var center = Point()
  var radius = 0.0f
  var styling = YamapCircleViewPropsStyling();

  var onPress: (() -> Unit)? = null
}
