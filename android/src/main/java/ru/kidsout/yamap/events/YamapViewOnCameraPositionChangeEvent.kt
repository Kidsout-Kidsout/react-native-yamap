package ru.kidsout.yamap.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraUpdateReason

class YamapViewOnCameraPositionChangeEvent : Event<YamapViewOnCameraPositionChangeEvent> {
  var zoom: Double
  var tilt: Double
  var azimuth: Double
  var point: Point
  var reason: CameraUpdateReason
  var finished: Boolean

  constructor(surfaceId: Int, viewTag: Int, zoom: Double, tilt: Double, azimuth: Double, point: Point, reason: CameraUpdateReason, finished: Boolean) : super(surfaceId, viewTag) {
    this.zoom = zoom
    this.tilt = tilt
    this.azimuth = azimuth
    this.point = point
    this.reason = reason
    this.finished = finished
  }

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap = Arguments.createMap().apply {
    putDouble("zoom", zoom)
    putDouble("tilt", tilt)
    putDouble("azimuth", azimuth)
    putMap("point", getPointSerialized())
    putString("reason", getReasonSerialized())
    putBoolean("finished", finished)
  }

  override fun canCoalesce(): Boolean {
    return false
  }

  private fun getPointSerialized(): ReadableMap {
    val map = Arguments.createMap()
    map.putDouble("lat", point.latitude)
    map.putDouble("lon", point.longitude)
    return map
  }

  private fun getReasonSerialized(): String {
    return when(reason) {
      CameraUpdateReason.GESTURES -> "gesture"
      else -> "application"
    }
  }

  companion object {
    const val EVENT_NAME = "onCameraPositionChange"
  }
}
