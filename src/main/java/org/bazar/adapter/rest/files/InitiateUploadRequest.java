package org.bazar.adapter.rest.files;

import jakarta.ws.rs.QueryParam;

public record InitiateUploadRequest(
        @QueryParam("fileName") String fileName,
        @QueryParam("size") Integer size,
        @QueryParam("contentType") String contentType,
        @QueryParam("domain") String domain) {
}
