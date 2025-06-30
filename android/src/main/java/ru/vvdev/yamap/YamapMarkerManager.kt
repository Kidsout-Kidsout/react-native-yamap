package ru.vvdev.yamap

import android.graphics.PointF
import android.view.View
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.yandex.mapkit.geometry.Point
import ru.vvdev.yamap.view.YamapMarker

class YamapMarkerManager : ViewGroupManager<YamapMarker>() {
  companion object {
    const val REACT_CLASS = "YamapMarker"
  }

  override fun getName(): String {
    return REACT_CLASS
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> {
    return mapOf("onPress" to mapOf("registrationName" to "onPress"))
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> {
    return emptyMap()
  }

  private fun castToMarkerView(view: View): YamapMarker {
    return view as YamapMarker
  }

  override fun createViewInstance(context: ThemedReactContext): YamapMarker {
    return YamapMarker(context)
  }

  @ReactProp(name = "point")
  fun setPoint(view: View, markerPoint: ReadableMap?) {
    markerPoint?.let {
      val lon = it.getDouble("lon")
      val lat = it.getDouble("lat")
      val point = Point(lat, lon)
      castToMarkerView(view).point = point
    }
  }

  @ReactProp(name = "zIndex")
  fun setZIndex(view: View, zIndex: Int) {
    castToMarkerView(view).setZIndex(zIndex)
  }

  @ReactProp(name = "scale")
  fun setScale(view: View, scale: Float) {
    castToMarkerView(view).setScale(scale)
  }

  @ReactProp(name = "visible")
  fun setVisible(view: View, visible: Boolean?) {
    castToMarkerView(view).setVisible(visible ?: true)
  }

  @ReactProp(name = "source")
  fun setSource(view: View, source: String?) {
    source?.let {
      castToMarkerView(view).setIconSource(it)
    }
  }

  @ReactProp(name = "anchor")
  fun setAnchor(view: View, anchor: ReadableMap?) {
    anchor?.let {
      PointF(it.getDouble("x").toFloat(), it.getDouble("y").toFloat())
    }?.let { castToMarkerView(view).setAnchor(it) }
  }

  override fun addView(parent: YamapMarker, child: View, index: Int) {
    parent.addChildView(child, index)
    super.addView(parent, child, index)
  }

  override fun removeViewAt(parent: YamapMarker, index: Int) {
    parent.removeChildView(index)
    super.removeViewAt(parent, index)
  }

  override fun receiveCommand(view: YamapMarker, commandType: String, args: ReadableArray?) {
    when (commandType) {
      "animatedMoveTo" -> {
        val markerPoint = args?.getMap(0)
        val moveDuration = args?.getDouble(1)?.toFloat() ?: 0f
        val lon = markerPoint?.getDouble("lon")?.toFloat() ?: 0f
        val lat = markerPoint?.getDouble("lat")?.toFloat() ?: 0f
        val point = Point(lat.toDouble(), lon.toDouble())
        castToMarkerView(view).animatedMoveTo(point, moveDuration)
      }
      "animatedRotateTo" -> {
        val angle = args?.getDouble(0)?.toFloat() ?: 0f
        val rotateDuration = args?.getDouble(1)?.toFloat() ?: 0f
        castToMarkerView(view).animatedRotateTo(angle, rotateDuration)
      }
      else -> throw IllegalArgumentException("Unsupported command $commandType received by ${this::class.java.simpleName}.")
    }
  }
}
