package org.bazar.app.impl.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bazar.app.impl.output.FileEventOut;
import org.mapstruct.Named;

@RequiredArgsConstructor
public class FileEventJsonMapper {
    private final ObjectMapper objectMapper;

    @Named("toFileJson")
    public String toFileJson(FileEventOut file) throws JsonProcessingException {
        return objectMapper.writeValueAsString(file);
    }
}
