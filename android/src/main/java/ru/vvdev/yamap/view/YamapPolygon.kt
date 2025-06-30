package ru.vvdev.yamap.view

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.yandex.mapkit.geometry.LinearRing
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolygonMapObject
import ru.vvdev.yamap.models.ReactMapObject

class YamapPolygon(context: Context) : ViewGroup(context), MapObjectTapListener, ReactMapObject {
  var polygon: Polygon = Polygon(LinearRing(emptyList()), emptyList())
  private var _points: ArrayList<Point> = ArrayList()
  private var innerRings: ArrayList<ArrayList<Point>> = ArrayList()
  private var mapObject: PolygonMapObject? = null
  private var fillColor: Int = Color.BLACK
  private var strokeColor: Int = Color.BLACK
  private var zIndex: Int = 1
  private var strokeWidth: Float = 1f

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

  // PROPS
  fun setPolygonPoints(points: ArrayList<Point>?) {
    _points = points ?: ArrayList()
    updatePolygonGeometry()
    updatePolygon()
  }

  fun setInnerRings(innerRings: ArrayList<ArrayList<Point>>?) {
    this.innerRings = innerRings ?: ArrayList()
    updatePolygonGeometry()
    updatePolygon()
  }

  private fun updatePolygonGeometry() {
    val rings = ArrayList<LinearRing>()
    innerRings.forEach { rings.add(LinearRing(it)) }
    polygon = Polygon(LinearRing(_points), rings)
  }

  fun setZIndex(zIndex: Int) {
    this.zIndex = zIndex
    updatePolygon()
  }

  fun setStrokeColor(color: Int) {
    strokeColor = color
    updatePolygon()
  }

  fun setFillColor(color: Int) {
    fillColor = color
    updatePolygon()
  }

  fun setStrokeWidth(width: Float) {
    strokeWidth = width
    updatePolygon()
  }

  private fun updatePolygon() {
    mapObject?.let {
      it.geometry = polygon
      it.strokeWidth = strokeWidth
      it.strokeColor = strokeColor
      it.fillColor = fillColor
      it.zIndex = zIndex.toFloat()
    }
  }

  fun setMapObject(obj: MapObject) {
    mapObject = obj as PolygonMapObject
    mapObject?.addTapListener(this)
    updatePolygon()
  }

  fun getMapObject(): MapObject? {
    return mapObject
  }

  override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
    val e: WritableMap = Arguments.createMap()
    (context as ReactContext).getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "onPress", e)
    return false
  }
}
