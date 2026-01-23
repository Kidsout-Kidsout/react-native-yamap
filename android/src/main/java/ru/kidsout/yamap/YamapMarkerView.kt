package ru.kidsout.yamap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.viewmanagers.YamapMarkerViewManagerInterface
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.BaseMapObjectCollection
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.runtime.image.ImageProvider
import ru.kidsout.yamap.events.YamapMarkerViewOnPressEvent
import ru.kidsout.yamap.types.YamapMarkerViewProps
import ru.kidsout.yamap.types.YamapMarkerViewPropsImage
import kotlin.math.roundToInt
import androidx.core.graphics.createBitmap
import com.yandex.runtime.ui_view.ViewProvider
import ru.kidsout.yamap.utils.ImageLoader

class YamapMarkerView : FrameLayout, YamapMarkerViewManagerInterface<YamapMarkerView> {
  private var collection: BaseMapObjectCollection? = null
  private var obj: PlacemarkMapObject? = null
  private var props = YamapMarkerViewProps()

  var markerChild: View? = null

  private val markerLayoutChangeListener = object : OnLayoutChangeListener {
    override fun onLayoutChange(
      v: View,
      left: Int,
      top: Int,
      right: Int,
      bottom: Int,
      oldLeft: Int,
      oldTop: Int,
      oldRight: Int,
      oldBottom: Int
    ) {
      v.removeOnLayoutChangeListener(this)
      val w = right - left
      val h = bottom - top
      Log.i(TAG, "Measured marker: w${w} h${h}")
      if (w > 0 && h > 0) {
        markerChild = v
        updatePlacemarkView()
      }
    }
  }

  constructor(context: Context) : super(context) {
    configureComponent()
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    configureComponent()
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    configureComponent()
  }

  fun setCollection(col: BaseMapObjectCollection) {
    collection = col
    obj = when (col) {
      is MapObjectCollection -> col.addPlacemark()
      is ClusterizedPlacemarkCollection -> col.addPlacemark()
      else -> throw Exception("Unsopported object collection passed")
    }
    obj?.addTapListener(tapListener)
    updateUserData()
    updatePlacemark()
  }

  fun unmount() {
    detachPlacemark()
    collection = null
  }

  private fun detachPlacemark() {
    obj?.removeTapListener(tapListener)
    collection?.let { col ->
      obj?.let { col.remove(it) }
    }
    obj = null
  }

  private val tapListener = MapObjectTapListener { _, _ ->
    val dispatcher = getEventEmitter()
    val surfaceId = getSurfaceId()
    val markerId = props.markerId
    dispatcher.dispatchEvent(YamapMarkerViewOnPressEvent(surfaceId, id, markerId))
    true
  }

  override fun setId(view: YamapMarkerView?, value: String?) {
    props.markerId = value ?: ""
    updateUserData()
  }

  override fun setText(view: YamapMarkerView?, value: String?) {
    props.text = value ?: ""
    updateText()
  }

  override fun setStyling(view: YamapMarkerView?, value: ReadableMap?) {
    val map = value ?: return
    if (map.hasKey("fontSize") && !map.isNull("fontSize")) {
      props.styling.fontSize = map.getDouble("fontSize").toFloat()
    }
    if (map.hasKey("fontColor") && !map.isNull("fontColor")) {
      props.styling.fontColor = map.getInt("fontColor")
    }
    updateText()
  }

  override fun setLIndex(view: YamapMarkerView?, value: Int) {
    props.lIndex = value
    updateGeometry()
  }

  override fun setCenter(view: YamapMarkerView?, value: ReadableMap?) {
    val map = value ?: return
    props.center = Point(map.getDouble("lat"), map.getDouble("lon"))
    updateGeometry()
  }

  override fun setImage(view: YamapMarkerView?, value: ReadableMap?) {
    when (value) {
      null -> {
        props.image = null
        updatePlacemarkView()
      }
      else -> {
        try {
          props.image = YamapMarkerViewPropsImage(
            value.getString("uri")!!,
            if (value.hasKey("scale")) value.getInt("scale") else 1,
            value.getInt("height"),
            value.getInt("width"),
          )
        } catch (_: Throwable) {
          props.image = null
        }
        updatePlacemarkView()
      }
    }
  }

  fun addMarker(view: View) {
    Log.i(TAG, "Add marker")
    ensureMeasured(view)
    view.addOnLayoutChangeListener(markerLayoutChangeListener)
  }

  fun removeMarker() {
    Log.i(TAG, "Remove marker")
    markerChild = null
    updatePlacemarkView()
  }

  private fun updatePlacemark() {
    updateGeometry()
    updateText()
  }

  private fun updateUserData() {
    obj?.userData = props.markerId
  }

  private fun updatePlacemarkView() {
    Log.i(TAG, "Updating marker")
    val obj = obj ?: return
    props.image?.let {
      Log.i(TAG, "Setting icon to ${it.uri}")
      ImageLoader.downloadImageBitmap(context, it.uri, {
        obj.setIcon(ImageProvider.fromBitmap(it))
      })
      return
    }
    val view = markerChild ?: createPlaceholder();
    val childName = when (markerChild) {
      null -> "Empty view"
      else -> "Some view"
    }
    Log.i(TAG, "Setting view to ${childName}")
    val bitmap = createBitmap(view.width, view.height)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    obj.setIcon(ImageProvider.fromBitmap(bitmap))
  }

  private fun updateGeometry() {
    val obj = obj ?: return
    obj.geometry = props.center
    obj.zIndex = props.lIndex.toFloat()
  }

  private fun updateText() {
    val obj = obj ?: return
    val style = TextStyle().apply {
      size = props.styling.fontSize
      color = props.styling.fontColor
      placement = TextStyle.Placement.CENTER
      offset = 0f
      textOptional = false
      offsetFromIcon = false
      outlineWidth = 0f
    }
    obj.setText(props.text, style)
  }

  private fun createPlaceholder(): View {
    val v = View(context)
    v.layoutParams = LayoutParams(dpToPx(10), dpToPx(10))
    ensureMeasured(v)
    return v
  }

  private fun ensureMeasured(view: View) {
    if (view.measuredWidth > 0 && view.measuredHeight > 0) return
    if (view.layoutParams == null) {
      view.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }
    val widthSpec = MeasureSpec.makeMeasureSpec(500, MeasureSpec.AT_MOST)
    val heightSpec = MeasureSpec.makeMeasureSpec(500, MeasureSpec.AT_MOST)
    view.measure(widthSpec, heightSpec)
    val measuredWidth = view.measuredWidth.coerceAtLeast(1)
    val measuredHeight = view.measuredHeight.coerceAtLeast(1)
    view.layout(0, 0, measuredWidth, measuredHeight)
  }

  private fun dpToPx(value: Int): Int {
    val density = context.resources.displayMetrics.density
    return (value * density).roundToInt().coerceAtLeast(1)
  }

  private fun getEventEmitter(): EventDispatcher {
    return UIManagerHelper.getEventDispatcherForReactTag(context as ReactContext, id)!!
  }

  private fun getSurfaceId(): Int {
    return UIManagerHelper.getSurfaceId(context)
  }

  private fun configureComponent() {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
  }

  companion object {
    private const val TAG = "YamapMarkerView"
  }
}
