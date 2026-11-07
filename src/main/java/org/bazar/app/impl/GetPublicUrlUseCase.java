package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import org.bazar.app.api.FileRepository;
import org.bazar.app.api.GetPublicUrlInbound;
import org.bazar.app.api.StorageService;
import org.bazar.app.api.exception.BusinessException;
import org.bazar.app.api.exception.ErrorCode;
import org.bazar.app.impl.commands.GetPublicUrlCommand;
import org.bazar.domain.file.File;

import static org.bazar.domain.file.FileAccessType.PUBLIC;

@RequiredArgsConstructor
public class GetPublicUrlUseCase implements GetPublicUrlInbound {
    private final FileRepository fileRepository;
    private final StorageService storageService;

    @Override
    public String execute(GetPublicUrlCommand command) {
        File file = fileRepository.findByFileUuid(command.fileUuid())
                .orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_FOUND_BY_FILE_UUID, command.fileUuid()));
        if (!PUBLIC.equals(file.getAccessType())) {
            throw new BusinessException(ErrorCode.FILE_HAS_INCORRECT_ACCESS_TYPE, command.fileUuid(), file.getAccessType());
        }

        return storageService.getPublicUrl(file);
    }
}
