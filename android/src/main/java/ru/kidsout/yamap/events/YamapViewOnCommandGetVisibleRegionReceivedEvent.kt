package ru.kidsout.yamap.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event
import com.yandex.mapkit.geometry.Point

class YamapViewOnCommandGetVisibleRegionReceivedEvent : Event<YamapViewOnCommandGetVisibleRegionReceivedEvent> {
  var cid: String
  var bottomLeft: Point
  var bottomRight: Point
  var topLeft: Point
  var topRight: Point

  constructor(
    surfaceId: Int,
    viewTag: Int,
    cid: String,
    bottomLeft: Point,
    bottomRight: Point,
    topLeft: Point,
    topRight: Point,
  ) : super(surfaceId, viewTag) {
    this.cid = cid
    this.bottomLeft = bottomLeft
    this.bottomRight = bottomRight
    this.topLeft = topLeft
    this.topRight = topRight
  }

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap {
    val map = Arguments.createMap()

    val bl = Arguments.createMap()
    bl.putDouble("lat", bottomLeft.latitude)
    bl.putDouble("lon", bottomLeft.longitude)
    map.putMap("bottomLeft", bl)

    val br = Arguments.createMap()
    br.putDouble("lat", bottomRight.latitude)
    br.putDouble("lon", bottomRight.longitude)
    map.putMap("bottomRight", br)

    val tl = Arguments.createMap()
    tl.putDouble("lat", topLeft.latitude)
    tl.putDouble("lon", topLeft.longitude)
    map.putMap("topLeft", tl)

    val tr = Arguments.createMap()
    tr.putDouble("lat", topRight.latitude)
    tr.putDouble("lon", topRight.longitude)
    map.putMap("topRight", tr)

    map.putString("cid", cid)

    return map
  }

  override fun canCoalesce(): Boolean {
    return false
  }

  companion object {
    const val EVENT_NAME = "onCommandGetVisibleRegionReceived"
  }
}
