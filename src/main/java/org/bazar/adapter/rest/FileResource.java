package org.bazar.adapter.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.bazar.app.api.SaveTempFileInbound;

@Path("/hello")
public class FileResource {
    private final SaveTempFileInbound saveTempFileInbound;

    @Inject
    public FileResource(SaveTempFileInbound saveTempFileInbound) {
        this.saveTempFileInbound = saveTempFileInbound;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return saveTempFileInbound.execute();
    }
}
