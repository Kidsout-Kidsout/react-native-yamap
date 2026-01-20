package ru.kidsout.yamap

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.YamapCircleViewManagerDelegate
import com.facebook.react.viewmanagers.YamapCircleViewManagerInterface
import ru.kidsout.yamap.events.YamapCircleViewOnPressEvent
import ru.kidsout.yamap.types.YamapCircleViewProps
import ru.kidsout.yamap.utils.BubblingDescriptor

@ReactModule(name = YamapCircleViewManager.REACT_CLASS)
class YamapCircleViewManager(context: ReactApplicationContext) : SimpleViewManager<YamapCircleView>(), YamapCircleViewManagerInterface<YamapCircleView> {
  private var props = YamapCircleViewProps()
  private val delegate: YamapCircleViewManagerDelegate<YamapCircleView, YamapCircleViewManager> =
    YamapCircleViewManagerDelegate(this)

  override fun getDelegate(): ViewManagerDelegate<YamapCircleView> = delegate

  override fun getName(): String = REACT_CLASS

  override fun createViewInstance(context: ThemedReactContext): YamapCircleView = YamapCircleView(context)

  override fun setCenter(view: YamapCircleView?, value: ReadableMap?) {
    view?.setCenter(view, value)
  }

  override fun setLIndex(view: YamapCircleView?, value: Int) {
    view?.setLIndex(view, value)
  }

  override fun setRadius(view: YamapCircleView?, value: Double) {
    view?.setRadius(view, value)
  }

  override fun setStyling(view: YamapCircleView?, value: ReadableMap?) {
    view?.setStyling(view, value)
  }

  companion object {
    const val REACT_CLASS = "YamapCircleView"
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> =
    mapOf(
      YamapCircleViewOnPressEvent.EVENT_NAME to BubblingDescriptor.create(YamapCircleViewOnPressEvent.EVENT_NAME)
    )
}
