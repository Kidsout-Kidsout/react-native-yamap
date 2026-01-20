package ru.kidsout.yamap.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event
import com.yandex.mapkit.geometry.Point

class YamapViewOnCommandGetCameraPositionReceivedEvent : Event<YamapViewOnCommandGetCameraPositionReceivedEvent> {
  var cid: String
  var zoom: Double
  var tilt: Double
  var azimuth: Double
  var point: Point

  constructor(surfaceId: Int, viewTag: Int, cid: String, zoom: Double, tilt: Double, azimuth: Double, point: Point) : super(surfaceId, viewTag) {
    this.cid = cid
    this.zoom = zoom
    this.tilt = tilt
    this.azimuth = azimuth
    this.point = point
  }

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap {
    val map = Arguments.createMap()
    map.putDouble("zoom", zoom)
    map.putDouble("tilt", tilt)
    map.putDouble("azimuth", azimuth)
    val p = Arguments.createMap()
    p.putDouble("lat", point.latitude)
    p.putDouble("lon", point.longitude)
    map.putMap("point", p)
    map.putString("cid", cid)
    return map
  }

  override fun canCoalesce(): Boolean {
    return false
  }

  companion object {
    const val EVENT_NAME = "onCommandGetCameraPositionReceived"
  }
}
