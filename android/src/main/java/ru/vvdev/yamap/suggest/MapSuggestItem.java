package ru.vvdev.yamap.suggest;

import javax.annotation.Nullable;

public class MapSuggestItem {
    private String title;
    @Nullable
    private String subtitle;
    @Nullable
    private String uri;
    private String searchText;
    @Nullable
    private String displayText;

    public MapSuggestItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }


    @Nullable
    public String getUri() {
        return uri;
    }

    public void setUri(@Nullable String uri) {
        this.uri = uri;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Nullable
    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(@Nullable String displayText) {
        this.displayText = displayText;
    }
}
