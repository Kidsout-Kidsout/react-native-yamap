package ru.kidsout.yamap.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

class YamapMarkerViewOnPressEvent(
  surfaceId: Int,
  viewTag: Int,
  private val markerId: String
) : Event<YamapMarkerViewOnPressEvent>(surfaceId, viewTag) {
  override fun getEventName(): String = EVENT_NAME

  override fun canCoalesce(): Boolean = false

  override fun getEventData(): WritableMap = Arguments.createMap().apply {
    putString("id", markerId)
  }

  companion object {
    const val EVENT_NAME = "onPress"
  }
}
