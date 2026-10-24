package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.HandleFileUploadedInbound;
import org.bazar.app.api.EventService;
import org.bazar.app.api.StorageService;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.impl.commands.HandleFileUploadedCommand;
import org.bazar.app.impl.helpers.FileMetadataValidator;
import org.bazar.app.impl.mapper.FileMapper;
import org.bazar.domain.file.File;
import org.bazar.domain.file.FileMetadata;
import org.bazar.domain.file.FileStatus;
import org.bazar.domain.file.FileValidationError;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class HandleFileUploadedUseCase implements HandleFileUploadedInbound {
    private final FileRepository fileRepository;
    private final UnitOfWork unitOfWork;
    private final EventService eventService;
    private final StorageService storageService;
    private final FileMetadataValidator fileMetadataValidator;
    private final FileMapper fileMapper;

    @Override
    public void execute(HandleFileUploadedCommand command) {
        try {
            unitOfWork.perform(() -> {
                File file = fileRepository.findByObjectKey(command.key())
                        .orElseThrow(() -> new IllegalStateException("File not found for key: " + command.key()));

                if (file.getStatus() == FileStatus.UPLOADED) {
                    return;
                }

                ValidationResult validationResult = validateFile(file);
                file.setSize(validationResult.metadata().getSize());
                file.setContentType(validationResult.metadata().getContentType());
                if (validationResult.hasErrors()) {
                    log.warn("Got validation errors {} for file {}", validationResult.errors, file.getFileUuid());
                    file.setErrors(validationResult.errors);
                    file.setStatus(FileStatus.VALIDATION_ERROR);
                    fileRepository.update(file);
                    // TODO: Доработать удаление файла (чистить ещё и в БД) - https://grinbog015.atlassian.net/browse/BZR-176
                    // И не удалять сразу здесь, а делать это в отдельном процессе, чтобы не нарушать согласованность данных (S3 и наша БД)
                    storageService.deleteByObjectKey(file.getObjectKey());
                } else {
                    file.setStatus(FileStatus.UPLOADED);
                    fileRepository.update(file);
                }
                eventService.saveFileEvent(fileMapper.toOutput(file));
            });
        } catch (Exception e) {
            log.error("Failed to process file upload", e);
            markAsErrorAndSaveEventIfExists(command.key());
        }
    }

    // =================================================================================================================
    // Implementation
    // =================================================================================================================

    private void markAsErrorAndSaveEventIfExists(String objectKey) {
        try {
            unitOfWork.perform(() -> {
                Optional<File> optionalFile = fileRepository.findByObjectKey(objectKey);

                optionalFile.ifPresent(file -> {
                    file.setStatus(FileStatus.ERROR);
                    fileRepository.update(file);
                    eventService.saveFileEvent(fileMapper.toOutput(file));
                });
            });
        } catch (Exception ex) {
            log.error("Failed to mark file as ERROR", ex);
        }
    }

    private ValidationResult validateFile(File file) {
        FileMetadata fileMetadata = storageService.getFileMetadata(file);
        List<FileValidationError> fileValidationErrors = fileMetadataValidator.validateFileMetadata(fileMetadata);
        return new ValidationResult(fileMetadata, fileValidationErrors);
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
