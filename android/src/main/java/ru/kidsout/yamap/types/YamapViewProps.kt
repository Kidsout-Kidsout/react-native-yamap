package ru.kidsout.yamap.types

import com.yandex.mapkit.map.MapType

class YamapViewProps {
  var nightMode = false
  var mapType = MapType.NONE
  var scrollGesturesEnabled = false
  var zoomGesturesEnabled = false
  var tiltGesturesEnabled = false
  var rotateGesturesEnabled = false
  var fastTapEnabled = false
  var maxFps = 60
}
