package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.GetStorageUrlsOutbound;
import org.bazar.app.api.InitiateDownloadFileInbound;
import org.bazar.app.api.exception.BusinessException;
import org.bazar.app.api.exception.ErrorCode;
import org.bazar.app.impl.commands.InitiateDownloadCommand;
import org.bazar.domain.File;

@RequiredArgsConstructor
public class InitiateDownloadFileUseCase implements InitiateDownloadFileInbound {
    private final GetStorageUrlsOutbound getStorageUrlsOutbound;
    private final FileRepository fileRepository;

    @Override
    public String execute(InitiateDownloadCommand command) {
        File file = fileRepository.findByIdFileUuid(command.fileUuid())
                .orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_FOUND_BY_FILE_UUID, command.fileUuid()));
        return getStorageUrlsOutbound.getDownloadUrl(file);
    }
}
