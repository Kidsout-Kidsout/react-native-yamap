package ru.kidsout.yamap

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.allViews
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.viewmanagers.YamapViewManagerInterface
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapType
import com.yandex.mapkit.mapview.MapView
import ru.kidsout.yamap.events.YamapViewOnMapLoadedEvent
import ru.kidsout.yamap.types.YamapViewProps

class YamapView: FrameLayout, YamapViewManagerInterface<YamapView> {
  private var map: MapView
  private var props = YamapViewProps()
  private var subviewsMap = mutableMapOf<Int, View>()

  constructor(context: Context) : super(context) {
    this.map = MapView(context)
    configureComponent()
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    this.map = MapView(context)
    configureComponent()
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    this.map = MapView(context)
    configureComponent()
  }

  override fun onDetachedFromWindow() {
    map.onStop()
    super.onDetachedFromWindow()
  }

  fun getEventEmitter(): EventDispatcher {
    return UIManagerHelper.getEventDispatcherForReactTag(context as ReactContext, id)!!
  }

  fun getSurfaceId(): Int {
    return UIManagerHelper.getSurfaceId(context)
  }

  fun addSubview(child: View, index: Int) {
    when (child) {
      is YamapCircleView -> {
        child.setCollection(map.mapWindow.map.mapObjects)
        subviewsMap[index] = child
      }
      else -> addView(child, index)
    }
  }

  fun removeSubviewAt(index: Int) {
    when (val v = subviewsMap[index]) {
      is YamapCircleView -> v.unmount()
      else -> removeViewAt(index)
    }
  }

  fun getSubviewAt(index: Int): View? {
    return when (val v = subviewsMap[index]) {
      is View -> v
      else -> getChildAt(index)
    }
  }

  fun getSubviewCount(): Int {
    return subviewsMap.size + allViews.count()
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
    val cp = CameraPosition(Point(lat, lon), zoom.toFloat(), azimuth.toFloat(), tilt.toFloat())
    val an = Animation(if (animationType == 1) Animation.Type.SMOOTH else Animation.Type.LINEAR, animationDuration.toFloat())
    map.mapWindow.map.move(cp, an) { completed -> TODO("Not yet implemented") }
  }

  override fun commandSetBounds(view: YamapView?, cid: String?, bottomLeftPointLat: Double, bottomLeftPointLon: Double, topRightPointLat: Double, topRightPointLon: Double, minZoom: Double, maxZoom: Double, offset: Double, animationType: Int, animationDuration: Double) {
    val bb = BoundingBox(
      Point(bottomLeftPointLat, bottomLeftPointLon),
      Point(topRightPointLat, topRightPointLon)
    )

    val cp = map.mapWindow.map.cameraPosition(Geometry.fromBoundingBox(bb))
    val an = Animation(if (animationType == 1) Animation.Type.SMOOTH else Animation.Type.LINEAR, animationDuration.toFloat())
    map.mapWindow.map.move(cp, an) { completed -> TODO("Not yet implemented") }
  }

  override fun commandSetZoom(view: YamapView?, cid: String?, zoom: Double, offset: Double, animationType: Int, animationDuration: Double) {
    val ccp = map.mapWindow.map.cameraPosition
    val cp = CameraPosition(ccp.target, zoom.toFloat(), ccp.azimuth, ccp.tilt)
    val an = Animation(if (animationType == 1) Animation.Type.SMOOTH else Animation.Type.LINEAR, animationDuration.toFloat())
    map.mapWindow.map.move(cp, an) { completed -> TODO("Not yet implemented") }
  }

  override fun commandGetCameraPosition(view: YamapView?, cid: String?) {
    val cp = map.mapWindow.map.cameraPosition
    TODO()
  }

  override fun commandGetVisibleRegion(view: YamapView?, cid: String?) {
    val vr = map.mapWindow.map.visibleRegion
    TODO()
  }

  fun update() {
    map.mapWindow.map.isNightModeEnabled = props.nightMode
    map.mapWindow.map.mapType = props.getEffectiveMapType()
    map.mapWindow.map.isScrollGesturesEnabled = props.scrollGesturesEnabled
    map.mapWindow.map.isZoomGesturesEnabled = props.zoomGesturesEnabled
    map.mapWindow.map.isTiltGesturesEnabled = props.tiltGesturesEnabled
    map.mapWindow.map.isRotateGesturesEnabled = props.rotateGesturesEnabled
    map.mapWindow.map.isFastTapEnabled = props.fastTapEnabled
    map.mapWindow.setMaxFps(props.maxFps.toFloat())
  }

  private fun configureComponent() {
    map.mapWindow.map.setMapLoadedListener { stats ->
      getEventEmitter().dispatchEvent(YamapViewOnMapLoadedEvent(
        getSurfaceId(),
        id,
        stats.renderObjectCount,
        stats.curZoomModelsLoaded.toDouble(),
        stats.curZoomPlacemarksLoaded.toDouble(),
        stats.curZoomLabelsLoaded.toDouble(),
        stats.curZoomGeometryLoaded.toDouble(),
        stats.tileMemoryUsage,
        stats.delayedGeometryLoaded.toDouble(),
        stats.fullyAppeared.toDouble(),
        stats.fullyLoaded.toDouble()
      ))
    }

    this.addView(map)
    map.onStart()
    this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    this.map.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
  }
}
