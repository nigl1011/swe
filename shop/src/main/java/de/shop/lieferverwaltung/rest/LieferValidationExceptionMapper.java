package de.shop.lieferverwaltung.rest;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.CONFLICT;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.shop.lieferverwaltung.domain.Lieferung;
import de.shop.lieferverwaltung.service.LieferValidationException;
import de.shop.util.Log;


@Provider
@ApplicationScoped
@Log
public class LieferValidationExceptionMapper implements ExceptionMapper<LieferValidationException> {
	private static final String NEWLINE = System.getProperty("line.separator");

	@Override
	public Response toResponse(LieferValidationException e) {
		final Collection<ConstraintViolation<Lieferung>> violations = e.getViolations();
		final StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<Lieferung> v : violations) {
			sb.append(v.getMessage());
			sb.append(NEWLINE);
		}
		
		final String responseStr = sb.toString();
		final Response response = Response.status(CONFLICT)
		                                  .type(TEXT_PLAIN)
		                                  .entity(responseStr)
		                                  .build();
		return response;
	}

}
