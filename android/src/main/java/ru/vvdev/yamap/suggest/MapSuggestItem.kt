package ru.vvdev.yamap.suggest

import javax.annotation.Nullable

data class MapSuggestItem(
  var title: String = "",
  @Nullable var subtitle: String? = null,
  @Nullable var uri: String? = null,
  var searchText: String = "",
  @Nullable var displayText: String? = null
)
