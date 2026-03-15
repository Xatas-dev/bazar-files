package org.bazar.adapter.persistence.file;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.bazar.adapter.persistence.DomainObject;

import java.util.UUID;

/**
 * Сущность файла в хранилище
 */
@Entity
@Table(name = "file")
@Setter
@Getter
public class FileEntity extends DomainObject {
    /**
     * Уникальный идентификатор файла
     */
    @Column(name = "file_uuid")
    private UUID fileUuid;

    /**
     * Домен, для которого был загружен файл
     */
    @Column(name = "domain")
    private String domain;

    /**
     * MIME-тип файла
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * Размер файла в байтах
     */
    @Column(name = "size")
    private Integer size;

    /**
     * Имя файла
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * Ключ объекта в хранилище файлов
     */
    @Column(name = "object_key")
    private String objectKey;

    /**
     * Статус файла
     */
    @Column(name = "status")
    private String status;
}
