package org.bazar.app.impl.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bazar.domain.file.File;
import org.bazar.domain.outbox.OutboxEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "jakarta")
@RequiredArgsConstructor
public abstract class OutboxMapper {
    private final ObjectMapper objectMapper;

    @Mapping(target = "topic", constant = "file.events")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "retryCount", constant = "0")
    @Mapping(target = "message", source = ".", qualifiedByName = "toFileJson")
    public abstract OutboxEvent toPendingFileEvent(File file);

    @Named("toFileJson")
    protected String toFileJson(File file) throws JsonProcessingException {
        return objectMapper.writeValueAsString(file);
    }
}
