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

        validateSize(fileMetadata.getSize(), errors);
        validateContentType(fileMetadata, errors);

        return errors;
    }

    // =================================================================================================================
    // Implementation
    // =================================================================================================================

    private void validateSize(long size, List<FileValidationError> errors) {
        if (size > configProvider.getMaxFileSize()) {
            String description = configProvider.getErrorMessages().get(ValidationErrorCode.FILE_TOO_LARGE);
            errors.add(new FileValidationError(ValidationErrorCode.FILE_TOO_LARGE, description));
        }
    }

    private void validateContentType(FileMetadata fileMetadata, List<FileValidationError> errors) {
        try {
            List<String> extensions =
                    MimeTypes.getDefaultMimeTypes().forName(fileMetadata.getContentType()).getExtensions();
            for (String extension : extensions) {
                if (configProvider.getNotAllowedExtensions().contains(extension)) {
                    String description = configProvider.getErrorMessages().get(ValidationErrorCode.FILE_TOO_LARGE);
                    errors.add(new FileValidationError(ValidationErrorCode.FILE_EXTENSION_NOT_ALLOWED, description));
                }
            }
        } catch (MimeTypeException e) {
            log.error("Failed to validate contentType of {} file", fileMetadata.getObjectKey(), e);
            throw new TechnicalException(
                    String.format("Failed to validate contentType of %s file", fileMetadata.getObjectKey())
            );
        }
    }
}
