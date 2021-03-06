package de.shop.bestellverwaltung.rest;

import static de.shop.util.TestConstants.BESTELLUNG_ID_EXISTS;
import static de.shop.util.TestConstants.NO_ID;
import static de.shop.util.TestConstants.BESTELLUNG_ID_URI;
import static de.shop.util.TestConstants.BESTELLUNG_ID_PATH_PARAM;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static de.shop.util.TestConstants.ARTIKEL_STUHL;
import static de.shop.util.TestConstants.ARTIKEL_DOPPELBETT;
import static de.shop.util.TestConstants.ARTIKEL_URI;
import static de.shop.util.TestConstants.USERNAME_MITARBEITER;
import static de.shop.util.TestConstants.PASSWORD_MITARBEITER;
import static de.shop.util.TestConstants.BESTELLUNG_URI;
import static de.shop.util.TestConstants.ARTIKEL_ANZAHL_INVALID;
import static java.util.Locale.ENGLISH;
import static javax.ws.rs.client.Entity.json;

import java.net.URI;
import java.net.URISyntaxException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.filter;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;





import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ViolationReport;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.bestellverwaltung.domain.Bestellposten;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.domain.StatusType;
import de.shop.util.AbstractResourceTest;


@RunWith(Arquillian.class)
public class BestellungResourceTest extends AbstractResourceTest {

	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	
	/* Suche nach exisitierender BestellId
	 * 
	 */
	@Test
	@InSequence(1)
	public void findBestellungByIdVorhanden() {
		
		LOGGER.finer("BEGINN");
		//Given
		final Long bestellungId = Long.valueOf(BESTELLUNG_ID_EXISTS);
		
		//When
		final Response response = getHttpsClient().target(BESTELLUNG_ID_URI)
				                                  .resolveTemplate(BESTELLUNG_ID_PATH_PARAM, bestellungId)
				                                  .request()
				                                  .accept(APPLICATION_JSON)
				                                  .get();
		
		//Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		final Bestellung bestellung = response.readEntity(Bestellung.class);
		
		assertThat(bestellung.getId()).isEqualTo(bestellungId);
		assertThat(bestellung.getBestellposten()).isNotEmpty();
		LOGGER.finer("ENDE");
	}
	
	/* Suche nach einer BestellungId die NICHT existiert
	 * 
	 */
	@Test
	@InSequence(2)
	public void findBestellungIdNichtVorhanden() {
		
		LOGGER.finer("BEGINN");
		//Given
		final Long bestellungId = Long.valueOf(NO_ID);
		
		//When
		
		final Response response = getHttpsClient().target(BESTELLUNG_ID_URI)
												  .resolveTemplate(BESTELLUNG_ID_PATH_PARAM, bestellungId)
												  .request()
												  .accept(APPLICATION_JSON)
												  .get();
		
		//Then
		assertThat(response.getStatus()).isEqualTo(HTTP_NOT_FOUND);
		LOGGER.finer("ENDE");
		
		
		
	}


	@Test
	@InSequence(3)
	public void createBestellungOK() throws URISyntaxException {
		LOGGER.finer("BEGINN createBestellungOK");
		
		// Given
		final Long artikelId1 = ARTIKEL_STUHL;
		final Long artikelId2 = ARTIKEL_DOPPELBETT;
				
		final Bestellung bestellung = new Bestellung();
		
		//Ich vermute das hier der fehler entsteht...
		final Bestellposten bp = new Bestellposten();
		bp.setArtikelUri(new URI(ARTIKEL_URI + "/" + artikelId1));
		bp.setAnzahl((short) 1);
		bestellung.addBestellposition(bp);
		
		final Bestellposten bp1 = new Bestellposten();
		bp1.setArtikelUri(new URI(ARTIKEL_URI + "/" + artikelId2));
		bp1.setAnzahl((short) 1);
		bestellung.addBestellposition(bp1);
		bestellung.setStatus(StatusType.INBEARBEITUNG);
		
		
		// When		
		 Response response = getHttpsClient(USERNAME_MITARBEITER, PASSWORD_MITARBEITER).target(BESTELLUNG_URI)
						  			     .request()
						  			     .accept(APPLICATION_JSON)
						  			     .post(json(bestellung));
		
		
		//Then
		 Long id;
		assertThat(response.getStatus()).isEqualTo(HTTP_CREATED);
		final String location = response.getLocation().toString();
		
		response.close();
		
		final int startPos = location.lastIndexOf('/');
		final String idStr = location.substring(startPos + 1);
		id = Long.valueOf(idStr);
		assertThat(id).isPositive();
		
		response = getHttpsClient().target(BESTELLUNG_ID_URI)
				.resolveTemplate(BESTELLUNG_ID_PATH_PARAM, id)
				.request()
				.accept(APPLICATION_JSON)
				.get();
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		response.close();
		
		
		LOGGER.finer("ENDE createBestellungOK");
	}
	
