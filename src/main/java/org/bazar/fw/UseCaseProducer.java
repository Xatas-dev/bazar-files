package org.bazar.fw;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.GetUploadUrlOutbound;
import org.bazar.app.api.NotifyFileUploadedOutbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.impl.HandleFileUploadedUseCase;
import org.bazar.app.impl.InitiateUploadFileUseCase;
import org.bazar.app.impl.mapper.FileMapper;

@Singleton
public class UseCaseProducer {
    @Produces
    @Singleton
    InitiateUploadFileUseCase initiateUploadFileUseCase(FileRepository fileRepository, UnitOfWork unitOfWork,
                                                  GetUploadUrlOutbound getUploadUrlOutbound, FileMapper fileMapper) {
        return new InitiateUploadFileUseCase(fileRepository, unitOfWork, getUploadUrlOutbound, fileMapper);
    }

    @Produces
    @Singleton
    HandleFileUploadedUseCase handleFileUploadedUseCase(FileRepository fileRepository, UnitOfWork unitOfWork, NotifyFileUploadedOutbound notifyFileUploadedOutbound) {
        return new HandleFileUploadedUseCase(fileRepository, unitOfWork, notifyFileUploadedOutbound);
    }
}
