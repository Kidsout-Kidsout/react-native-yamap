package ru.kidsout.yamap.events

import com.facebook.react.uimanager.events.Event

class YamapCircleViewOnPressEvent : Event<YamapCircleViewOnPressEvent> {
  constructor(surfaceId: Int, viewTag: Int) : super(surfaceId, viewTag) {}

  override fun getEventName(): String = EVENT_NAME

  override fun getCoalescingKey(): Short = 0

  companion object {
    const val EVENT_NAME = "onPress"
  }
}
