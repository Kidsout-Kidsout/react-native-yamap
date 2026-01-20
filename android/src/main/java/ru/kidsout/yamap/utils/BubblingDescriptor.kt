package ru.kidsout.yamap.utils

class BubblingDescriptor {
  companion object {
    fun create(event: String) {
      mapOf(
        "phasedRegistrationNames" to
            mapOf(
                "bubbled" to event,
                "captured" to "${event}Capture"
            )
      )
    }
  }
}
