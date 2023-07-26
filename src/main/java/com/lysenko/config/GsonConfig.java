package com.lysenko.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonConfig {

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        }
        return gson;
    }
}
