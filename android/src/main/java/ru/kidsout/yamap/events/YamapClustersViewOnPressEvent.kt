package ru.kidsout.yamap.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableArray
import com.facebook.react.uimanager.events.Event

class YamapClustersViewOnPressEvent(
  surfaceId: Int,
  viewTag: Int,
  private val ids: List<String>
) : Event<YamapClustersViewOnPressEvent>(surfaceId, viewTag) {
  override fun getEventName(): String = EVENT_NAME

  override fun canCoalesce(): Boolean = false

  override fun getEventData(): WritableMap = Arguments.createMap().apply {
    val arr: WritableArray = Arguments.createArray()
    ids.forEach { arr.pushString(it) }
    putArray("ids", arr)
  }

  companion object {
    const val EVENT_NAME = "onPress"
  }
}
