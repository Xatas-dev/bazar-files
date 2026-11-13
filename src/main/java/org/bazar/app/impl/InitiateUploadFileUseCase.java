package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import org.bazar.app.api.ConfigProvider;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.StorageService;
import org.bazar.app.api.InitiateUploadFileInbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.impl.commands.InitiateUploadCommand;
import org.bazar.app.impl.mapper.FileMapper;
import org.bazar.app.impl.output.FileInfo;
import org.bazar.domain.file.File;
import org.bazar.domain.file.FileAccessType;
import org.bazar.domain.file.FileStatus;

import java.util.UUID;

@RequiredArgsConstructor
public class InitiateUploadFileUseCase implements InitiateUploadFileInbound {
    private final FileRepository fileRepository;
    private final UnitOfWork unitOfWork;
    private final StorageService storageService;
    private final FileMapper fileMapper;
    private final ConfigProvider configProvider;

    @Override
    public FileInfo execute(InitiateUploadCommand command) {
        return unitOfWork.perform(() -> {
            File file = saveFile(command);

            return fileMapper.toOutput(storageService.getUploadUrl(file), file.getFileUuid().toString());
        });
    }

    // =================================================================================================================
    // Implementation
    // =================================================================================================================

    private File saveFile(InitiateUploadCommand command) {
        File file = fileMapper.toDomain(command);
        file.setFileUuid(UUID.randomUUID());
        file.setStatus(FileStatus.INIT);
        file.setObjectKey(file.getFileUuid() + "_" + file.getFileName());
        file.setBucketName(getBucketName(command.accessType()));
        fileRepository.create(file);
        return file;
    }

    private String getBucketName(FileAccessType accessType) {
        return switch (accessType) {
            case PUBLIC -> configProvider.getFilesPublicBucketName();
            case PRIVATE -> configProvider.getFilesBucketName();
        };
    }
}
