package org.bazar.app.impl.output;

import lombok.Data;

import java.util.List;

@Data
public class FileEventOut {
    private String fileName;
    private String domain;
    private String contentType;
    private Long size;
    private String fileUuid;
    private String status;
    private List<Error> errors;

    @Data
    public static class Error {
        private String code;
        private String description;
    }
}
