package org.bazar.fw;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.GetStorageUrlsOutbound;
import org.bazar.app.api.NotifyFileUploadedOutbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.impl.HandleFileUploadedUseCase;
import org.bazar.app.impl.InitiateDownloadFileUseCase;
import org.bazar.app.impl.InitiateUploadFileUseCase;
import org.bazar.app.impl.mapper.FileMapper;

@Singleton
public class UseCaseProducer {
    @Produces
    @Singleton
    InitiateUploadFileUseCase initiateUploadFileUseCase(FileRepository fileRepository, UnitOfWork unitOfWork,
                                                        GetStorageUrlsOutbound getStorageUrlsOutbound, FileMapper fileMapper) {
        return new InitiateUploadFileUseCase(fileRepository, unitOfWork, getStorageUrlsOutbound, fileMapper);
    }

    @Produces
    @Singleton
    HandleFileUploadedUseCase handleFileUploadedUseCase(FileRepository fileRepository, UnitOfWork unitOfWork, NotifyFileUploadedOutbound notifyFileUploadedOutbound) {
        return new HandleFileUploadedUseCase(fileRepository, unitOfWork, notifyFileUploadedOutbound);
    }

    @Produces
    @Singleton
    InitiateDownloadFileUseCase initiateDownloadFileUseCase(GetStorageUrlsOutbound getStorageUrlsOutbound, FileRepository fileRepository) {
        return new InitiateDownloadFileUseCase(getStorageUrlsOutbound, fileRepository);
    }
}
