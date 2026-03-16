package org.bazar.adapter.rest.files;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.bazar.app.api.InitiateUploadFileInbound;
import org.bazar.domain.File;
import org.bazar.domain.FileInfo;

@Path("/api/files")
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
    public InitiateUploadResponse initiateUpload(@QueryParam("fileName") String fileName, @QueryParam("size") Integer size,
                                 @QueryParam("contentType") String contentType, @QueryParam("domain") String domain) {
        File file = restFileMapper.toDomain(fileName, size, contentType, domain);
        FileInfo result = initiateUploadFileInbound.execute(file);
        return restFileMapper.toInitiateUploadResponse(result);
    }
}
