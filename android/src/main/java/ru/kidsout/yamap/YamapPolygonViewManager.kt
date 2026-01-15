package ru.kidsout.yamap

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.YamapPolygonViewManagerDelegate
import com.facebook.react.viewmanagers.YamapPolygonViewManagerInterface
import ru.kidsout.yamap.utils.BubblingDescriptor
import ru.kidsout.yamap.events.YamapPolygonViewOnPressEvent

@ReactModule(name = YamapPolygonViewManager.NAME)
class YamapPolygonViewManager(context: ReactApplicationContext) : SimpleViewManager<YamapPolygonView>(), YamapPolygonViewManagerInterface<YamapPolygonView> {
  private val delegate: YamapPolygonViewManagerDelegate<YamapPolygonView, YamapPolygonViewManager> =
    YamapPolygonViewManagerDelegate(this)

  override fun getDelegate(): ViewManagerDelegate<YamapPolygonView> = delegate

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): YamapPolygonView = YamapPolygonView(context)

  override fun setPoints(view: YamapPolygonView?, value: ReadableArray?) {
    view?.setPoints(view, value)
  }

  override fun setInnerRings(view: YamapPolygonView?, value: ReadableArray?) {
    view?.setInnerRings(view, value)
  }

  override fun setLIndex(view: YamapPolygonView?, value: Int) {
    view?.setLIndex(view, value)
  }

  override fun setStyling(view: YamapPolygonView?, value: ReadableMap?) {
    view?.setStyling(view, value)
  }

  companion object {
    const val NAME = "YamapPolygonView"
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> =
    mapOf(
      YamapPolygonViewOnPressEvent.EVENT_NAME to BubblingDescriptor.create(YamapPolygonViewOnPressEvent.EVENT_NAME)
    )
}
