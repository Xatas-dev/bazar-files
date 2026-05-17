package org.bazar.fw;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.bazar.app.api.ConfigProvider;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.NotifyFileUploadedOutbound;
import org.bazar.app.api.StorageService;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.impl.DeleteMarkedFilesUseCase;
import org.bazar.app.impl.HandleFileUploadedUseCase;
import org.bazar.app.impl.InitiateDownloadFileUseCase;
import org.bazar.app.impl.InitiateUploadFileUseCase;
import org.bazar.app.impl.MarkFileForDeletionUseCase;
import org.bazar.app.impl.mapper.FileMapper;

@Singleton
public class UseCaseProducer {
    @Produces
    @Singleton
    InitiateUploadFileUseCase initiateUploadFileUseCase(FileRepository fileRepository, UnitOfWork unitOfWork,
                                                        StorageService storageService, FileMapper fileMapper) {
        return new InitiateUploadFileUseCase(fileRepository, unitOfWork, storageService, fileMapper);
    }

    @Produces
    @Singleton
    HandleFileUploadedUseCase handleFileUploadedUseCase(FileRepository fileRepository, UnitOfWork unitOfWork, NotifyFileUploadedOutbound notifyFileUploadedOutbound) {
        return new HandleFileUploadedUseCase(fileRepository, unitOfWork, notifyFileUploadedOutbound);
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
}
