package org.bazar.app.impl;

import org.bazar.app.api.GetUploadUrlOutbound;
import org.bazar.app.api.InitiateUploadFileInbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.api.UploadedFileRepository;
import org.bazar.app.impl.mapper.FileInfoMapper;
import org.bazar.domain.File;
import org.bazar.domain.FileInfo;
import org.bazar.domain.FileStatus;

import java.util.UUID;

public class InitiateUploadFileUseCase implements InitiateUploadFileInbound {
    private final UploadedFileRepository uploadedFileRepository;
    private final UnitOfWork unitOfWork;
    private final GetUploadUrlOutbound getUploadUrlOutbound;
    private final FileInfoMapper fileInfoMapper;

    public InitiateUploadFileUseCase(UploadedFileRepository uploadedFileRepository, UnitOfWork unitOfWork, GetUploadUrlOutbound getUploadUrlOutbound, FileInfoMapper fileInfoMapper) {
        this.uploadedFileRepository = uploadedFileRepository;
        this.unitOfWork = unitOfWork;
        this.getUploadUrlOutbound = getUploadUrlOutbound;
        this.fileInfoMapper = fileInfoMapper;
    }

    @Override
    public FileInfo execute(File file) {
        return unitOfWork.perform(() -> {
            file.setFileUuid(UUID.randomUUID().toString());
            file.setStatus(FileStatus.INIT);
            file.setObjectKey(file.getFileUuid() + "_" + file.getFileName());
            uploadedFileRepository.save(file);

            return fileInfoMapper.toDomain(getUploadUrlOutbound.execute(file), file.getFileUuid());
        });
    }
}
