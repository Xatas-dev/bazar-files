package org.bazar.fw;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.bazar.app.api.GetUploadUrlOutbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.api.UploadedFileRepository;
import org.bazar.app.impl.InitiateUploadFileUseCase;
import org.bazar.app.impl.mapper.FileInfoMapper;

@Singleton
public class UseCaseProducer {
    @Produces
    @Singleton
    InitiateUploadFileUseCase saveTempFileUseCase(UploadedFileRepository uploadedFileRepository, UnitOfWork unitOfWork, GetUploadUrlOutbound getUploadUrlOutbound, FileInfoMapper fileInfoMapper) {
        return new InitiateUploadFileUseCase(uploadedFileRepository, unitOfWork, getUploadUrlOutbound, fileInfoMapper);
    }
}
