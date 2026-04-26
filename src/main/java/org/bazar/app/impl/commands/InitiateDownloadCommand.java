package org.bazar.app.impl.commands;

import java.util.UUID;

public record InitiateDownloadCommand(UUID fileUuid) {
}
