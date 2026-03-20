package org.bazar.app.impl.mapper;

import org.bazar.app.impl.commands.InitiateUploadCommand;
import org.bazar.app.impl.output.FileInfo;
import org.bazar.domain.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta")
public interface FileMapper {
    File toDomain(InitiateUploadCommand command);
    FileInfo toOutput(String presignedUrl, String fileUuid);
}
