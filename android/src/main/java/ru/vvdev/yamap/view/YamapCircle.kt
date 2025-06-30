package ru.vvdev.yamap.view

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import ru.vvdev.yamap.models.ReactMapObject

class YamapCircle(context: Context) : ViewGroup(context), MapObjectTapListener, ReactMapObject {
  var circle: Circle = Circle(Point(0.0, 0.0), 0f)

  override var mapObject: MapObject? = null
    set(mapObject) {
      field = mapObject
      mapObject?.addTapListener(this)
      updateCircle()
    }
  private var fillColor: Int = Color.BLACK
  private var strokeColor: Int = Color.BLACK
  private var zIndex: Int = 1
  private var strokeWidth: Float = 1f
  private var center: Point = Point(0.0, 0.0)
  private var radius: Float = 0f

  init {
    circle = Circle(center, radius)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

  // PROPS
  fun setCenter(point: Point) {
    center = point
    updateGeometry()
    updateCircle()
  }

  fun setRadius(_radius: Float) {
    radius = _radius
    updateGeometry()
    updateCircle()
  }

  private fun updateGeometry() {
    circle = Circle(center, radius)
  }

  fun setZIndex(_zIndex: Int) {
    zIndex = _zIndex
    updateCircle()
  }

  fun setStrokeColor(_color: Int) {
    strokeColor = _color
    updateCircle()
  }

  fun setFillColor(_color: Int) {
    fillColor = _color
    updateCircle()
  }

  fun setStrokeWidth(width: Float) {
    strokeWidth = width
    updateCircle()
  }

  private fun updateCircle() {
    (mapObject as CircleMapObject?)?.apply {
      setGeometry(circle)
      setStrokeWidth(strokeWidth)
      setStrokeColor(strokeColor)
      setFillColor(fillColor)
      setZIndex(zIndex)
    }
  }

  override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
    val e: WritableMap = Arguments.createMap()
    (context as ReactContext).getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "onPress", e)
    return false
  }
}
