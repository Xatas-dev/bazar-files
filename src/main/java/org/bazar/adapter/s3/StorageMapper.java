package org.bazar.adapter.s3;

import org.bazar.domain.File;
import org.bazar.domain.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Mapper
public interface StorageMapper {
    @Mapping(target = "size", source = "response", qualifiedByName = "mapFileSize")
    @Mapping(target = "objectKey", source = "file.objectKey")
    @Mapping(target = "contentType", source = "contentType")
    FileMetadata toFileMetadata(GetObjectResponse response, String contentType, File file);

    @Named("mapFileSize")
    default Long mapFileSize(GetObjectResponse response) {
        String contentRange = response.contentRange();
        String stringSize = contentRange.substring(contentRange.indexOf("/") + 1);
        return Long.parseLong(stringSize);
    }
}
