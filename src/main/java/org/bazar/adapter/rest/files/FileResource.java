package org.bazar.adapter.rest.files;

import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.bazar.app.api.MarkFileForDeletionInbound;
import org.bazar.app.api.InitiateDownloadFileInbound;
import org.bazar.app.api.InitiateUploadFileInbound;
import org.bazar.app.impl.commands.InitiateDownloadCommand;
import org.bazar.app.impl.commands.InitiateUploadCommand;
import org.bazar.app.impl.output.FileInfo;

import java.util.UUID;

@Path("/api/v1/files")
@RequiredArgsConstructor
public class FileResource {
    private final InitiateUploadFileInbound initiateUploadFileInbound;
    private final InitiateDownloadFileInbound initiateDownloadFileInbound;
    private final MarkFileForDeletionInbound markFileForDeletionInbound;
    private final RestFileMapper restFileMapper;

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

    @DELETE
    @Path("/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteFileByUuid(@PathParam("fileUuid") String fileUuid) {
        markFileForDeletionInbound.execute(UUID.fromString(fileUuid));
    }
}
