package ru.kidsout.yamap.events

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

  override fun getCoalescingKey(): Short = 0

  companion object {
    const val EVENT_NAME = "onMapLoaded"
  }
}
