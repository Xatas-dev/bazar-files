package org.bazar.adapter.rest.files;

import org.bazar.app.impl.commands.InitiateUploadCommand;
import org.bazar.app.impl.output.FileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразований для REST API
 */
@Mapper(componentModel = "jakarta")
public interface RestFileMapper {
    InitiateUploadCommand toInitiateUploadCommand(String fileName, Integer size, String contentType, String domain);
    @Mapping(target = "uploadUrl", source = "presignedUrl")
    InitiateUploadResponse toInitiateUploadResponse(FileInfo fileDto);
}
