package de.shop.bestellverwaltung.rest;


import static de.shop.util.TestConstants.BESTELLUNG_ID_EXISTS;
import static de.shop.util.TestConstants.NO_ID;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_URI;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_PATH_PARAM;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.fest.assertions.api.Assertions.assertThat;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.bestellverwaltung.domain.Bestellung;
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
		Long bestellungId = Long.valueOf(BESTELLUNG_ID_EXISTS);
		
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
	@Test
	@InSequence(2)
	public void findBestellungIdNichtVorhanden() {
		
		LOGGER.finer("BEGINN");
		//Given
		Long bestellungId = Long.valueOf(NO_ID);
		
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
	//TODO:		createBestellungKundeNotLoggedIn	(HTTP_FORBIDDEN oder UNAUTHORIZED)
	//TODO:		createBestellungNotOK				(400_BAD_REQUEST)
	/*			evtl. mehrere Methoden (Kunde gibts nicht, Bestellpos falsch...)
	 * 			
	 */
	//TODO:		findKundeByBestellungId				(200)
	//TODO:		findNoKundeByBestellungId			(404)
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
