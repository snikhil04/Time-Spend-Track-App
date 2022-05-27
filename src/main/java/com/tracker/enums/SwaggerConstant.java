package com.tracker.enums;

public enum SwaggerConstant {
    JWT("JWT"),
    X_AUTHORIZATION("X-Authorization"),
    GLOBAL("global"),
    ACCESS_EVERYTHING("accessEverything"),
    HEADER("header");

    String value;

    SwaggerConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
