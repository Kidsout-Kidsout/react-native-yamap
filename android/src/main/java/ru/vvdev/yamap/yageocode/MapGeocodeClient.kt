package ru.vvdev.yamap.yageocode

import com.yandex.mapkit.geometry.Point
import ru.vvdev.yamap.utils.Callback

interface MapGeocodeClient {

  fun geocode(text: String, onSuccess: Callback<MapGeocodeItem>, onError: Callback<Throwable>)

  fun geocodeUri(uri: String, onSuccess: Callback<MapGeocodeItem>, onError: Callback<Throwable>)

  fun geocodePoint(point: Point, onSuccess: Callback<MapGeocodeItem>, onError: Callback<Throwable>)
}
