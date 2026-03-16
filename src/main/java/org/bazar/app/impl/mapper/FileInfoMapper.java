package org.bazar.app.impl.mapper;

import org.bazar.domain.FileInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta")
public interface FileInfoMapper {
    FileInfo toDomain(String presignedUrl, String fileUuid);
}
