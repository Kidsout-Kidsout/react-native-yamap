package ru.kidsout.yamap.events

import com.facebook.react.uimanager.events.Event

class YamapViewOnMapLoadedEvent : Event<YamapViewOnMapLoadedEvent> {
  var renderObjectCount: Int
  var curZoomModelsLoaded: Int
  var curZoomPlacemarksLoaded: Int
  var curZoomLabelsLoaded: Int
  var curZoomGeometryLoaded: Int
  var tileMemoryUsage: Int
  var delayedGeometryLoaded: Int
  var fullyAppeared: Int
  var fullyLoaded: Int

  constructor(
    surfaceId: Int,
    viewTag: Int,
    renderObjectCount: Int,
    curZoomModelsLoaded: Int,
    curZoomPlacemarksLoaded: Int,
    curZoomLabelsLoaded: Int,
    curZoomGeometryLoaded: Int,
    tileMemoryUsage: Int,
    delayedGeometryLoaded: Int,
    fullyAppeared: Int,
    fullyLoaded: Int
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
