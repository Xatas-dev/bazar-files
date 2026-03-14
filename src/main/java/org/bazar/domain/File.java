package org.bazar.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность файла в хранилище
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class File {
    /**
     * Уникальный идентификатор файла
     */
    private String id;

    /**
     * Уникальный идентификатор файла в хранилище
     */
    private String fileUuid;
    /**
     * Домен, для которого был загружен файл
     */
    private String domain;

    /**
     * MIME-тип файла
     */
    private String contentType;

    /**
     * Размер файла в байтах
     */
    private Long size;

    /**
     * Имя файла
     */
    private String fileName;

    /**
     * Ключ объекта в хранилище файлов
     */
    private String objectKey;

    /**
     * Статус файла
     */
    private String status;
}
