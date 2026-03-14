package org.bazar.app.api;

import org.bazar.domain.File;

import java.util.List;

public interface UploadedFileRepository {
    void save(File file);
    List<File> findAll();
}
