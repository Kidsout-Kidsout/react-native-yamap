package ru.kidsout.yamap

import android.view.View
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.IViewGroupManager
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.YamapViewManagerDelegate
import com.facebook.react.viewmanagers.YamapViewManagerInterface
import ru.kidsout.yamap.events.YamapViewOnMapLoadedEvent
import ru.kidsout.yamap.events.YamapViewOnMapPressEvent
import ru.kidsout.yamap.events.YamapViewOnMapLongPressEvent
import ru.kidsout.yamap.events.YamapViewOnCameraPositionChangeEvent
import ru.kidsout.yamap.events.YamapViewOnCommandSetCenterReceivedEvent
import ru.kidsout.yamap.events.YamapViewOnCommandSetBoundsReceivedEvent
import ru.kidsout.yamap.events.YamapViewOnCommandSetZoomReceivedEvent
import ru.kidsout.yamap.events.YamapViewOnCommandGetCameraPositionReceivedEvent
import ru.kidsout.yamap.events.YamapViewOnCommandGetVisibleRegionReceivedEvent
import ru.kidsout.yamap.types.YamapViewProps
import ru.kidsout.yamap.utils.BubblingDescriptor
import ru.kidsout.yamap.utils.DirectDescriptor

@ReactModule(name = YamapViewManager.NAME)
class YamapViewManager(context: ReactApplicationContext) : SimpleViewManager<YamapView>(), IViewGroupManager<YamapView>, YamapViewManagerInterface<YamapView> {
  private val delegate: YamapViewManagerDelegate<YamapView, YamapViewManager> =
    YamapViewManagerDelegate(this)

  override fun getDelegate(): ViewManagerDelegate<YamapView> = delegate

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): YamapView = YamapView(context)

  override fun setNightMode(view: YamapView?, value: Boolean) {
    view?.setNightMode(view, value)
  }
  override fun setMapType(view: YamapView?, value: String?) {
    view?.setMapType(view, value)
  }
  override fun setScrollGesturesEnabled(view: YamapView?, value: Boolean) {
    view?.setScrollGesturesEnabled(view, value)
  }
  override fun setZoomGesturesEnabled(view: YamapView?, value: Boolean) {
    view?.setZoomGesturesEnabled(view, value)
  }
  override fun setTiltGesturesEnabled(view: YamapView?, value: Boolean) {
    view?.setTiltGesturesEnabled(view, value)
  }
  override fun setRotateGesturesEnabled(view: YamapView?, value: Boolean) {
    view?.setRotateGesturesEnabled(view, value)
  }
  override fun setFastTapEnabled(view: YamapView?, value: Boolean) {
    view?.setFastTapEnabled(view, value)
  }
  override fun setMaxFps(view: YamapView?, value: Int) {
    view?.setMaxFps(view, value)
  }

  override fun addView(parent: YamapView, child: View, index: Int) {
    return parent.addSubview(child, index)
  }

  override fun removeViewAt(parent: YamapView, index: Int) {
    return parent.removeSubviewAt(index)
  }

  override fun getChildAt(parent: YamapView, index: Int): View? {
    return parent.getSubviewAt(index)
  }

  override fun getChildCount(parent: YamapView): Int {
    return parent.getSubviewCount()
  }

  override fun needsCustomLayoutForChildren(): Boolean {
    return false
  }

  override fun commandSetCenter(view: YamapView?, cid: String?, lat: Double, lon: Double, zoom: Double, azimuth: Double, tilt: Double, offset: Double, animationType: Int, animationDuration: Double) {
    view?.commandSetCenter(view, cid, lat, lon, zoom, azimuth, tilt, offset, animationType, animationDuration)
  }

  override fun commandSetBounds(view: YamapView?, cid: String?, bottomLeftPointLat: Double, bottomLeftPointLon: Double, topRightPointLat: Double, topRightPointLon: Double, minZoom: Double, maxZoom: Double, offset: Double, animationType: Int, animationDuration: Double) {
    view?.commandSetBounds(view, cid, bottomLeftPointLat, bottomLeftPointLon, topRightPointLat, topRightPointLon, minZoom, maxZoom, offset, animationType, animationDuration)
  }

  override fun commandSetZoom(view: YamapView?, cid: String?, zoom: Double, offset: Double, animationType: Int, animationDuration: Double) {
    view?.commandSetZoom(view, cid, zoom, offset, animationType, animationDuration)
  }

  override fun commandGetCameraPosition(view: YamapView?, cid: String?) {
    view?.commandGetCameraPosition(view, cid)
  }

  override fun commandGetVisibleRegion(view: YamapView?, cid: String?) {
    view?.commandGetVisibleRegion(view, cid)
  }

  companion object {
    const val NAME= "YamapView"
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> =
    mapOf(
      YamapViewOnMapLoadedEvent.EVENT_NAME to BubblingDescriptor.create(YamapViewOnMapLoadedEvent.EVENT_NAME),
      YamapViewOnMapPressEvent.EVENT_NAME to BubblingDescriptor.create(YamapViewOnMapPressEvent.EVENT_NAME),
      YamapViewOnMapLongPressEvent.EVENT_NAME to BubblingDescriptor.create(YamapViewOnMapLongPressEvent.EVENT_NAME),
      YamapViewOnCameraPositionChangeEvent.EVENT_NAME to BubblingDescriptor.create(YamapViewOnCameraPositionChangeEvent.EVENT_NAME),
    )

  override fun getExportedCustomDirectEventTypeConstants(): Map<String?, Any?> = mapOf(
      YamapViewOnCommandSetCenterReceivedEvent.EVENT_NAME to DirectDescriptor.create(YamapViewOnCommandSetCenterReceivedEvent.EVENT_NAME),
      YamapViewOnCommandSetBoundsReceivedEvent.EVENT_NAME to DirectDescriptor.create(YamapViewOnCommandSetBoundsReceivedEvent.EVENT_NAME),
      YamapViewOnCommandSetZoomReceivedEvent.EVENT_NAME to DirectDescriptor.create(YamapViewOnCommandSetZoomReceivedEvent.EVENT_NAME),
      YamapViewOnCommandGetCameraPositionReceivedEvent.EVENT_NAME to DirectDescriptor.create(YamapViewOnCommandGetCameraPositionReceivedEvent.EVENT_NAME),
      YamapViewOnCommandGetVisibleRegionReceivedEvent.EVENT_NAME to DirectDescriptor.create(YamapViewOnCommandGetVisibleRegionReceivedEvent.EVENT_NAME)
    )

}
