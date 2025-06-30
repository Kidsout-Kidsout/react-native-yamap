package ru.vvdev.yamap.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.NonNull
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.facebook.react.views.view.ReactViewGroup
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.RotationType
import com.yandex.runtime.image.ImageProvider
import ru.vvdev.yamap.models.ReactMapObject
import ru.vvdev.yamap.utils.Callback
import ru.vvdev.yamap.utils.ImageLoader

class YamapMarker(context: Context) : ReactViewGroup(context), MapObjectTapListener, ReactMapObject {
  var point: Point? = null
    set(point) {
      field = point
      updateMarker()
    }
  private var zIndex = 1
  private var scale = 1f
  private var visible = true
  private val YAMAP_FRAMES_PER_SECOND = 25
  private var markerAnchor: PointF? = null
  private var iconSource: String? = null
  private var _childView: View? = null
  override var mapObject: MapObject? = null
    set(obj) {
      field = obj
      obj?.addTapListener(this)
      updateMarker()
    }
  private val childs = ArrayList<View>()

  private val childLayoutListener = OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
    updateMarker()
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

  fun setZIndex(_zIndex: Int) {
    zIndex = _zIndex
    updateMarker()
  }

  fun setScale(_scale: Float) {
    scale = _scale
    updateMarker()
  }

  fun setVisible(_visible: Boolean) {
    visible = _visible
    updateMarker()
  }

  fun setIconSource(source: String) {
    iconSource = source
    updateMarker()
  }

  fun setAnchor(anchor: PointF) {
    markerAnchor = anchor
    updateMarker()
  }

  private fun updateMarker() {
    mapObject?.takeIf { it.isValid }?.let { placemark ->
      val iconStyle = IconStyle().apply {
        scale = this@YamapMarker.scale
        rotationType = RotationType.ROTATE
        visible = this@YamapMarker.visible
        markerAnchor?.let { anchor = it }
      }
      (placemark as PlacemarkMapObject?)?.apply {
        geometry = point!!
        zIndex = zIndex.toFloat()
        setIconStyle(iconStyle)
      }

      _childView?.let { childView ->
        try {
          val bitmap = Bitmap.createBitmap(childView.width, childView.height, Bitmap.Config.ARGB_8888)
          val canvas = Canvas(bitmap)
          childView.draw(canvas)
          placemark.setIcon(ImageProvider.fromBitmap(bitmap))
          placemark.setIconStyle(iconStyle)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }

      if (childs.isEmpty() && !iconSource.isNullOrEmpty()) {
        ImageLoader.downloadImageBitmap(context, iconSource!!, fun(bitmap) {
            try {
              placemark.setIcon(ImageProvider.fromBitmap(bitmap))
              placemark.setIconStyle(iconStyle)
            } catch (e: Exception) {
              e.printStackTrace()
            }
          })
      }
    }
  }

  fun setChildView(view: View?) {
    if (view == null) {
      _childView?.removeOnLayoutChangeListener(childLayoutListener)
      _childView = null
      updateMarker()
      return
    }
    _childView = view
    _childView?.addOnLayoutChangeListener(childLayoutListener)
  }

  fun addChildView(view: View, index: Int) {
    childs.add(index, view)
    setChildView(childs.firstOrNull())
  }

  fun removeChildView(index: Int) {
    childs.removeAt(index)
    setChildView(childs.firstOrNull())
  }

  fun moveAnimationLoop(lat: Double, lon: Double) {
    (mapObject as? PlacemarkMapObject)?.geometry = Point(lat, lon)
  }

  fun rotateAnimationLoop(delta: Float) {
    (mapObject as? PlacemarkMapObject)?.direction = delta
  }

  fun animatedMoveTo(point: Point, duration: Float) {
    val placemark = mapObject as? PlacemarkMapObject ?: return
    val startLat = placemark.geometry.latitude
    val startLon = placemark.geometry.longitude
    val deltaLat = point.latitude - startLat
    val deltaLon = point.longitude - startLon
    val valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
      this.duration = duration.toLong()
      interpolator = LinearInterpolator()
      addUpdateListener { animation ->
        try {
          val v = animation.animatedFraction
          moveAnimationLoop(startLat + v * deltaLat, startLon + v * deltaLon)
        } catch (ex: Exception) {
          // Ignored
        }
      }
    }
    valueAnimator.start()
  }

  fun animatedRotateTo(angle: Float, duration: Float) {
    val placemark = mapObject as? PlacemarkMapObject ?: return
    val startDirection = placemark.direction
    val delta = angle - startDirection
    val valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
      this.duration = duration.toLong()
      interpolator = LinearInterpolator()
      addUpdateListener { animation ->
        try {
          val v = animation.animatedFraction
          rotateAnimationLoop(startDirection + v * delta)
        } catch (ex: Exception) {
          // Ignored
        }
      }
    }
    valueAnimator.start()
  }

  override fun onMapObjectTap(@NonNull mapObject: MapObject, @NonNull point: Point): Boolean {
    val e = Arguments.createMap()
    (context as ReactContext).getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "onPress", e)
    return false
  }
}
