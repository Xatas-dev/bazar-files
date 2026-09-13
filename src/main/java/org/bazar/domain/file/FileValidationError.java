package org.bazar.domain.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileValidationError {
    private ValidationErrorCode code;
    private String description;
}
