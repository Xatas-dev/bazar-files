package org.bazar.app.api;

public interface GetUploadUrlOutbound {
    String execute(String fileUuid, String extension);
}
