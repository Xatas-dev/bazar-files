package org.bazar.app.impl;

import org.bazar.app.api.GetUploadUrlOutbound;
import org.bazar.app.api.InitiateUploadFileInbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.api.UploadedFileRepository;
import org.bazar.domain.File;

import java.util.UUID;

public class InitiateUploadFileUseCase implements InitiateUploadFileInbound {
    private final UploadedFileRepository uploadedFileRepository;
    private final UnitOfWork unitOfWork;
    private final GetUploadUrlOutbound getUploadUrlOutbound;

    public InitiateUploadFileUseCase(UploadedFileRepository uploadedFileRepository, UnitOfWork unitOfWork, GetUploadUrlOutbound getUploadUrlOutbound) {
        this.uploadedFileRepository = uploadedFileRepository;
        this.unitOfWork = unitOfWork;
        this.getUploadUrlOutbound = getUploadUrlOutbound;
    }

    @Override
    public String execute(String extension) {
        return unitOfWork.perform(() -> {
            File file = new File();
            file.setFileUuid(UUID.randomUUID().toString());
            uploadedFileRepository.save(file);

            return getUploadUrlOutbound.execute(file.getFileUuid(), extension);
        });
    }
}
