package org.bazar.adapter.webhook;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bazar.app.api.HandleFileUploadedInbound;
import org.bazar.app.impl.commands.HandleFileUploadedCommand;

/**
 * REST ресурс для обработки webhook-событий от rustfs
 */
@Path("/webhook")
@Slf4j
@RequiredArgsConstructor
public class WebHookResource {
    private static final String CREATED_PUT_EVENT_NAME = "s3:ObjectCreated:Put";

    private final HandleFileUploadedInbound handleFileUploadedInbound;
    private final RestS3Mapper restS3Mapper;

    @POST
    @Path("/files/file-uploaded")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handleFileUploaded(S3WebhookEvent event) {
        try {
            if (CREATED_PUT_EVENT_NAME.equals(event.eventName())) {
                HandleFileUploadedCommand command = restS3Mapper.toDomain(event.records().getFirst().s3().object());
                handleFileUploadedInbound.execute(command);
            }
        } catch (Exception e) {
            log.error("Error handling file uploaded event", e);
        }
        return Response.ok().build();
    }
}