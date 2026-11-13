package org.bazar.app.impl.mapper;

import org.bazar.app.impl.helpers.FileEventJsonMapper;
import org.bazar.app.impl.output.FileEventOut;
import org.bazar.domain.outbox.OutboxEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta", uses = FileEventJsonMapper.class)
public interface OutboxMapper {

    @Mapping(target = "topic", constant = "file.events")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "retryCount", constant = "0")
    @Mapping(target = "message", source = ".", qualifiedByName = "toFileJson")
    @Mapping(target = "nextAttemptAt", expression = "java(java.time.Instant.now())")
    OutboxEvent toPendingFileEvent(FileEventOut file);
}
