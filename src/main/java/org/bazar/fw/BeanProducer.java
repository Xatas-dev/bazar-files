package org.bazar.fw;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.bazar.app.api.ConfigProvider;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.OutboxRepository;
import org.bazar.app.api.OutboxService;
import org.bazar.app.api.SendOutBoxEventOutbound;
import org.bazar.app.api.StorageService;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.impl.DeleteMarkedFilesUseCase;
import org.bazar.app.impl.HandleFileUploadedUseCase;
import org.bazar.app.impl.InitiateDownloadFileUseCase;
import org.bazar.app.impl.InitiateUploadFileUseCase;
import org.bazar.app.impl.MarkFileForDeletionUseCase;
import org.bazar.app.impl.SendOutboxEventsUseCase;
import org.bazar.app.impl.helpers.FileEventJsonMapper;
import org.bazar.app.impl.helpers.FileMetadataValidator;
import org.bazar.app.impl.mapper.FileMapper;

@Singleton
public class BeanProducer {
    @Produces
    @Singleton
    InitiateUploadFileUseCase initiateUploadFileUseCase(FileRepository fileRepository, UnitOfWork unitOfWork,
                                                        StorageService storageService, FileMapper fileMapper) {
        return new InitiateUploadFileUseCase(fileRepository, unitOfWork, storageService, fileMapper);
    }

    @Produces
    @Singleton
    HandleFileUploadedUseCase handleFileUploadedUseCase(FileRepository fileRepository, UnitOfWork unitOfWork,
                                                        OutboxService outboxService, StorageService storageService,
                                                        FileMetadataValidator validator, FileMapper fileMapper) {
        return new HandleFileUploadedUseCase(fileRepository, unitOfWork, outboxService, storageService, validator, fileMapper);
    }

    @Produces
    @Singleton
    InitiateDownloadFileUseCase initiateDownloadFileUseCase(StorageService storageService, FileRepository fileRepository) {
        return new InitiateDownloadFileUseCase(storageService, fileRepository);
    }

    @Produces
    @Singleton
    MarkFileForDeletionUseCase deleteFileByUuidUseCase(FileRepository fileRepository, UnitOfWork unitOfWork) {
        return new MarkFileForDeletionUseCase(fileRepository, unitOfWork);
    }

    @Produces
    @Singleton
    DeleteMarkedFilesUseCase deleteMarkedFilesUseCase(FileRepository fileRepository, UnitOfWork unitOfWork,
                                                      StorageService storageService, ConfigProvider configProvider) {
        return new DeleteMarkedFilesUseCase(fileRepository, unitOfWork, storageService, configProvider);
    }

    @Produces
    @Singleton
    FileMetadataValidator fileMetadataValidator(ConfigProvider configProvider) {
        return new FileMetadataValidator(configProvider);
    }

    @Produces
    @Singleton
    SendOutboxEventsUseCase sendOutboxEventsUseCase(OutboxRepository outboxRepository, SendOutBoxEventOutbound sendOutBoxEventOutbound,
                                                    ConfigProvider configProvider, UnitOfWork unitOfWork) {
        return new SendOutboxEventsUseCase(outboxRepository, sendOutBoxEventOutbound, configProvider, unitOfWork);
    }

    @Produces
    @Singleton
    FileEventJsonMapper fileEventJsonMapper(ObjectMapper objectMapper) {
        return new FileEventJsonMapper(objectMapper);
    }
}
