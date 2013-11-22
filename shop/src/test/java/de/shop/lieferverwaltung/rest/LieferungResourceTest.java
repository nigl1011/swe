package de.shop.lieferverwaltung.rest;

import static de.shop.util.TestConstants.LIEFERUNG_ID_URI;
import static de.shop.util.TestConstants.BESTELLUNG_ID;
import static de.shop.util.TestConstants.BESTELLUNGEN_URI;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Locale.GERMAN;
import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.lieferverwaltung.domain.Lieferung;
import de.shop.util.AbstractResourceTest;

@RunWith(Arquillian.class)
public class LieferungResourceTest extends AbstractResourceTest {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	private static final Long LIEFERUNG_ID_VORHANDEN = Long.valueOf(700);
	private static final Long LIEFERUNG_ID_NICHT_VORHANDEN = Long.valueOf(777);
	
	
	 @Test
	    @InSequence(1)
	    public void findLieferungByIdVorhanden() {
	            LOGGER.finer("BEGINN");

	            final Long lieferungId = LIEFERUNG_ID_VORHANDEN;

	            final Response response = getHttpsClient().target(LIEFERUNG_ID_URI)
	                            .resolveTemplate(LieferungResource.LIEFERUNG_ID_PATH_PARAM, lieferungId)
	                            .request().acceptLanguage(GERMAN).get();
	            assertThat(response.getStatus()).isEqualTo(HTTP_OK);
	            final Lieferung lieferung = response.readEntity(Lieferung.class);
	            assertThat(lieferung.getId()).isEqualTo(lieferungId);

	            LOGGER.finer("ENDE");
	    }
	 
	  @Test
	    @InSequence(2)
	    public void findLieferungByIdNichtVorhanden() {
	            LOGGER.finer("BEGINN");

	            final Long lieferungId = LIEFERUNG_ID_NICHT_VORHANDEN;

	            final Response response = getHttpsClient().target(LIEFERUNG_ID_URI)
	                            .resolveTemplate(LieferungResource.LIEFERUNG_ID_PATH_PARAM, lieferungId)
	                            .request().acceptLanguage(GERMAN).get();
	            assertThat(response.getStatus()).isEqualTo(HTTP_NOT_FOUND);
	            final String fehlermeldung = response.readEntity(String.class);
	            assertThat(fehlermeldung).startsWith("Keine Lieferung mit der ID").endsWith("gefunden.");

	            LOGGER.finer("ENDE");
	    }
	  
	//TODO: Methoden die noch ergänzt werden muss
		/*
		 * createLieferung (OK, Forbidden, BadRequest(Nicht ok))
		 * findBestellungByLieferungId(OK, //notFound) // -> evtl. unnötig
		 */
	  
	  @Test
	  	@InSequence(3)
	  	public void createLieferungOK () throws URISyntaxException {
		  LOGGER.finer("BEGINN");
		  
		  final Long bestellungId = BESTELLUNG_ID;
		  
		  final Lieferung lieferung = new Lieferung();
		  lieferung.setBestellungUri(new URI(BESTELLUNGEN_URI + "/" + bestellungId));
		  
		  LOGGER.finer("ENDE");
	  }
}
