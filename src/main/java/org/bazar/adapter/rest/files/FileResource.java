package org.bazar.adapter.rest.files;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.bazar.app.api.InitiateUploadFileInbound;

@Path("/files")
public class FileResource {
    private final InitiateUploadFileInbound initiateUploadFileInbound;

    @Inject
    public FileResource(InitiateUploadFileInbound initiateUploadFileInbound) {
        this.initiateUploadFileInbound = initiateUploadFileInbound;
    }

    @POST
    @Path("/initiate-upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String initiateUpload(InitiateUploadRequest request) {
        return initiateUploadFileInbound.execute(request.extension());
    }
}
