package org.bazar.adapter.rest.files;

import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.bazar.app.api.InitiateUploadFileInbound;
import org.bazar.app.impl.commands.InitiateUploadCommand;
import org.bazar.app.impl.output.FileInfo;

@Path("/api/v1/files")
public class FileResource {
    private final InitiateUploadFileInbound initiateUploadFileInbound;
    private final RestFileMapper restFileMapper;

    @Inject
    public FileResource(InitiateUploadFileInbound initiateUploadFileInbound, RestFileMapper restFileMapper) {
        this.initiateUploadFileInbound = initiateUploadFileInbound;
        this.restFileMapper = restFileMapper;
    }

    @GET
    @Path("/initiate-upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InitiateUploadResponse initiateUpload(@BeanParam InitiateUploadRequest request) {
        InitiateUploadCommand command = restFileMapper.toInitiateUploadCommand(request);
        FileInfo result = initiateUploadFileInbound.execute(command);
        return restFileMapper.toInitiateUploadResponse(result);
    }
}
