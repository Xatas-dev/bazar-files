package org.bazar.adapter.rest.files;

import org.bazar.domain.File;
import org.bazar.domain.FileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразований для REST API
 */
@Mapper(componentModel = "jakarta")
public interface RestFileMapper {
    File toDomain(String fileName, Integer size, String contentType, String domain);
    @Mapping(target = "uploadUrl", source = "presignedUrl")
    InitiateUploadResponse toInitiateUploadResponse(FileInfo fileDto);
}
