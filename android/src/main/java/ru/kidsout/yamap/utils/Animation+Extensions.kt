package ru.kidsout.yamap.utils

import com.yandex.mapkit.Animation

fun animationFromProps(animationType: Int, duration: Float): Animation {
  return Animation(if (animationType == 1) Animation.Type.SMOOTH else Animation.Type.LINEAR, duration)
}

fun animationFromProps(animationType: Int, duration: Double): Animation {
  return animationFromProps(animationType, duration.toFloat())
}
