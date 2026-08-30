package org.bazar.app.impl.helpers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.bazar.app.api.ConfigProvider;
import org.bazar.app.api.exception.TechnicalException;
import org.bazar.domain.FileMetadata;
import org.bazar.domain.FileValidationError;
import org.bazar.domain.ValidationErrorCode;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class FileMetadataValidator {
    private final ConfigProvider configProvider;

    public List<FileValidationError> validateFileMetadata(FileMetadata fileMetadata) {
        List<FileValidationError> errors = new ArrayList<>();

        errors.addAll(validateSize(fileMetadata.getSize()));
        errors.addAll(validateContentType(fileMetadata));

        return errors;
    }

    // =================================================================================================================
    // Implementation
    // =================================================================================================================

    private List<FileValidationError> validateSize(long size) {
        if (size > configProvider.getMaxFileSize()) {
            String description = configProvider.getErrorMessages().get(ValidationErrorCode.FILE_TOO_LARGE);
            return List.of(new FileValidationError(ValidationErrorCode.FILE_TOO_LARGE, description));
        }
        return List.of();
    }

    private List<FileValidationError> validateContentType(FileMetadata fileMetadata) {
        try {
            List<String> extensions =
                    MimeTypes.getDefaultMimeTypes().forName(fileMetadata.getContentType()).getExtensions();

            return extensions.stream()
                    .filter(extension -> configProvider.getNotAllowedExtensions().contains(extension))
                    .map(extension -> new FileValidationError(ValidationErrorCode.FILE_EXTENSION_NOT_ALLOWED,
                            configProvider.getErrorMessages().get(ValidationErrorCode.FILE_EXTENSION_NOT_ALLOWED)
                    ))
                    .toList();
        } catch (MimeTypeException e) {
            log.error("Failed to validate contentType of {} file", fileMetadata.getObjectKey(), e);
            throw new TechnicalException(
                        String.format("Failed to validate contentType of %s file", fileMetadata.getObjectKey()));
        }
    }
}
