package ru.kidsout.yamap.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

class YamapViewOnMapLoadedEvent : Event<YamapViewOnMapLoadedEvent> {
  var renderObjectCount: Int
  var curZoomModelsLoaded: Double
  var curZoomPlacemarksLoaded: Double
  var curZoomLabelsLoaded: Double
  var curZoomGeometryLoaded: Double
  var tileMemoryUsage: Int
  var delayedGeometryLoaded: Double
  var fullyAppeared: Double
  var fullyLoaded: Double

  constructor(
    surfaceId: Int,
    viewTag: Int,
    renderObjectCount: Int,
    curZoomModelsLoaded: Double,
    curZoomPlacemarksLoaded: Double,
    curZoomLabelsLoaded: Double,
    curZoomGeometryLoaded: Double,
    tileMemoryUsage: Int,
    delayedGeometryLoaded: Double,
    fullyAppeared: Double,
    fullyLoaded: Double
  ) : super(surfaceId, viewTag) {
    this.renderObjectCount = renderObjectCount
    this.curZoomModelsLoaded = curZoomModelsLoaded
    this.curZoomPlacemarksLoaded = curZoomPlacemarksLoaded
    this.curZoomLabelsLoaded = curZoomLabelsLoaded
    this.curZoomGeometryLoaded = curZoomGeometryLoaded
    this.tileMemoryUsage = tileMemoryUsage
    this.delayedGeometryLoaded = delayedGeometryLoaded
    this.fullyAppeared = fullyAppeared
    this.fullyLoaded = fullyLoaded
  }

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap {
    val map = Arguments.createMap()
    map.putInt("renderObjectCount", renderObjectCount)
    map.putDouble("curZoomModelsLoaded", curZoomModelsLoaded)
    map.putDouble("curZoomPlacemarksLoaded", curZoomPlacemarksLoaded)
    map.putDouble("curZoomLabelsLoaded", curZoomLabelsLoaded)
    map.putDouble("curZoomGeometryLoaded", curZoomGeometryLoaded)
    map.putInt("tileMemoryUsage", tileMemoryUsage)
    map.putDouble("delayedGeometryLoaded", delayedGeometryLoaded)
    map.putDouble("fullyAppeared", fullyAppeared)
    map.putDouble("fullyLoaded", fullyLoaded)
    return map
  }

  override fun canCoalesce(): Boolean {
    return false
  }

  companion object {
    const val EVENT_NAME = "onMapLoaded"
  }
}
