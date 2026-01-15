package ru.kidsout.yamap.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event
import com.yandex.mapkit.geometry.Point

class YamapViewOnMapLongPressEvent : Event<YamapViewOnMapLongPressEvent> {
  var point: Point

  constructor(surfaceId: Int, viewTag: Int, point: Point) : super(surfaceId, viewTag) {
      this.point = point
  }

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap {
    val map = Arguments.createMap()
    map.putDouble("lat", point.latitude)
    map.putDouble("lon", point.longitude)
    return map
  }

  override fun canCoalesce(): Boolean {
    return false
  }

  companion object {
    const val EVENT_NAME = "onMapLongPress"
  }
}
