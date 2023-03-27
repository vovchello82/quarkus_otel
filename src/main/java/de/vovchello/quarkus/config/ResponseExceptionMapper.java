package de.vovchello.quarkus.config;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

@Provider
public class ResponseExceptionMapper implements ExceptionMapper<Exception> {

    private final static Logger LOGGER = Logger.getLogger(ResponseExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof WebApplicationException) {
            LOGGER.error("error during request processing", exception);

            Response originalErrorResponse = ((WebApplicationException) exception).getResponse();
            return Response.fromResponse(originalErrorResponse)
                    .entity("not applicable request")
                    .build();
        } else if (exception instanceof IllegalArgumentException) {
            LOGGER.error("bad call's arguments", exception);
            return Response.status(400).entity("Request couldn't be processed").build();
        } else if (exception instanceof UnsupportedOperationException) {
            LOGGER.error("call of an unimplemented method", exception);
            return Response.status(501).entity(exception.getMessage()).build();
        } else {
            LOGGER.error("unexpected error on request processing", exception);
            return Response.serverError().entity("Request failed").build();
        }
    }

}
