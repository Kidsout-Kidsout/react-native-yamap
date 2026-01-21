package ru.kidsout.yamap.events

import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

class YamapPolygonViewOnPressEvent : Event<YamapPolygonViewOnPressEvent> {
  constructor(surfaceId: Int, viewTag: Int) : super(surfaceId, viewTag)

  override fun getEventName(): String = EVENT_NAME

  override fun canCoalesce(): Boolean {
    return false
  }

  override fun getEventData(): WritableMap? {
    return null
  }

  companion object {
    const val EVENT_NAME = "onPress"
  }
}
