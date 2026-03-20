package org.bazar.adapter.webhook;

import org.bazar.app.impl.commands.HandleFileUploadedCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "jakarta")
public interface RestS3Mapper {
    @Mapping(target = "key", source = "key", qualifiedByName = "decodeKey")
    HandleFileUploadedCommand toDomain(S3WebhookEvent.S3Object event);

    @Named("decodeKey")
    default String decodeKey(String key) {
        if (key == null) {
            return null;
        }
        return java.net.URLDecoder.decode(key, java.nio.charset.StandardCharsets.UTF_8);
    }
}
