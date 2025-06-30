package ru.vvdev.yamap.view

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import ru.vvdev.yamap.models.ReactMapObject

class YamapPolyline(context: Context) : ViewGroup(context), MapObjectTapListener, ReactMapObject {
  var polyline: Polyline = Polyline(emptyList())
  private var _points: ArrayList<Point> = ArrayList()
  override var mapObject: MapObject? = null
    set(obj) {
      field= obj as? PolylineMapObject
      obj?.addTapListener(this)
      updatePolyline()
    }
  private var outlineColor: Int = Color.BLACK
  private var strokeColor: Int = Color.BLACK
  private var zIndex: Int = 1
  private var strokeWidth: Float = 1f
  private var dashLength: Int = 1
  private var gapLength: Int = 0
  private var dashOffset: Float = 0f
  private var outlineWidth: Int = 0

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

  // PROPS
  fun setPolygonPoints(points: ArrayList<Point>?) {
    _points = points ?: ArrayList()
    polyline = Polyline(_points)
    updatePolyline()
  }

  fun setZIndex(_zIndex: Int) {
    zIndex = _zIndex
    updatePolyline()
  }

  fun setStrokeColor(_color: Int) {
    strokeColor = _color
    updatePolyline()
  }

  fun setDashLength(length: Int) {
    dashLength = length
    updatePolyline()
  }

  fun setDashOffset(offset: Float) {
    dashOffset = offset
    updatePolyline()
  }

  fun setGapLength(length: Int) {
    gapLength = length
    updatePolyline()
  }

  fun setOutlineWidth(width: Int) {
    outlineWidth = width
    updatePolyline()
  }

  fun setOutlineColor(color: Int) {
    outlineColor = color
    updatePolyline()
  }

  fun setStrokeWidth(width: Float) {
    strokeWidth = width
    updatePolyline()
  }

  private fun updatePolyline() {
    (mapObject as PolylineMapObject?)?.let {
      it.geometry = polyline
      it.strokeWidth = strokeWidth
      it.setStrokeColor(strokeColor)
      it.zIndex = zIndex.toFloat()
      it.dashLength = dashLength.toFloat()
      it.gapLength = gapLength.toFloat()
      it.dashOffset = dashOffset
      it.outlineColor = outlineColor
      it.outlineWidth = outlineWidth.toFloat()
    }
  }

  override fun onMapObjectTap(@NonNull mapObject: MapObject, @NonNull point: Point): Boolean {
    val e: WritableMap = Arguments.createMap()
    (context as ReactContext).getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "onPress", e)
    return false
  }
}
