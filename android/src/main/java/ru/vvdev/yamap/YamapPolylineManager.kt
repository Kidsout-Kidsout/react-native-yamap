package ru.vvdev.yamap

import android.view.View
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.yandex.mapkit.geometry.Point
import ru.vvdev.yamap.view.YamapPolyline

class YamapPolylineManager : ViewGroupManager<YamapPolyline>() {
  companion object {
    const val REACT_CLASS = "YamapPolyline"
  }

  override fun getName(): String {
    return REACT_CLASS
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
    return mapOf("onPress" to mapOf("registrationName" to "onPress"))
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any>? {
    return emptyMap()
  }

  private fun castToPolylineView(view: View): YamapPolyline {
    return view as YamapPolyline
  }

  override fun createViewInstance(context: ThemedReactContext): YamapPolyline {
    return YamapPolyline(context)
  }

  @ReactProp(name = "points")
  fun setPoints(view: View, points: ReadableArray?) {
    if (points != null) {
      val parsed = ArrayList<Point>()
      for (i in 0 until points.size()) {
        val markerMap = points.getMap(i)
        if (markerMap != null) {
          val lon = markerMap.getDouble("lon")
          val lat = markerMap.getDouble("lat")
          val point = Point(lat, lon)
          parsed.add(point)
        }
      }
      castToPolylineView(view).setPolygonPoints(parsed)
    }
  }

  @ReactProp(name = "strokeWidth")
  fun setStrokeWidth(view: View, width: Float) {
    castToPolylineView(view).setStrokeWidth(width)
  }

  @ReactProp(name = "strokeColor")
  fun setStrokeColor(view: View, color: Int) {
    castToPolylineView(view).setStrokeColor(color)
  }

  @ReactProp(name = "zIndex")
  fun setZIndex(view: View, zIndex: Int) {
    castToPolylineView(view).setZIndex(zIndex)
  }

  @ReactProp(name = "dashLength")
  fun setDashLength(view: View, length: Int) {
    castToPolylineView(view).setDashLength(length)
  }

  @ReactProp(name = "dashOffset")
  fun setDashOffset(view: View, offset: Int) {
    castToPolylineView(view).setDashOffset(offset)
  }

  @ReactProp(name = "gapLength")
  fun setGapLength(view: View, length: Int) {
    castToPolylineView(view).setGapLength(length)
  }

  @ReactProp(name = "outlineWidth")
  fun setOutlineWidth(view: View, width: Int) {
    castToPolylineView(view).setOutlineWidth(width)
  }

  @ReactProp(name = "outlineColor")
  fun setOutlineColor(view: View, color: Int) {
    castToPolylineView(view).setOutlineColor(color)
  }
}
