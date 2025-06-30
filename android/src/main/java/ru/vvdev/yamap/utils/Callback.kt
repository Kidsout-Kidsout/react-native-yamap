package ru.vvdev.yamap.utils

fun interface Callback<T> {
  fun invoke(arg: T)
}
