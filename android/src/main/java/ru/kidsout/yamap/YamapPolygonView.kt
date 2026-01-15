package ru.kidsout.yamap

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.viewmanagers.YamapPolygonViewManagerInterface
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.EventDispatcher
import ru.kidsout.yamap.events.YamapPolygonViewOnPressEvent
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.LinearRing
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolygonMapObject
import ru.kidsout.yamap.types.YamapPolygonViewProps

class YamapPolygonView: View, YamapPolygonViewManagerInterface<YamapPolygonView> {
  private var col: MapObjectCollection? = null
  private var obj: PolygonMapObject? = null
  private var props = YamapPolygonViewProps()

  constructor(context: Context) : super(context) {
    configureComponent()
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    configureComponent()
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    configureComponent()
  }

  fun getEventEmitter(): EventDispatcher {
    return UIManagerHelper.getEventDispatcherForReactTag(context as ReactContext, id)!!
  }

  fun getSurfaceId(): Int {
    return UIManagerHelper.getSurfaceId(context)
  }

  fun setCollection(col: MapObjectCollection) {
    this.col = col
    this.obj = col.addPolygon(Polygon(
      LinearRing(listOf()),
      listOf()
    ))
    obj!!.addTapListener(tapListener)
    update()
  }

  private val tapListener = MapObjectTapListener { p0, p1 ->
    val dispatcher = getEventEmitter()
    val surfaceId = getSurfaceId()
    dispatcher.dispatchEvent(YamapPolygonViewOnPressEvent(surfaceId, id))
    true
  }

  override fun setPoints(view: YamapPolygonView?, value: ReadableArray?) {
    val value = value ?: return
    val pts = mutableListOf<Point>()
    for (i in 0 until value.size()) {
      val item = value.getMap(i)!!
      pts.add(Point(item.getDouble("lat"), item.getDouble("lon")))
    }
    props.points = pts
    update()
  }

  override fun setInnerRings(view: YamapPolygonView?, value: ReadableArray?) {
    val value = value ?: return
    val rings = mutableListOf<List<Point>>()
    for (i in 0 until value.size()) {
      val ring = value.getArray(i)!!
      val pts = mutableListOf<Point>()
      for (i in 0 until ring.size()) {
        val item = ring.getMap(i)!!
        pts.add(Point(item.getDouble("lat"), item.getDouble("lon")))
      }
      rings.add(pts)
    }
    props.innerRings = rings
    update()
  }

  override fun setLIndex(view: YamapPolygonView?, value: Int) {
    props.lIndex = value
    update()
  }

  override fun setStyling(view: YamapPolygonView?, value: ReadableMap?) {
    val value = value ?: return
    props.styling.fillColor = value.getInt("fillColor")
    props.styling.strokeColor = value.getInt("strokeColor")
    props.styling.strokeWidth = value.getDouble("strokeWidth").toFloat()
    update()
  }

  fun update() {
    val obj = obj ?: return
    obj.geometry = Polygon(LinearRing(props.points), props.innerRings.map { LinearRing(it) })
    obj.zIndex = props.lIndex.toFloat()
    obj.strokeColor = props.styling.strokeColor
    obj.strokeWidth = props.styling.strokeWidth
    obj.fillColor = props.styling.fillColor
  }

  fun unmount() {
    col?.let { col ->
      obj?.let { obj ->
        col.remove(obj)
      }
    }
    col = null
    obj = null
  }

  private fun configureComponent() {
    this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
  }
}
