package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import org.bazar.app.api.MarkFileForDeletionInbound;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.UnitOfWork;
import org.bazar.domain.File;
import org.bazar.domain.FileStatus;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class MarkFileForDeletionUseCase implements MarkFileForDeletionInbound {
    private final FileRepository fileRepository;
    private final UnitOfWork unitOfWork;

    @Override
    public void execute(UUID fileUuid) {
        unitOfWork.perform(() -> {
            Optional<File> optionalFile = fileRepository.findByFileUuid(fileUuid);
            optionalFile.ifPresent(file -> {
                file.setStatus(FileStatus.DELETING);
                fileRepository.update(file);
            });
            return 0;
        });
    }
}
