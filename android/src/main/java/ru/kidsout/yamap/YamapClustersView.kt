package ru.kidsout.yamap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.viewmanagers.YamapClustersViewManagerInterface
import com.yandex.mapkit.map.Cluster
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.ImageProvider
import ru.kidsout.yamap.events.YamapClustersViewOnPressEvent
import ru.kidsout.yamap.types.YamapClusterStyle
import ru.kidsout.yamap.types.YamapClustersViewProps
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

class YamapClustersView : FrameLayout, YamapClustersViewManagerInterface<YamapClustersView>, ClusterListener, ClusterTapListener {
  private var collection: MapObjectCollection? = null
  private var clusterCollection: ClusterizedPlacemarkCollection? = null
  private val props = YamapClustersViewProps()
  private val markerChildren = mutableMapOf<Int, View>()
  private val handler = Handler(Looper.getMainLooper())
  private var timerRunning = false
  private var needsUpdate = false

  private val updateRunnable = object : Runnable {
    override fun run() {
      if (!timerRunning) return
      if (needsUpdate) {
        performClustering()
        needsUpdate = false
      }
      handler.postDelayed(this, UPDATE_INTERVAL_MS)
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

  fun setCollection(col: MapObjectCollection) {
    collection = col
    clusterCollection?.clear()
    clusterCollection = col.addClusterizedPlacemarkCollection(this)
    performClustering()
  }

  fun unmount() {
    stopTimer()
    markerChildren.forEach { (it.value as? YamapMarkerView)?.unmount() }
    markerChildren.clear()
    clusterCollection?.clear()
    clusterCollection = null
    collection = null
  }

  override fun setRadius(view: YamapClustersView?, value: Double) {
    props.radius = value
    performClustering()
  }

  override fun setMinZoom(view: YamapClustersView?, value: Double) {
    props.minZoom = value
    performClustering()
  }

  override fun setClusterStyle(view: YamapClustersView?, value: ReadableMap?) {
    val styleMap = value ?: return
    updateStyle(styleMap)
    performClustering()
  }

  fun addSubview(child: View, index: Int) {
    Log.i(TAG, "Add subview at $index")
    if (child is YamapMarkerView) {
      val col = clusterCollection ?: return
      markerChildren[index] = child
      child.setCollection(col)
      markNeedsUpdate()
      return
    }

    return addView(child, index)
  }

  fun removeSubviewAt(index: Int) {
    Log.i(TAG, "Remove subview at $index")
    (markerChildren[index] as? YamapMarkerView)?.let {
      markerChildren.remove(index)
      it.unmount()
      markNeedsUpdate()
      return
    }

    return removeViewAt(index)
  }

  fun getSubviewAt(index: Int): View? {
    Log.i(TAG, "Get subview at $index")
    markerChildren[index]?.let {
      return it
    }

    return getChildAt(index)
  }

  fun getSubviewCount(): Int {
    return markerChildren.size + childCount
  }

  override fun onClusterAdded(cluster: Cluster) {
    val bitmap = createClusterBitmap(cluster.size)
    cluster.appearance.setIcon(ImageProvider.fromBitmap(bitmap))
    cluster.addClusterTapListener(this)
  }

  override fun onClusterTap(cluster: Cluster): Boolean {
    val ids = cluster.placemarks.mapNotNull { placemark ->
      val data = placemark.userData as? String
      if (data.isNullOrEmpty()) null else data
    }
    dispatchOnPress(ids)
    return true
  }

  private fun performClustering() {
    val cluster = clusterCollection ?: return
    cluster.clusterPlacemarks(props.radius, props.minZoom.toInt())
  }

  private fun markNeedsUpdate() {
    needsUpdate = true
  }

  private fun updateStyle(map: ReadableMap) {
    val style: YamapClusterStyle = props.clusterStyle
    if (map.hasKey("fontSize") && !map.isNull("fontSize")) {
      style.fontSize = map.getDouble("fontSize").toFloat()
    }
    if (map.hasKey("fontColor") && !map.isNull("fontColor")) {
      style.fontColor = map.getInt("fontColor")
    }
    if (map.hasKey("fillColor") && !map.isNull("fillColor")) {
      style.fillColor = map.getInt("fillColor")
    }
    if (map.hasKey("strokeColor") && !map.isNull("strokeColor")) {
      style.strokeColor = map.getInt("strokeColor")
    }
    if (map.hasKey("strokeWidth") && !map.isNull("strokeWidth")) {
      style.strokeWidth = map.getDouble("strokeWidth").toFloat()
    }
    if (map.hasKey("padding") && !map.isNull("padding")) {
      style.padding = map.getDouble("padding").toFloat()
    }
  }

  private fun createClusterBitmap(clusterSize: Int): Bitmap {
    val style = props.clusterStyle
    val density = resources.displayMetrics.density
    val fontSizePx = max(style.fontSize * density, 1f)
    val paddingPx = max(style.padding * density, 0f)
    val strokePx = max(style.strokeWidth * density, 0f)
    val text = clusterSize.toString()

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      color = style.fontColor
      textSize = fontSizePx
      textAlign = Paint.Align.CENTER
    }

    val bounds = Rect()
    textPaint.getTextBounds(text, 0, text.length, bounds)
    val textWidth = textPaint.measureText(text)
    val textHeight = max(bounds.height().toFloat(), fontSizePx)
    val textRadius = sqrt(textWidth * textWidth + textHeight * textHeight) / 2f
    val internalRadius = textRadius + paddingPx
    val externalRadius = internalRadius + strokePx
    val sizePx = max((externalRadius * 2f).roundToInt(), 1)

    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val center = externalRadius

    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      color = style.strokeColor
      this.style = Paint.Style.FILL
    }
    canvas.drawCircle(center, center, externalRadius, strokePaint)

    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      color = style.fillColor
      this.style = Paint.Style.FILL
    }
    canvas.drawCircle(center, center, internalRadius, fillPaint)

    val metrics = textPaint.fontMetrics
    val baseline = center - (metrics.ascent + metrics.descent) / 2f
    canvas.drawText(text, center, baseline, textPaint)

    return bitmap
  }

  private fun dispatchOnPress(ids: List<String>) {
    val dispatcher = getEventEmitter()
    val surfaceId = getSurfaceId()
    dispatcher.dispatchEvent(YamapClustersViewOnPressEvent(surfaceId, id, ids))
  }

  private fun getEventEmitter(): EventDispatcher {
    return UIManagerHelper.getEventDispatcherForReactTag(context as ReactContext, id)!!
  }

  private fun getSurfaceId(): Int {
    return UIManagerHelper.getSurfaceId(context)
  }

  private fun startTimer() {
    if (timerRunning) return
    timerRunning = true
    handler.postDelayed(updateRunnable, UPDATE_INTERVAL_MS)
  }

  private fun stopTimer() {
    if (!timerRunning) return
    timerRunning = false
    handler.removeCallbacks(updateRunnable)
  }

  private fun configureComponent() {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    startTimer()
  }

  companion object {
    private const val UPDATE_INTERVAL_MS = 1000L
    private const val TAG = "YamapClustersView"
  }
}
