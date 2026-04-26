package org.bazar.adapter.rest.files;

import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.bazar.app.api.InitiateDownloadFileInbound;
import org.bazar.app.api.InitiateUploadFileInbound;
import org.bazar.app.impl.commands.InitiateDownloadCommand;
import org.bazar.app.impl.commands.InitiateUploadCommand;
import org.bazar.app.impl.output.FileInfo;

import java.util.UUID;

@Path("/api/v1/files")
public class FileResource {
    private final InitiateUploadFileInbound initiateUploadFileInbound;
    private final InitiateDownloadFileInbound initiateDownloadFileInbound;
    private final RestFileMapper restFileMapper;

    @Inject
    public FileResource(InitiateUploadFileInbound initiateUploadFileInbound, InitiateDownloadFileInbound initiateDownloadFileInbound, RestFileMapper restFileMapper) {
        this.initiateUploadFileInbound = initiateUploadFileInbound;
        this.initiateDownloadFileInbound = initiateDownloadFileInbound;
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

    @GET
    @Path("/initiate-download")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InitiateDownloadResponse initiateDownload(@QueryParam("fileUuid") String fileUuid) {
        InitiateDownloadCommand command = restFileMapper.toInitiateDownloadCommand(UUID.fromString(fileUuid));
        String downloadUrl = initiateDownloadFileInbound.execute(command);
        return restFileMapper.toInitiateDownloadResponse(downloadUrl);
    }
}
