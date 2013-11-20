package de.shop.bestellverwaltung.rest;

import static de.shop.util.TestConstants.BESTELLUNG_ID_EXISTS;
import static de.shop.util.TestConstants.NO_ID;
import static de.shop.util.TestConstants.VERSION;
import static de.shop.util.TestConstants.GESAMTPREIS;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_URI;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_PATH_PARAM;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_KUNDE_URI;
import static de.shop.util.TestConstants.KUNDEN_ID_URI;
import static de.shop.util.TestConstants.KUNDEN_ID_PATH_PARAM;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static de.shop.util.TestConstants.ARTIKEL_STUHL;
import static de.shop.util.TestConstants.ARTIKEL_DOPPELBETT;
import static de.shop.util.TestConstants.ARTIKEL_URI;
import static de.shop.util.TestConstants.USERNAME;
import static de.shop.util.TestConstants.PASSWORD;
import static de.shop.util.TestConstants.BESTELLUNGEN_URI;
import static javax.ws.rs.client.Entity.json;

import java.net.URI;
import java.net.URISyntaxException;

import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;


import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.bestellverwaltung.domain.Bestellposten;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.domain.StatusType;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.util.AbstractResourceTest;


@RunWith(Arquillian.class)
public class BestellungResourceTest extends AbstractResourceTest {

	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	
	/* Suche nach exisitierender BestellId
	 * 
	 */
	@Ignore
	@Test
	@InSequence(1)
	public void findBestellungByIdVorhanden() {
		
		LOGGER.finer("BEGINN");
		//Given
		final Long bestellungId = Long.valueOf(BESTELLUNG_ID_EXISTS);
		
		//When
		final Response response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
				                                  .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
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
	@Ignore
	@Test
	@InSequence(2)
	public void findBestellungIdNichtVorhanden() {
		
		LOGGER.finer("BEGINN");
		//Given
		final Long bestellungId = Long.valueOf(NO_ID);
		
		//When
		
		final Response response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
												  .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
												  .request()
												  .accept(APPLICATION_JSON)
												  .get();
		
		//Then
		assertThat(response.getStatus()).isEqualTo(HTTP_NOT_FOUND);
		LOGGER.finer("ENDE");
		
		
		
	}

	//TODO:  	createBestellungOK 					(204)
	//Internal Server Error 500, irgendwie klappt die �bergabe des Username nicht..
	@Ignore
	@Test
	@InSequence(3)
	public void createBestellungOK () throws URISyntaxException {
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
		bestellung.setVersion(VERSION);
		bestellung.setGesamtpreis(GESAMTPREIS);
		
		
		

		// When
		Long id;
					
		 Response response = getHttpsClient(USERNAME, PASSWORD).target(BESTELLUNGEN_URI)
						  			     .request()
						  			     .accept(APPLICATION_JSON)
						  			     .post(json(bestellung));
		
		
		//Then
		assertThat(response.getStatus()).isEqualTo(HTTP_CREATED);
		final String location = response.getLocation().toString();
		
		
		final int startPos = location.lastIndexOf('/');
		final String idStr = location.substring(startPos + 1);
		id = Long.valueOf(idStr);
		assertThat(id).isPositive();
		
		response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
				.resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, id)
				.request()
				.accept(APPLICATION_JSON)
				.get();
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		response.close();
		
		
		LOGGER.finer("ENDE createBestellungOK");
	}
	//TODO:		createBestellungKundeNotLoggedIn	(HTTP_FORBIDDEN oder UNAUTHORIZED)
	//TODO:		createBestellungNotOK				(400_BAD_REQUEST)
	/*			evtl. mehrere Methoden (Kunde gibts nicht, Bestellpos falsch...)
	 * 			
	 */
	@Ignore
	@Test
	@InSequence(4)
	public void findKundeByBestellungId() {
		LOGGER.finer("BEGINN");
		
		//Given
		final Long bestellungId = Long.valueOf(BESTELLUNG_ID_EXISTS);
			
		
		/*
		 * Zun�chst Kundenobjekt anhand ID suchen
		 */
		Response response = getHttpsClient().target(BESTELLUNGEN_ID_KUNDE_URI)
				 								  .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
				 								  .request()
				 								  .accept(APPLICATION_JSON)
				 								  .get();
		//Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		/*
		 * Wenn HTTP_OK, dann Kundenobjekt auslesen und �berpr�fen ob dieses nicht NULL ist,
		 * anschlie�end Kunde anhand der URI und der gefundenen ID suchen
		 */
		final AbstractKunde kunde = response.readEntity(AbstractKunde.class);
		assertThat(kunde).isNotNull();
		
		response = getHttpsClient().target(KUNDEN_ID_URI)
								  .resolveTemplate(KUNDEN_ID_PATH_PARAM, kunde.getId())
								  .request()
								  .accept(APPLICATION_JSON)
								  .get();
		
		/*
		 * Abschlie�end nochmals �berpr�fen auf HTTP_OK und ob die Links auch gesetzt sind
		 */
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		assertThat(response.getLinks()).isNotEmpty();
		
		LOGGER.finer("ENDE");
		
	}
	
		
	//TODO:		findNoKundeByBestellungId			(404)
	/*
	 * Die Methode macht doch gar kein Sinn?! Wir haben kein DeleteKunde, und beim create wird validiert, dass 
	 * der Kunde auch existiert.
	 */
	//TODO: 	findLieferungByBestellungId			(200)
	/*
	 * 			lieferung und status verschickt
	 * 			
	 * 			
	 * 			 			
	 * 			
	 */
	//TODO:		findNoLieferungByBestellungId		(404)
	/*
	 * 			keine Lieferung und IB
	 * 			
	 */
	


	
}
