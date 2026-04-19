package org.bazar.adapter.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.bazar.app.api.exception.BusinessException;

@Provider
public class RestExceptionHandler implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException exception) {
        return Response.status(exception.getErrorCode().getStatus())
                .entity(exception.getMessage())
                .build();
    }
}
