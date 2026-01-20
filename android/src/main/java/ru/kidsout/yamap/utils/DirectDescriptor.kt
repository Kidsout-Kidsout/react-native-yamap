package ru.kidsout.yamap.utils

class DirectDescriptor {
  companion object {
    fun create(event: String): Map<String, String> {
      return mapOf(
        "registrationName" to event
      )
    }
  }
}
