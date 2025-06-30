package ru.vvdev.yamap.yageocode

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.GeoObjectCollection
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Address
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error
import ru.vvdev.yamap.utils.Callback

class YandexMapGeocodeClient(context: Context) : MapGeocodeClient {

  private val searchManager: SearchManager
  private val searchOptions = SearchOptions()

  private val defaultGeometry = BoundingBox(
    Point(-89.999999, -179.999999),
    Point(89.999999, 179.999999)
  )

  init {
    SearchFactory.initialize(context)
    searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    searchOptions.searchTypes = SearchType.GEO.value
    searchOptions.isGeometry = true
    searchOptions.isDisableSpellingCorrection = true
  }

  override fun geocode(text: String, onSuccess: Callback<MapGeocodeItem>, onError: Callback<Throwable>) {
    searchManager.submit(
      text,
      Geometry.fromBoundingBox(defaultGeometry),
      searchOptions,
      object : Session.SearchListener {
        override fun onSearchResponse(@NonNull response: Response) {
          onSuccess.invoke(extract(response))
        }

        override fun onSearchError(@NonNull error: Error) {
          onError.invoke(IllegalStateException("Suggest error: $error"))
        }
      }
    )
  }

  override fun geocodeUri(uri: String, onSuccess: Callback<MapGeocodeItem>, onError: Callback<Throwable>) {
    searchManager.resolveURI(
      uri,
      searchOptions,
      object : Session.SearchListener {
        override fun onSearchResponse(@NonNull response: Response) {
          onSuccess.invoke(extract(response))
        }

        override fun onSearchError(@NonNull error: Error) {
          onError.invoke(IllegalStateException("Suggest error: $error"))
        }
      }
    )
  }

  override fun geocodePoint(point: Point, onSuccess: Callback<MapGeocodeItem>, onError: Callback<Throwable>) {
    searchManager.submit(
      point,
      0,
      searchOptions,
      object : Session.SearchListener {
        override fun onSearchResponse(@NonNull response: Response) {
          onSuccess.invoke(extract(response))
        }

        override fun onSearchError(@NonNull error: Error) {
          onError.invoke(IllegalStateException("Suggest error: $error"))
        }
      }
    )
  }

  @Nullable
  private fun extract(response: Response): MapGeocodeItem? {
    var geoObject: GeoObject? = null
    var point: Point? = null
    var address: Address? = null
    var box: BoundingBox? = null

    val children = response.collection.children
    for (item in children) {
      val _geoObject = item.obj ?: continue
      val _meta = _geoObject.metadataContainer.getItem(ToponymObjectMetadata::class.java) ?: continue

      var _point: Point? = null
      val geometryList = _geoObject.geometry
      for (geometry in geometryList) {
        val pp = geometry.point
        if (pp != null) {
          _point = pp
          break
        }
      }

      if (_point == null) continue

      val _box = _geoObject.boundingBox ?: continue

      geoObject = _geoObject
      address = _meta.address
      point = _point
      box = _box
      break
    }

    if (geoObject == null) return null

    return MapGeocodeItem().apply {
      name = geoObject.name
      descriptionText = geoObject.descriptionText
      formattedAddress = address?.formattedAddress
      coords = point
      upperCorner = box?.northEast
      lowerCorner = box?.southWest
      components = address?.components
    }
  }
}
