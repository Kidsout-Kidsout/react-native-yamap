package ru.vvdev.yamap.utils

import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.transport.masstransit.Route
import java.util.*

class RouteManager {
  private val data = mutableMapOf<String, Route>()
  private val mapObjects = mutableMapOf<String, ArrayList<MapObject>>()

  companion object {
    fun generateId(): String {
      return UUID.randomUUID().toString()
    }
  }

  fun saveRoute(route: Route, id: String) {
    data[id] = route
  }

  fun putRouteMapObjects(id: String, objects: ArrayList<MapObject>) {
    mapObjects[id] = objects
  }

  fun getRoute(id: String): Route? {
    return data[id]
  }

  fun getRouteMapObjects(id: String): ArrayList<MapObject>? {
    return mapObjects[id]
  }
}
