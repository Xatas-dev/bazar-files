package org.bazar.app.api;

import org.bazar.domain.File;

public interface NotifyFileUploadedOutbound {
    void execute(File event);
}
