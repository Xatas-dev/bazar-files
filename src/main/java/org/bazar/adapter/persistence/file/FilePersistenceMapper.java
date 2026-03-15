package org.bazar.adapter.persistence.file;

import org.bazar.domain.File;
import org.mapstruct.Mapper;

/**
 * Маппер для сущности файла
 */
@Mapper(componentModel = "jakarta")
public interface FilePersistenceMapper {
    /**
     * Преобразует доменную сущность файла в ORM-сущность
     */
    FileEntity toEntity(File file);
}
