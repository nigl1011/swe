package de.shop.lieferverwaltung.rest;

import static de.shop.util.TestConstants.LIEFERUNG_ID_URI;
import static de.shop.util.TestConstants.BESTELLUNG_ID;
import static de.shop.util.TestConstants.BESTELLUNG_ID_NICHT_VERSCHICKT;
import static de.shop.util.TestConstants.BESTELLUNGEN_URI;
import static de.shop.util.TestConstants.LIEFERUNG_URI;
import static de.shop.util.TestConstants.USERNAME_ADMIN;
import static de.shop.util.TestConstants.PASSWORD_ADMIN;
import static de.shop.util.TestConstants.USERNAME_KUNDE;
import static de.shop.util.TestConstants.PASSWORD_KUNDE;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.util.Locale.GERMAN;
import static org.fest.assertions.api.Assertions.assertThat;
import static javax.ws.rs.client.Entity.json;

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
	            LOGGER.finer("BEGINN findLieferungByIdVorhanden");

	            //Given
	            final Long lieferungId = LIEFERUNG_ID_VORHANDEN;

	            //When
	            final Response response = getHttpsClient().target(LIEFERUNG_ID_URI)
	                            .resolveTemplate(LieferungResource.LIEFERUNG_ID_PATH_PARAM, lieferungId)
	                            .request().acceptLanguage(GERMAN).get();
	            
	            //Then
	            assertThat(response.getStatus()).isEqualTo(HTTP_OK);
	            final Lieferung lieferung = response.readEntity(Lieferung.class);
	            assertThat(lieferung.getId()).isEqualTo(lieferungId);
	            response.close();

	            LOGGER.finer("ENDE findLieferungByIdVorhanden");
	    }
	 
	 
	 	@Test
	    @InSequence(2)
	    public void findLieferungByIdNichtVorhanden() {
	            LOGGER.finer("BEGINN findLieferungByIdNichtVorhanden");
	            
	            //Given
	            final Long lieferungId = LIEFERUNG_ID_NICHT_VORHANDEN;

	            //When
	            final Response response = getHttpsClient().target(LIEFERUNG_ID_URI)
	                            .resolveTemplate(LieferungResource.LIEFERUNG_ID_PATH_PARAM, lieferungId)
	                            .request().acceptLanguage(GERMAN).get();
	            assertThat(response.getStatus()).isEqualTo(HTTP_NOT_FOUND);
	            
	            final String fehlermeldung = response.readEntity(String.class);
	            assertThat(fehlermeldung).startsWith("Keine Lieferung mit der ID").endsWith("gefunden.");
	            response.close();

	            LOGGER.finer("ENDE findLieferungByIdNichtVorhanden");
	    }
	 	
	  	
	  	@Test
	  	@InSequence(3)
	  	public void createLieferungOK () throws URISyntaxException {
		  LOGGER.finer("BEGINN createLieferungOK");
		  
		  //Given
		  final Long bestellungId = BESTELLUNG_ID_NICHT_VERSCHICKT;
		  
		  final Lieferung lieferung = new Lieferung();
		  lieferung.setBestellungUri(new URI(BESTELLUNGEN_URI + "/" + bestellungId));
		  
		  //When
		  Response response = getHttpsClient(USERNAME_ADMIN, PASSWORD_ADMIN).target(LIEFERUNG_URI)
				  .request()
				  .accept(APPLICATION_JSON)
				  .post(json(lieferung));
				  
		  //Then
		  Long id;
		  assertThat(response.getStatus()).isEqualTo(HTTP_CREATED);
		  final String location = response.getLocation().toString();
			
		  response.close();
			
		  final int startPos = location.lastIndexOf('/');
		  final String idStr = location.substring(startPos + 1);
		  id = Long.valueOf(idStr);
		  assertThat(id).isPositive();
			
		  response = getHttpsClient().target(LIEFERUNG_ID_URI)
				  .resolveTemplate(LieferungResource.LIEFERUNG_ID_PATH_PARAM, id)
				  .request()
				  .accept(APPLICATION_JSON)
				  .get();
		  assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		  response.close();
			
		  LOGGER.finer("ENDE createLieferungOK");
	  	}
	  	
	  	
	  	//Test createBestellungNotOK Bestellung schon verschickt
	  	@Test
	  	@InSequence(4)
	  	public void createLieferungNotOK () throws URISyntaxException {
		  LOGGER.finer("BEGINN createLieferungNotOK");
		  
		  //Given
		  final Long bestellungId = BESTELLUNG_ID;
		  
		  final Lieferung lieferung = new Lieferung();
		  lieferung.setBestellungUri(new URI(BESTELLUNGEN_URI + "/" + bestellungId));
		  
		  //When
		  Response response = getHttpsClient(USERNAME_ADMIN, PASSWORD_ADMIN).target(LIEFERUNG_URI)
				  .request()
				  .accept(APPLICATION_JSON)
				  .acceptLanguage(GERMAN)
				  .post(json(lieferung));
				  
		  //Then
		  assertThat(response.getStatus()).isEqualTo(HTTP_BAD_REQUEST);

		  final String fehlermeldung = response.readEntity(String.class);
          assertThat(fehlermeldung).startsWith("Die Bestellung mit der ID").endsWith("verschickt.");
          response.close();
			
		  LOGGER.finer("ENDE createLieferungNotOK");
	  	}
	  	
	  	
	  	//Lieferung wird versucht als kunde zu erstellen
	  	@Test
	  	@InSequence(5)
	  	public void createLieferungForbidden () throws URISyntaxException {
		  LOGGER.finer("BEGINN createLieferungForbidden");
		  
		  //Given
		  final Long bestellungId = BESTELLUNG_ID_NICHT_VERSCHICKT;
		  
		  final Lieferung lieferung = new Lieferung();
		  lieferung.setBestellungUri(new URI(BESTELLUNGEN_URI + "/" + bestellungId));
		  
		  //When
		  Response response = getHttpsClient(USERNAME_KUNDE, PASSWORD_KUNDE).target(LIEFERUNG_URI)
				  .request()
				  .accept(APPLICATION_JSON)
				  .post(json(lieferung));
				  
		  //Then
		  assertThat(response.getStatus()).isEqualTo(HTTP_FORBIDDEN);

          response.close();
			
		  LOGGER.finer("ENDE createLieferungForbidden");
	  	}
}
