package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.HandleFileUploadedInbound;
import org.bazar.app.api.NotifyFileUploadedOutbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.impl.commands.HandleFileUploadedCommand;
import org.bazar.app.impl.output.ProcessResult;
import org.bazar.domain.File;
import org.bazar.domain.FileStatus;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class HandleFileUploadedUseCase implements HandleFileUploadedInbound {
    private final FileRepository fileRepository;
    private final UnitOfWork unitOfWork;
    private final NotifyFileUploadedOutbound notifyFileUploadedOutbound;

    @Override
    public void execute(HandleFileUploadedCommand command, String eventName) {
        ProcessResult result;
        try {
            result = unitOfWork.perform(() -> {
                File file = fileRepository.findByObjectKey(command.key())
                        .orElseThrow(() -> new IllegalStateException("File not found for key: " + command.key()));

                boolean statusChanged = file.getStatus() != FileStatus.UPLOADED;
                if (statusChanged) {
                    file.setStatus(FileStatus.UPLOADED);
                    fileRepository.update(file);
                }

                return ProcessResult.success(file, statusChanged);
            });
        } catch (Exception e) {
            log.error("Failed to process file upload", e);
            File errorFile = markAsErrorIfExists(command);
            result = ProcessResult.fail(errorFile);
        }

        if (result.shouldPublish()) {
            publishEvent(result.file());
        }
    }

    // =================================================================================================================
    // Implementation
    // =================================================================================================================

    private File markAsErrorIfExists(HandleFileUploadedCommand command) {
        try {
            return unitOfWork.perform(() -> {
                Optional<File> optionalFile = fileRepository.findByObjectKey(command.key());

                optionalFile.ifPresent(file -> {
                    file.setStatus(FileStatus.ERROR);
                    fileRepository.update(file);
                });

                return optionalFile.orElse(null);
            });
        } catch (Exception ex) {
            log.error("Failed to mark file as ERROR", ex);
            return null;
        }
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
