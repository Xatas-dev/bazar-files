package org.bazar.adapter.persistance.uploadedfile;

import org.bazar.domain.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta")
public interface UploadedFileMapper {
    FileEntity toEntity(File file);
    File toDomain(FileEntity fileEntity);
}
