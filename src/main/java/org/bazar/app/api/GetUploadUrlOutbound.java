package org.bazar.app.api;

import org.bazar.domain.File;

/**
 * Интерфейс для получения URL для загрузки файла в хранилище
 */
public interface GetUploadUrlOutbound {
    String execute(File file);
}
