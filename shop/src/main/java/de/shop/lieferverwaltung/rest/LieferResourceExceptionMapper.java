package de.shop.lieferverwaltung.rest;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.CONFLICT;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.shop.lieferverwaltung.service.AbstractLieferServiceException;
import de.shop.util.interceptor.Log;


@Provider
@ApplicationScoped
@Log
public class LieferResourceExceptionMapper implements ExceptionMapper<AbstractLieferServiceException> {
	@Override
	public Response toResponse(AbstractLieferServiceException e) {
		final String msg = e.getMessage();
		final Response response = Response.status(CONFLICT)
		                                  .type(TEXT_PLAIN)
		                                  .entity(msg)
		                                  .build();
		return response;
	}

}
