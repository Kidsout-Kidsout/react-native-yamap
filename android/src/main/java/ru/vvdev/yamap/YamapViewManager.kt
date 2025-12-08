package ru.vvdev.yamap

import android.view.View
import androidx.annotation.NonNull
import com.facebook.infer.annotation.Assertions
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import ru.vvdev.yamap.view.YamapView

class YamapViewManager : ViewGroupManager<YamapView>() {
  companion object {
    const val REACT_CLASS = "YamapView"

    private const val SET_CENTER = 1
    private const val SET_BOUNDS = 2
    private const val FIT_ALL_MARKERS = 3
    private const val FIND_ROUTES = 4
    private const val SET_ZOOM = 5
    private const val GET_CAMERA_POSITION = 6
    private const val GET_VISIBLE_REGION = 7
    private const val SET_TRAFFIC_VISIBLE = 8
    private const val FIT_MARKERS = 9
    private const val GET_SCREEN_POINTS = 10
    private const val GET_WORLD_POINTS = 11
  }

  override fun getName(): String = REACT_CLASS

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> = mapOf()

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> = mapOf(
    "routes" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onRouteFound")),
    "cameraPosition" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onCameraPositionReceived")),
    "cameraPositionChange" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onCameraPositionChange")),
    "cameraPositionChangeEnd" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onCameraPositionChangeEnd")),
    "visibleRegion" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onVisibleRegionReceived")),
    "onMapPress" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onMapPress")),
    "onMapLongPress" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onMapLongPress")),
    "onMapLoaded" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onMapLoaded")),
    "screenToWorldPoints" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onScreenToWorldPointsReceived")),
    "worldToScreenPoints" to mapOf("phasedRegistrationNames" to mapOf("bubbled" to "onWorldToScreenPointsReceived"))
  )

  override fun getCommandsMap(): Map<String, Int> = mapOf(
    "setCenter" to SET_CENTER,
    "setBounds" to SET_BOUNDS,
    "fitAllMarkers" to FIT_ALL_MARKERS,
    "findRoutes" to FIND_ROUTES,
    "setZoom" to SET_ZOOM,
    "getCameraPosition" to GET_CAMERA_POSITION,
    "getVisibleRegion" to GET_VISIBLE_REGION,
    "setTrafficVisible" to SET_TRAFFIC_VISIBLE,
    "fitMarkers" to FIT_MARKERS,
    "getScreenPoints" to GET_SCREEN_POINTS,
    "getWorldPoints" to GET_WORLD_POINTS
  )

  override fun receiveCommand(
    @NonNull view: YamapView,
    commandType: String,
    args: ReadableArray?
  ) {
    Assertions.assertNotNull(view)
    Assertions.assertNotNull(args)

    when (commandType) {
      "setCenter" -> setCenter(view, args!!.getMap(0), args.getDouble(1).toFloat(), args.getDouble(2).toFloat(), args.getDouble(3).toFloat(), args.getDouble(4).toFloat(), args.getInt(5))
      "setBounds" -> setBounds(view, args!!.getMap(0), args.getMap(1), args.getDouble(2).toFloat(), args.getDouble(3).toFloat(), args.getInt(4))
      "fitAllMarkers" -> fitAllMarkers(view)
      "fitMarkers" -> args?.let { fitMarkers(view, it.getArray(0)) }
      "findRoutes" -> args?.let { findRoutes(view, it.getArray(0), it.getArray(1), it.getString(2)) }
      "setZoom" -> args?.let { view.setZoom(it.getDouble(0).toFloat(), it.getDouble(1).toFloat(), it.getInt(2)) }
      "getCameraPosition" -> args?.let { view.emitCameraPositionToJS(it.getString(0)) }
      "getVisibleRegion" -> args?.let { view.emitVisibleRegionToJS(it.getString(0)) }
      "setTrafficVisible" -> args?.let { view.setTrafficVisible(it.getBoolean(0)) }
      "getScreenPoints" -> args?.let { view.emitWorldToScreenPoints(it.getArray(0)!!, it.getString(1)) }
      "getWorldPoints" -> args?.let { view.emitScreenToWorldPoints(it.getArray(0)!!, it.getString(1)) }
      else -> throw IllegalArgumentException("Unsupported command $commandType received by ${javaClass.simpleName}.")
    }
  }

  @NonNull
  override fun createViewInstance(@NonNull context: ThemedReactContext): YamapView {
    val view = YamapView(context)
    MapKitFactory.getInstance().onStart()
    view.onStart()
    return view
  }

  private fun setCenter(view: YamapView, center: ReadableMap?, zoom: Float, azimuth: Float, tilt: Float, duration: Float, animation: Int) {
    center?.let {
      val centerPosition = Point(it.getDouble("lat"), it.getDouble("lon"))
      val pos = CameraPosition(centerPosition, zoom, azimuth, tilt)
      view.setCenter(pos, duration, animation)
    }
  }

  private fun setBounds(view: YamapView, bottomLeft: ReadableMap?, topRight: ReadableMap?, offset: Float, duration: Float, animation: Int) {
    view.setBounds(
      Point(bottomLeft!!.getDouble("lat"), bottomLeft.getDouble("lon")),
      Point(topRight!!.getDouble("lat"), topRight.getDouble("lon")),
      offset,
      duration,
      animation
    )
  }

