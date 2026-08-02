package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bazar.app.api.*;
import org.bazar.app.impl.commands.HandleFileUploadedCommand;
import org.bazar.app.impl.helpers.FileMetadataValidator;
import org.bazar.app.impl.output.FileProcessingResult;
import org.bazar.domain.File;
import org.bazar.domain.FileMetadata;
import org.bazar.domain.FileStatus;
import org.bazar.domain.FileValidationError;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class HandleFileUploadedUseCase implements HandleFileUploadedInbound {
    private final FileRepository fileRepository;
    private final UnitOfWork unitOfWork;
    private final NotifyFileUploadedOutbound notifyFileUploadedOutbound;
    private final StorageService storageService;
    private final FileMetadataValidator fileMetadataValidator;

    @Override
    public void execute(HandleFileUploadedCommand command) {
        FileProcessingResult result;
        try {
            result = unitOfWork.perform(() -> {
                File file = fileRepository.findByObjectKey(command.key())
                        .orElseThrow(() -> new IllegalStateException("File not found for key: " + command.key()));

                ValidationResult validationResult = validateFile(file);
                file.setSize(validationResult.metadata().getSize());
                file.setContentType(validationResult.metadata().getContentType());
                if (validationResult.hasErrors()) {
                    log.warn("Got validation errors {} for file {}", validationResult.errors, file.getFileUuid());
                    file.setErrors(validationResult.errors);
                    file.setStatus(FileStatus.VALIDATION_ERROR);
                    fileRepository.update(file);
                    // TODO: Доработать удаление файла (чистить ещё и в БД) - https://grinbog015.atlassian.net/browse/BZR-176
                    storageService.deleteByObjectKey(file.getObjectKey());
                    return new FileProcessingResult(file, true);
                }

                if (file.getStatus() == FileStatus.UPLOADED) {
                    return new FileProcessingResult(file, false);
                }

                file.setStatus(FileStatus.UPLOADED);
                fileRepository.update(file);

                return new FileProcessingResult(file, true);
            });
        } catch (Exception e) {
            log.error("Failed to process file upload", e);
            Optional<File> errorFile = markAsErrorIfExists(command.key());
            result = new FileProcessingResult(errorFile.orElse(null), errorFile.isPresent());
        }

        if (result.shouldPublish()) {
            publishEvent(result.file());
        }
    }

    // =================================================================================================================
    // Implementation
    // =================================================================================================================

    private Optional<File> markAsErrorIfExists(String objectKey) {
        try {
            return unitOfWork.perform(() -> {
                Optional<File> optionalFile = fileRepository.findByObjectKey(objectKey);

                optionalFile.ifPresent(file -> {
                    file.setStatus(FileStatus.ERROR);
                    fileRepository.update(file);
                });

                return optionalFile;
            });
        } catch (Exception ex) {
            log.error("Failed to mark file as ERROR", ex);
            return Optional.empty();
        }
    }

    private ValidationResult validateFile(File file) {
        FileMetadata fileMetadata = storageService.getFileMetadata(file);
        List<FileValidationError> fileValidationErrors = fileMetadataValidator.validateFileMetadata(fileMetadata);
        return new ValidationResult(fileMetadata, fileValidationErrors);
    }

    private void publishEvent(File file) {
        try {
            log.info("Publishing Kafka event for fileUuid {}", file.getFileUuid());
            notifyFileUploadedOutbound.execute(file);
        } catch (Exception e) {
            log.error("Failed to publish Kafka event", e);
        }
    }

    private record ValidationResult(
            FileMetadata metadata,
            List<FileValidationError> errors
    ) {
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }
}
