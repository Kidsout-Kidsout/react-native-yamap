package ru.kidsout.yamap

import android.view.View
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.IViewGroupManager
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.YamapClustersViewManagerDelegate
import com.facebook.react.viewmanagers.YamapClustersViewManagerInterface
import ru.kidsout.yamap.events.YamapClustersViewOnPressEvent
import ru.kidsout.yamap.utils.BubblingDescriptor

@ReactModule(name = YamapClustersViewManager.NAME)
class YamapClustersViewManager(context: ReactApplicationContext) : SimpleViewManager<YamapClustersView>(), IViewGroupManager<YamapClustersView>, YamapClustersViewManagerInterface<YamapClustersView> {
  private val delegate: ViewManagerDelegate<YamapClustersView> =
    YamapClustersViewManagerDelegate(this)

  override fun getDelegate(): ViewManagerDelegate<YamapClustersView> = delegate

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): YamapClustersView = YamapClustersView(context)

  override fun setRadius(view: YamapClustersView?, value: Double) {
    view?.setRadius(view, value)
  }

  override fun setMinZoom(view: YamapClustersView?, value: Double) {
    view?.setMinZoom(view, value)
  }

  override fun setClusterStyle(view: YamapClustersView?, value: ReadableMap?) {
    view?.setClusterStyle(view, value)
  }

  override fun addView(parent: YamapClustersView, child: View, index: Int) {
    parent.addSubview(child, index)
  }

  override fun getChildAt(parent: YamapClustersView, index: Int): View? {
    return parent.getSubviewAt(index)
  }

  override fun getChildCount(parent: YamapClustersView): Int {
    return parent.getSubviewCount()
  }

  override fun removeViewAt(parent: YamapClustersView, index: Int) {
    parent.removeSubviewAt(index)
  }

  override fun needsCustomLayoutForChildren(): Boolean {
    return false
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> =
    mapOf(
      YamapClustersViewOnPressEvent.EVENT_NAME to BubblingDescriptor.create(YamapClustersViewOnPressEvent.EVENT_NAME)
    )

  companion object {
    const val NAME = "YamapClustersView"
  }
}
