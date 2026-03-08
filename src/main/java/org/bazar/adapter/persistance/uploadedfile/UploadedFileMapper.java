package org.bazar.adapter.persistance.uploadedfile;

import org.bazar.domain.UploadedFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta")
public interface UploadedFileMapper {
    UploadedFileEntity toEntity(UploadedFile uploadedFile);
    UploadedFile toDomain(UploadedFileEntity uploadedFileEntity);
}
