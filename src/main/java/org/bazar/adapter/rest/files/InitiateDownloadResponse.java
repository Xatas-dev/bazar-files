package org.bazar.adapter.rest.files;

/**
 * Ответ на запрос на инициацию скачивания файла
 *
 * @param downloadUrl URL для скачивания файла
 */
public record InitiateDownloadResponse(String downloadUrl) {
}
