package ru.kidsout.yamap.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.yandex.mapkit.geometry.Point

class YamapCirclePressEvent : Event<YamapCirclePressEvent> {
  private val point: Point

  constructor(surfaceId: Int, viewTag: Int, point: Point) : super(surfaceId, viewTag) {
    this.point = point
  }

  override fun getEventName(): String = "press"

  override fun getCoalescingKey(): Short = 0

  companion object {
    const val EVENT_NAME = "onPress"
  }
}
