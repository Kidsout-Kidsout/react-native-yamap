package ru.kidsout.yamap.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

class YamapViewOnCommandSetZoomReceivedEvent : Event<YamapViewOnCommandSetZoomReceivedEvent> {
  var cid: String
  var completed: Boolean

  constructor(surfaceId: Int, viewTag: Int, cid: String, completed: Boolean) : super(surfaceId, viewTag) {
    this.cid = cid
    this.completed = completed
  }

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap {
    val map = Arguments.createMap()
    map.putString("cid", cid)
    map.putBoolean("completed", completed)
    return map
  }

  override fun canCoalesce(): Boolean {
    return false
  }

  companion object {
    const val EVENT_NAME = "onCommandSetZoomReceived"
  }
}
