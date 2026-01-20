package ru.kidsout.yamap

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.EventDispatcher
import ru.kidsout.yamap.events.YamapCirclePressEvent
import com.facebook.react.viewmanagers.YamapCircleViewManagerInterface
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.MapObjectCollection
import ru.kidsout.yamap.types.YamapCircleViewProps
import ru.kidsout.yamap.types.YamapCircleViewPropsStyling

class YamapCircleView: View, YamapCircleViewManagerInterface<YamapCircleView> {
  private var col: MapObjectCollection? = null
  private var obj: CircleMapObject? = null
  private var props = YamapCircleViewProps()

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
    this.obj = col.addCircle(Circle())
    obj!!.addTapListener { _, p ->
      val dispatcher = getEventEmitter()
      val surfaceId = getSurfaceId()
      dispatcher.dispatchEvent(YamapCirclePressEvent(surfaceId, id, p))
      true
    }
    update()
  }

  override fun setCenter(view: YamapCircleView?, value: ReadableMap?) {
    val view = view ?: return
    val value = value ?: return
    props.center = Point(value.getDouble("lat"), value.getDouble("lon"))
    view.updateProps(props)
  }

  override fun setLIndex(view: YamapCircleView?, value: Int) {
    val view = view ?: return
    props.lIndex = value
    view.updateProps(props)
  }

  override fun setRadius(view: YamapCircleView?, value: Double) {
    val view = view ?: return
    props.radius = value.toFloat()
    view.updateProps(props)
  }

  override fun setStyling(view: YamapCircleView?, value: ReadableMap?) {
    val view = view ?: return
    val value = value ?: return
    props.styling = YamapCircleViewPropsStyling().apply {
      fillColor = value.getInt("fillColor")
      strokeColor = value.getInt("strokeColor")
      strokeWidth = value.getDouble("strokeWidth").toFloat()
    }
    view.updateProps(props)
  }

  fun updateProps(props: YamapCircleViewProps) {
    this.props = props
    update()
  }

  fun update() {
    val obj = obj ?: return
    obj.geometry = Circle(props.center, props.radius)
    obj.zIndex = props.lIndex.toFloat()
    obj.strokeColor = props.styling.strokeColor
    obj.strokeWidth = props.styling.strokeWidth
    obj.fillColor = props.styling.fillColor
  }

  private fun configureComponent() {
    this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
  }
}
