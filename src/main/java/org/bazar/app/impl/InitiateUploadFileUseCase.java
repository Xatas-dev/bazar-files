package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.GetUploadUrlOutbound;
import org.bazar.app.api.InitiateUploadFileInbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.impl.commands.InitiateUploadCommand;
import org.bazar.app.impl.mapper.FileMapper;
import org.bazar.app.impl.output.FileInfo;
import org.bazar.domain.File;
import org.bazar.domain.FileStatus;

import java.util.UUID;

@RequiredArgsConstructor
public class InitiateUploadFileUseCase implements InitiateUploadFileInbound {
    private final FileRepository fileRepository;
    private final UnitOfWork unitOfWork;
    private final GetUploadUrlOutbound getUploadUrlOutbound;
    private final FileMapper fileMapper;

    @Override
    public FileInfo execute(InitiateUploadCommand command) {
        return unitOfWork.perform(() -> {
            File file = fileMapper.toDomain(command);
            file.setFileUuid(UUID.randomUUID().toString());
            file.setStatus(FileStatus.INIT);
            file.setObjectKey(file.getFileUuid() + "_" + file.getFileName());
            fileRepository.create(file);

            return fileMapper.toOutput(getUploadUrlOutbound.execute(file), file.getFileUuid());
        });
    }
}
