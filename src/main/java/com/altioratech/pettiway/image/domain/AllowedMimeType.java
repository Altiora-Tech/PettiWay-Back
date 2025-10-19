package com.altioratech.pettiway.image.domain;

import java.util.Arrays;

public enum AllowedMimeType {
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_WEBP("image/webp"),
    PDF("application/pdf");

    private final String value;

    AllowedMimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isAllowed(String contentType) {
        return Arrays.stream(values())
                .anyMatch(type -> type.value.equalsIgnoreCase(contentType));
    }
}