	@Test
	@InSequence(4)
	public void createBestellungKundeNotLoggedIn() throws URISyntaxException {
		LOGGER.finer("BEGINN createBestellungKundeNotLoggedIn");
		// Given
		final Long artikelId1 = ARTIKEL_STUHL;
		final Long artikelId2 = ARTIKEL_DOPPELBETT;
				
		final Bestellung bestellung = new Bestellung();
		
		
		final Bestellposten bp = new Bestellposten();
		bp.setArtikelUri(new URI(ARTIKEL_URI + "/" + artikelId1));
		bp.setAnzahl((short) 1);
		bestellung.addBestellposition(bp);
		
		final Bestellposten bp1 = new Bestellposten();
		bp1.setArtikelUri(new URI(ARTIKEL_URI + "/" + artikelId2));
		bp1.setAnzahl((short) 1);
		bestellung.addBestellposition(bp1);
		bestellung.setStatus(StatusType.INBEARBEITUNG);
				
		// When		
		final Response response = getHttpsClient().target(BESTELLUNG_URI)
						  			     .request()
						  			     .accept(APPLICATION_JSON)
						  			     .post(json(bestellung));
		
		//Then
		assertThat(response.getStatus()).isEqualTo(HTTP_UNAUTHORIZED);
		
		response.close();
		
		LOGGER.finer("ENDE createBestellungKundeNotLoggedIn");
	}
	
	/*
	 * Test von @Valid
	 * 			Bestellposition
	 * konkret: Anzahl f�r einen Artikel muss gr��er 0 sein!
	 */
	@Test
	@InSequence(6)
	public void createBestellungPosNotValid() throws URISyntaxException {
		LOGGER.finer("Beginn createBestellungPosNotValid");
		
		// Given
		final Long artikelId1 = ARTIKEL_STUHL;
	
		
		final Bestellung bestellung = new Bestellung();
		
		final Bestellposten bp = new Bestellposten();
		bp.setArtikelUri(new URI(ARTIKEL_URI + "/" + artikelId1));
		/*
		 * ung�ltige Anzahl
		 */
		final short ungueltigeAnzahl = ARTIKEL_ANZAHL_INVALID;
		bp.setAnzahl(ungueltigeAnzahl);
		bestellung.addBestellposition(bp);
		
		final Response response = getHttpsClient(USERNAME_MITARBEITER, PASSWORD_MITARBEITER).target(BESTELLUNG_URI)
																	.request()
																	.accept(APPLICATION_JSON)
																	.acceptLanguage(ENGLISH)
																	.post(json(bestellung));
		//Then
		assertThat(response.getStatus()).isEqualTo(HTTP_BAD_REQUEST);
		assertThat(response.getHeaderString("validation-exception")).isEqualTo("true");
		final ViolationReport violationReport = response.readEntity(ViolationReport.class);
		response.close();
		
		final List<ResteasyConstraintViolation> violations = violationReport.getParameterViolations();
		assertThat(violations).isNotEmpty();
		
		final ResteasyConstraintViolation violation = 
									filter(violations).with("message")
													  .equalsTo("At least one order item is required.")
													  .get()
													  .iterator()
													  .next();
		assertThat(violation.getValue()).isEqualTo(String.valueOf(ungueltigeAnzahl));
		
		LOGGER.finer("ENDE createBestellungPosNotValid");

		
	}
}
