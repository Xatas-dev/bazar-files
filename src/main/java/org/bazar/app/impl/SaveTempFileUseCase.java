package org.bazar.app.impl;

import org.bazar.app.api.SaveTempFileInbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.app.api.UploadedFileRepository;
import org.bazar.domain.UploadedFile;

public class SaveTempFileUseCase implements SaveTempFileInbound {
    private final UploadedFileRepository uploadedFileRepository;
    // Вместо UnitOfWork можно использовать декоратор, который будет реализовывать SaveTempFileInbound, но для простоты
    // решил использовать общий порт для всех юзкейсов, которые требуют транзакцию. Без общего порта пришлось бы
    // создавать отдельный интерфейс для каждого юзкейса, который требует транзакцию, что может привести к избыточности кода
    // Но это, конечно, немного загрязняет архитектуру
    private final UnitOfWork unitOfWork;

    public SaveTempFileUseCase(UploadedFileRepository uploadedFileRepository, UnitOfWork unitOfWork) {
        this.uploadedFileRepository = uploadedFileRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public String execute() {
        return unitOfWork.perform(() -> {
            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setFileUuid("e6e6684f-cc0a-48d3-8d74-85950850ad76");
            uploadedFileRepository.save(uploadedFile);

            return uploadedFileRepository.findAll().getFirst().getFileUuid();
        });
    }
}