  private fun fitAllMarkers(view: YamapView) {
    view.fitAllMarkers()
  }

  private fun fitMarkers(view: YamapView, jsPoints: ReadableArray?) {
    jsPoints?.let {
      val points = ArrayList<Point?>()
      for (i in 0 until it.size()) {
        it.getMap(i)?.let { point ->
          points.add(Point(point.getDouble("lat"), point.getDouble("lon")))
        }
      }
      view.fitMarkers(points)
    }
  }

  private fun findRoutes(view: YamapView, jsPoints: ReadableArray?, jsVehicles: ReadableArray?, id: String?) {
    jsPoints?.let {
      val points = ArrayList<Point?>()
      for (i in 0 until it.size()) {
        it.getMap(i)?.let { point ->
          points.add(Point(point.getDouble("lat"), point.getDouble("lon")))
        }
      }
      val vehicles = ArrayList<String>()
      jsVehicles?.let { vehiclesArray ->
        for (i in 0 until vehiclesArray.size()) {
          vehicles.add(vehiclesArray.getString(i)!!)
        }
      }
      view.findRoutes(points, vehicles, id)
    }
  }

  @ReactProp(name = "userLocationIcon")
  fun setUserLocationIcon(view: YamapView, icon: String?) {
    icon?.let { view.setUserLocationIcon(it) }
  }

  @ReactProp(name = "withClusters")
  fun setClusters(view: YamapView, with: Boolean?) {
    view.setClusters(with!!)
  }

  @ReactProp(name = "clusterColor")
  fun setClusterColor(view: YamapView, color: Int) {
    view.setClustersColor(color)
  }

  @ReactProp(name = "userLocationAccuracyFillColor")
  fun setUserLocationAccuracyFillColor(view: YamapView, color: Int) {
    view.setUserLocationAccuracyFillColor(color)
  }

  @ReactProp(name = "userLocationAccuracyStrokeColor")
  fun setUserLocationAccuracyStrokeColor(view: YamapView, color: Int) {
    view.setUserLocationAccuracyStrokeColor(color)
  }

  @ReactProp(name = "userLocationAccuracyStrokeWidth")
  fun setUserLocationAccuracyStrokeWidth(view: YamapView, width: Float) {
    view.setUserLocationAccuracyStrokeWidth(width)
  }

  @ReactProp(name = "showUserPosition")
  fun setShowUserPosition(view: YamapView, show: Boolean?) {
    view.setShowUserPosition(show!!)
  }

  @ReactProp(name = "nightMode")
  fun setNightMode(view: YamapView, nightMode: Boolean?) {
    view.setNightMode(nightMode ?: false)
  }

  @ReactProp(name = "scrollGesturesEnabled")
  fun setScrollGesturesEnabled(view: YamapView, scrollGesturesEnabled: Boolean?) {
    view.setScrollGesturesEnabled(scrollGesturesEnabled == true)
  }

  @ReactProp(name = "rotateGesturesEnabled")
  fun setRotateGesturesEnabled(view: YamapView, rotateGesturesEnabled: Boolean?) {
    view.setRotateGesturesEnabled(rotateGesturesEnabled == true)
  }

  @ReactProp(name = "zoomGesturesEnabled")
  fun setZoomGesturesEnabled(view: YamapView, zoomGesturesEnabled: Boolean?) {
    view.setZoomGesturesEnabled(zoomGesturesEnabled == true)
  }

  @ReactProp(name = "tiltGesturesEnabled")
  fun setTiltGesturesEnabled(view: YamapView, tiltGesturesEnabled: Boolean?) {
    view.setTiltGesturesEnabled(tiltGesturesEnabled == true)
  }

  @ReactProp(name = "fastTapEnabled")
  fun setFastTapEnabled(view: YamapView, fastTapEnabled: Boolean?) {
    view.setFastTapEnabled(fastTapEnabled == true)
  }

  @ReactProp(name = "mapStyle")
  fun setMapStyle(view: YamapView, style: String?) {
    style?.let { view.setMapStyle(it) }
  }

  @ReactProp(name = "mapType")
  fun setMapType(view: YamapView, type: String?) {
    type?.let { view.setMapType(it) }
  }

  @ReactProp(name = "initialRegion")
  fun setInitialRegion(view: YamapView, params: ReadableMap?) {
    params?.let { view.setInitialRegion(it) }
  }

  @ReactProp(name = "maxFps")
  fun setMaxFps(view: YamapView, maxFps: Float) {
    view.setMaxFps(maxFps)
  }

  @ReactProp(name = "interactive")
  fun setInteractive(view: YamapView, interactive: Boolean) {
    view.setInteractive(interactive)
  }

  override fun addView(parent: YamapView, child: View, index: Int) {
    parent.subviews.add(index, child)
    parent.addFeature(child)
  }

  override fun removeViewAt(parent: YamapView, index: Int) {
    val child = parent.subviews[index]
    parent.subviews.removeAt(index)
    parent.removeChild(child)
  }

  override fun getChildCount(parent: YamapView): Int {
    return parent.subviews.size
  }

  override fun getChildAt(parent: YamapView, index: Int): View? {
    if (index >= parent.subviews.size ) return null
    return parent.subviews[index]
  }
}
