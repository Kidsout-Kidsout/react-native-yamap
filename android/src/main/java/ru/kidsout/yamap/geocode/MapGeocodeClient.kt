package ru.kidsout.yamap.geocode

import com.yandex.mapkit.GeoObject
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

class MapGeocodeClient() {
  private val searchManager: SearchManager by lazy {
    SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
  }
  private val searchOptions = SearchOptions()

  private val defaultGeometry = BoundingBox(
    Point(-89.999999, -179.999999),
    Point(89.999999, 179.999999)
  )

  init {
    searchOptions.searchTypes = SearchType.GEO.value
    searchOptions.geometry = true
    searchOptions.disableSpellingCorrection = true
  }

  fun geocode(text: String, onSuccess: (MapGeocodeItem) -> Unit, onError: (Throwable) -> Unit) {
    searchManager.submit(
      text,
      Geometry.fromBoundingBox(defaultGeometry),
      searchOptions,
      object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
          extract(response)?.let { onSuccess(it) }
        }
        override fun onSearchError(error: Error) {
          onError(IllegalStateException("Suggest error: $error"))
        }
      }
    )
  }

  fun geocodeUri(uri: String, onSuccess: (MapGeocodeItem) -> Unit, onError: (Throwable) -> Unit) {
    searchManager.resolveURI(
      uri,
      searchOptions,
      object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
          extract(response)?.let { onSuccess(it) }
        }

        override fun onSearchError(error: Error) {
          onError(IllegalStateException("Suggest error: $error"))
        }
      }
    )
  }

  fun geocodePoint(point: Point, onSuccess: (MapGeocodeItem) -> Unit, onError: (Throwable) -> Unit) {
    searchManager.submit(
      point,
      0,
      searchOptions,
      object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
          extract(response)?.let { onSuccess(it) }
        }

        override fun onSearchError( error: Error) {
          onError(IllegalStateException("Suggest error: $error"))
        }
      }
    )
  }

  private fun extract(response: Response): MapGeocodeItem? {
    var geoObject: GeoObject? = null
    var point: Point? = null
    var address: Address? = null
    var box: BoundingBox? = null

    val children = response.collection.children
    for (item in children) {
      val pGeoObject = item.obj ?: continue
      val pMeta = pGeoObject.metadataContainer.getItem(ToponymObjectMetadata::class.java) ?: continue

      var pPoint: Point? = null
      val geometryList = pGeoObject.geometry
      for (geometry in geometryList) {
        val pp = geometry.point
        if (pp != null) {
          pPoint = pp
          break
        }
      }

      if (pPoint == null) continue

      val pBox = pGeoObject.boundingBox ?: continue

      geoObject = pGeoObject
      address = pMeta.address
      point = pPoint
      box = pBox
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
