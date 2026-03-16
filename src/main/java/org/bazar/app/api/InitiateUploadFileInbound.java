package org.bazar.app.api;

import org.bazar.domain.File;
import org.bazar.domain.FileInfo;

public interface InitiateUploadFileInbound {
    FileInfo execute(File file);
}
