package org.bazar.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileMetadata {
    private String objectKey;
    private Long size;
    private String contentType;
}
