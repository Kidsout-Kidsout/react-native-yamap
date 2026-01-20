package ru.kidsout.yamap

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.EventDispatcher
import ru.kidsout.yamap.events.YamapCircleViewOnPressEvent
import com.facebook.react.viewmanagers.YamapViewManagerInterface
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapType
import com.yandex.mapkit.mapview.MapView
import ru.kidsout.yamap.types.YamapViewProps

class YamapView: FrameLayout, YamapViewManagerInterface<YamapView> {
  private var map: MapView
  private var props = YamapViewProps()

  constructor(context: Context) : super(context) {
    this.map = MapView(context)
    this.addView(map)
    configureComponent()
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    this.map = MapView(context)
    this.addView(map)
    configureComponent()
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    this.map = MapView(context)
    this.addView(map)
    configureComponent()
  }

  fun getEventEmitter(): EventDispatcher {
    return UIManagerHelper.getEventDispatcherForReactTag(context as ReactContext, id)!!
  }

  fun getSurfaceId(): Int {
    return UIManagerHelper.getSurfaceId(context)
  }

  override fun addView(child: View?, index: Int) {
    val child = child ?: return
    when (child) {
      is YamapCircleView -> child.setCollection(map.mapWindow.map.mapObjects)
      else -> super<FrameLayout>.addView(child, index)
    }
  }

  override fun removeView(view: View?) {
    val view = view ?: return
    when (view) {
      is YamapCircleView -> view.unmount()
    }
  }

  override fun setNightMode(view: YamapView?, value: Boolean) {
    props.nightMode = value
    update()
  }
  override fun setMapType(view: YamapView?, value: String?) {
    val mt = when (value) {
      "none" -> MapType.NONE
      "raster" -> MapType.MAP
      "vector" -> MapType.VECTOR_MAP
      "satellite" -> MapType.SATELLITE
      "hybrid" -> MapType.HYBRID
      else -> MapType.NONE
    }
    props.mapType = mt
    update()
  }
  override fun setScrollGesturesEnabled(view: YamapView?, value: Boolean) {
    props.scrollGesturesEnabled = value
    update()
  }
  override fun setZoomGesturesEnabled(view: YamapView?, value: Boolean) {
    props.zoomGesturesEnabled = value
  }
  override fun setTiltGesturesEnabled(view: YamapView?, value: Boolean) {
    props.tiltGesturesEnabled = value
    update()
  }
  override fun setRotateGesturesEnabled(view: YamapView?, value: Boolean) {
    props.rotateGesturesEnabled = value
    update()
  }
  override fun setFastTapEnabled(view: YamapView?, value: Boolean) {
    props.fastTapEnabled = value
    update()
  }
  override fun setMaxFps(view: YamapView?, value: Int) {
    props.maxFps = value
    update()
  }

  override fun commandSetCenter(view: YamapView?, cid: String?, lat: Double, lon: Double, zoom: Double, azimuth: Double, tilt: Double, offset: Double, animationType: Int, animationDuration: Double) {
    TODO()
  }

  override fun commandSetBounds(view: YamapView?, cid: String?, bottomLeftPointLat: Double, bottomLeftPointLon: Double, topRightPointLat: Double, topRightPointLon: Double, minZoom: Double, maxZoom: Double, offset: Double, animationType: Int, animationDuration: Double) {
    TODO()
  }

  override fun commandSetZoom(view: YamapView?, cid: String?, zoom: Double, offset: Double, animationType: Int, animationDuration: Double) {
    TODO()
  }

  override fun commandGetCameraPosition(view: YamapView?, cid: String?) {
    TODO()
  }

  override fun commandGetVisibleRegion(view: YamapView?, cid: String?) {
    TODO()
  }

  fun update() {
    map.mapWindow.map.isNightModeEnabled = props.nightMode
    map.mapWindow.map.mapType = props.mapType
    map.mapWindow.map.isScrollGesturesEnabled = props.scrollGesturesEnabled
    map.mapWindow.map.isZoomGesturesEnabled = props.zoomGesturesEnabled
    map.mapWindow.map.isTiltGesturesEnabled = props.tiltGesturesEnabled
    map.mapWindow.map.isRotateGesturesEnabled = props.rotateGesturesEnabled
    map.mapWindow.map.isFastTapEnabled = props.fastTapEnabled
    map.mapWindow.setMaxFps(props.maxFps.toFloat());
  }

  private fun configureComponent() {
    this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    this.map.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
  }
}
