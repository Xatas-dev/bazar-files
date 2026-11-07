package org.bazar.app.api;

import org.bazar.app.impl.commands.GetPublicUrlCommand;

public interface GetPublicUrlInbound {
    String execute(GetPublicUrlCommand command);
}
