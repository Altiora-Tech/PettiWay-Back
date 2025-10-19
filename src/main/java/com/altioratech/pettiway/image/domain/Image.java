package com.altioratech.pettiway.image.domain;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    private UUID id;
    private UUID userId;
    private ImageCategory category;
    private String fileName;
    private String url;
    private String contentType;
    private long size;
}