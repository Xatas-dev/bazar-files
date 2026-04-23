package org.bazar.app.api.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

public enum ErrorCode {
    FILE_NOT_FOUND_BY_FILE_UUID(Response.Status.NOT_FOUND, "File not found with fileUuid: %s");

    @Getter
    private final Response.Status status;
    private final String messageTemplate;

    ErrorCode(Response.Status status, String messageTemplate) {
        this.status = status;
        this.messageTemplate = messageTemplate;
    }

    public String formatMessage(Object... args) {
        return String.format(messageTemplate, args);
    }
}
