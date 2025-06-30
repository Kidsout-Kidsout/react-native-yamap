package ru.vvdev.yamap

import android.view.View
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.yandex.mapkit.geometry.Point
import ru.vvdev.yamap.view.YamapPolygon

class YamapPolygonManager : ViewGroupManager<YamapPolygon>() {
  companion object {
    const val REACT_CLASS = "YamapPolygon"
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

  private fun castToPolygonView(view: View): YamapPolygon {
    return view as YamapPolygon
  }

  override fun createViewInstance(context: ThemedReactContext): YamapPolygon {
    return YamapPolygon(context)
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
      castToPolygonView(view).setPolygonPoints(parsed)
    }
  }

  @ReactProp(name = "innerRings")
  fun setInnerRings(view: View, _rings: ReadableArray?) {
    val rings = ArrayList<ArrayList<Point>>()
    if (_rings != null) {
      for (j in 0 until _rings.size()) {
        val points = _rings.getArray(j)
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
          rings.add(parsed)
        }
      }
    }
    castToPolygonView(view).setInnerRings(rings)
  }

  @ReactProp(name = "strokeWidth")
  fun setStrokeWidth(view: View, width: Float) {
    castToPolygonView(view).setStrokeWidth(width)
  }

  @ReactProp(name = "strokeColor")
  fun setStrokeColor(view: View, color: Int) {
    castToPolygonView(view).setStrokeColor(color)
  }

  @ReactProp(name = "fillColor")
  fun setFillColor(view: View, color: Int) {
    castToPolygonView(view).setFillColor(color)
  }

  @ReactProp(name = "zIndex")
  fun setZIndex(view: View, zIndex: Int) {
    castToPolygonView(view).setZIndex(zIndex)
  }
}
