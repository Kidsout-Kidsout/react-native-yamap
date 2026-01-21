package ru.kidsout.yamap.types

import android.graphics.Color

class YamapClustersViewProps {
  var radius: Double = 100.0
  var minZoom: Double = 15.0
  var clusterStyle: YamapClusterStyle = YamapClusterStyle()
}

class YamapClusterStyle {
  var fontSize: Float = 14f
  var fontColor: Int = Color.BLACK
  var fillColor: Int = Color.WHITE
  var strokeColor: Int = Color.parseColor("#e54f1de8")
  var strokeWidth: Float = 2f
  var padding: Float = 5f
}
