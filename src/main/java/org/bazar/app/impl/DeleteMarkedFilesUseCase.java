package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bazar.app.api.ConfigProvider;
import org.bazar.app.api.DeleteMarkedFilesInbound;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.StorageService;
import org.bazar.app.api.UnitOfWork;
import org.bazar.domain.File;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class DeleteMarkedFilesUseCase implements DeleteMarkedFilesInbound {
    private final FileRepository fileRepository;
    private final UnitOfWork unitOfWork;
    private final StorageService storageService;
    private final ConfigProvider configProvider;

    @Override
    public void execute() {
        Long lastId = 0L;
        while (true) {
            List<File> batch = fileRepository.findDeletingFilesAfterId(lastId, configProvider.getDeletingFilesBatchSize());
            if (batch.isEmpty()) {
                break;
            }
            batch.forEach(this::deleteSingleFile);

            lastId = batch.getLast().getId();
        }
    }

    // =================================================================================================================
    // Implementation
    // =================================================================================================================

    private void deleteSingleFile(File file) {
        try {
            unitOfWork.perform(() -> {
                storageService.deleteByObjectKey(file.getObjectKey());
                fileRepository.deleteById(file.getId());
                return null;
            });
        } catch (Exception e) {
            log.error("Failed to cleanup file {}", file.getFileUuid(), e);
        }
    }
}
