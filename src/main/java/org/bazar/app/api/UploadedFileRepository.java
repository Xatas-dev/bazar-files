package org.bazar.app.api;

import org.bazar.domain.UploadedFile;

import java.util.List;

public interface UploadedFileRepository {
    void save(UploadedFile uploadedFile);
    List<UploadedFile> findAll();
}
