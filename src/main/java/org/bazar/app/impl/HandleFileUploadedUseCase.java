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

                List<FileValidationError> fileValidationErrors = validateAndUpdateFile(file);
                if (!fileValidationErrors.isEmpty()) {
                    log.warn("Got validation errors {} for file {}", fileValidationErrors, file.getFileUuid());
                    file.setErrors(fileValidationErrors);
                    file.setStatus(FileStatus.VALIDATION_ERROR);
                    fileRepository.update(file);
                    // Возможно стоит добавить шедуллер на очистку файлов в статусе VALIDATION_ERROR, но пока так
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

    private List<FileValidationError> validateAndUpdateFile(File file) {
        FileMetadata fileMetadata = storageService.getFileMetadata(file);
        file.setSize(fileMetadata.getSize());
        file.setContentType(fileMetadata.getContentType());
        return fileMetadataValidator.validateFileMetadata(fileMetadata);
    }

    private void publishEvent(File file) {
        try {
            log.info("Publishing Kafka event for fileUuid {}", file.getFileUuid());
            notifyFileUploadedOutbound.execute(file);
        } catch (Exception e) {
            log.error("Failed to publish Kafka event", e);
        }
    }
}
