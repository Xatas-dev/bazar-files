package org.bazar.fw;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.api.UploadedFileRepository;
import org.bazar.app.impl.SaveTempFileUseCase;

@Singleton
public class UseCaseConfig {
    @Produces
    @Singleton
    SaveTempFileUseCase saveTempFileUseCase(UploadedFileRepository uploadedFileRepository, UnitOfWork unitOfWork) {
        return new SaveTempFileUseCase(uploadedFileRepository, unitOfWork);
    }
}
