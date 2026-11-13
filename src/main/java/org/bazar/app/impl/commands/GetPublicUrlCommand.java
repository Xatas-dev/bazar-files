package org.bazar.app.impl.commands;

import java.util.UUID;

public record GetPublicUrlCommand(
        UUID fileUuid
) {}
