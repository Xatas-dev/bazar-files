package org.bazar.adapter.rest.files;

/**
 * Ответ на запрос на инициацию загрузки файла
 *
 * @param uploadUrl URL для загрузки файла
 * @param fileUuid Уникальный идентификатор файла
 */
public record InitiateUploadResponse(
        String uploadUrl,
        String fileUuid
) {
}
