package ru.kidsout.yamap

import android.view.View
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.IViewGroupManager
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.YamapMarkerViewManagerDelegate
import com.facebook.react.viewmanagers.YamapMarkerViewManagerInterface
import ru.kidsout.yamap.events.YamapMarkerViewOnPressEvent
import ru.kidsout.yamap.utils.BubblingDescriptor

@ReactModule(name = YamapMarkerViewManager.NAME)
class YamapMarkerViewManager(context: ReactApplicationContext) : SimpleViewManager<YamapMarkerView>(), IViewGroupManager<YamapMarkerView>, YamapMarkerViewManagerInterface<YamapMarkerView> {
  private val delegate: ViewManagerDelegate<YamapMarkerView> =
    YamapMarkerViewManagerDelegate(this)

  override fun getDelegate(): ViewManagerDelegate<YamapMarkerView> = delegate

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): YamapMarkerView = YamapMarkerView(context)

  override fun setId(view: YamapMarkerView?, value: String?) {
    view?.setId(view, value)
  }

  override fun setText(view: YamapMarkerView?, value: String?) {
    view?.setText(view, value)
  }

  override fun setStyling(view: YamapMarkerView?, value: ReadableMap?) {
    view?.setStyling(view, value)
  }

  override fun setLIndex(view: YamapMarkerView?, value: Int) {
    view?.setLIndex(view, value)
  }

  override fun setCenter(view: YamapMarkerView?, value: ReadableMap?) {
    view?.setCenter(view, value)
  }

  override fun addView(parent: YamapMarkerView, child: View, index: Int) {
    if (index > 0) return
    parent.addMarker(child)
  }

  override fun getChildAt(parent: YamapMarkerView, index: Int): View? {
    if (index > 0) return null
    return parent.markerChild
  }

  override fun getChildCount(parent: YamapMarkerView): Int {
    return if (parent.markerChild != null) 1 else 0
  }

  override fun removeViewAt(parent: YamapMarkerView, index: Int) {
    if (index > 0) return
    parent.removeMarker()
  }

  override fun needsCustomLayoutForChildren(): Boolean {
    return false
  }

  companion object {
    const val NAME = "YamapMarkerView"
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> =
    mapOf(
      YamapMarkerViewOnPressEvent.EVENT_NAME to BubblingDescriptor.create(YamapMarkerViewOnPressEvent.EVENT_NAME)
    )
}
