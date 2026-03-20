package org.bazar.adapter.kafka;

import org.bazar.adapter.kafka.message.FileEventOut;
import org.bazar.domain.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta")
public interface KafkaFileMapper {
    FileEventOut toFileEventOut(File file);
}
