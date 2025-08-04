package com.lab.moviebrowserapplication.utils;

import com.lab.moviebrowserapplication.App;
import javafx.scene.Scene;

public class ThemeManager {
    public enum Theme {
        LIGHT("light-theme.css"),
        DARK("dark-theme.css");

        private final String cssFileName;

        Theme(String cssFileName) {
            this.cssFileName = cssFileName;
        }

        public String getCssFileName() {
            return cssFileName;
        }
    }

    private static final String BASE_CSS_PATH = "/com/lab/moviebrowserapplication/css/";
    private static final String STYLES_CSS = BASE_CSS_PATH + "styles.css";

    public static void applyTheme(Scene scene, Theme theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(App.class.getResource(STYLES_CSS).toExternalForm());
        scene.getStylesheets().add(App.class.getResource(BASE_CSS_PATH + theme.getCssFileName()).toExternalForm());
    }
}