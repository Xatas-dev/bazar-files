package org.bazar.adapter.rest.files;

import org.bazar.app.impl.commands.InitiateDownloadCommand;
import org.bazar.app.impl.commands.InitiateUploadCommand;
import org.bazar.app.impl.output.FileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Маппер для преобразований для REST API
 */
@Mapper(componentModel = "jakarta")
public interface RestFileMapper {
    InitiateUploadCommand toInitiateUploadCommand(InitiateUploadRequest request);

    @Mapping(target = "uploadUrl", source = "presignedUrl")
    InitiateUploadResponse toInitiateUploadResponse(FileInfo fileDto);

    InitiateDownloadResponse toInitiateDownloadResponse(String downloadUrl);

    InitiateDownloadCommand toInitiateDownloadCommand(UUID fileUuid);
}
