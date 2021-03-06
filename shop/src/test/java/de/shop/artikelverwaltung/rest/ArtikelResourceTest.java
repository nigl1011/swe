package de.shop.artikelverwaltung.rest;

import static de.shop.util.TestConstants.ARTIKEL_ID_URI;
import static de.shop.util.TestConstants.ARTIKEL_URI;


import static de.shop.util.TestConstants.PASSWORD_MITARBEITER;
import static de.shop.util.TestConstants.USERNAME_MITARBEITER;
import static de.shop.util.TestConstants.USERNAME_KUNDE;
import static de.shop.util.TestConstants.PASSWORD_KUNDE;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.util.Locale.GERMAN;
import static org.fest.assertions.api.Assertions.assertThat;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;
import java.math.BigDecimal;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.domain.KategorieType;
import de.shop.util.AbstractResourceTest;





@RunWith(Arquillian.class)
public class ArtikelResourceTest extends AbstractResourceTest {
private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	private static final Long ARTIKEL_ID_VORHANDEN = Long.valueOf(301);
	private static final Long ARTIKEL_ID_NICHT_VORHANDEN = Long.valueOf(350);
	
	private static final String NEUE_BEZEICHNUNG = "Neuerartikel";
	private static final int NEUE_VERSION = 1;
	private static final String NEUE_FARBE = "Neuefarbe";
	private static final BigDecimal NEUER_PREIS = new BigDecimal(75.5);
	private static final boolean NEUE_VERFUEGBARKEIT = true;
	private static final KategorieType NEUE_KATEGORIE = KategorieType.KUECHE;

	
    @Test
    @InSequence(1)
    public void findArtikelByIdOk() {
            LOGGER.finer("BEGINN findArtikelByIdOk");

            //Given
            final Long artikelId = ARTIKEL_ID_VORHANDEN;

            //When
            final Response response = getHttpsClient().target(ARTIKEL_ID_URI)
                            .resolveTemplate(ArtikelResource.ARTIKEL_ID_PATH_PARAM, artikelId)
                            .request().acceptLanguage(GERMAN).get();
            
            //Then
            assertThat(response.getStatus()).isEqualTo(HTTP_OK);
            final Artikel artikel = response.readEntity(Artikel.class);
            assertThat(artikel.getId()).isEqualTo(artikelId);

            LOGGER.finer("ENDE findArtikelByIdOk");
    }

    @Test
    @InSequence(2)
    public void findArtikelByIdNotFound() {
            LOGGER.finer("BEGINN findArtikelByIdNotFound");
            
            //Given
            final Long artikelId = ARTIKEL_ID_NICHT_VORHANDEN;

            //When
            final Response response = getHttpsClient().target(ARTIKEL_ID_URI)
                            .resolveTemplate(ArtikelResource.ARTIKEL_ID_PATH_PARAM, artikelId)
                            .request().acceptLanguage(GERMAN).get();
            
            //Then
            assertThat(response.getStatus()).isEqualTo(HTTP_NOT_FOUND);
            final String fehlermeldung = response.readEntity(String.class);
            assertThat(fehlermeldung).startsWith("Kein Artikel mit der ID").endsWith("gefunden.");

            LOGGER.finer("ENDE findArtikelByIdNotFound");
    }

    @Test
    @InSequence(3)
    public void updateArtikelNotFound() {
            LOGGER.finer("BEGINN updateArtikelNotFound");
            
            //Given
            final Long artikelId = ARTIKEL_ID_NICHT_VORHANDEN;

            //When
           final Response response = getHttpsClient().target(ARTIKEL_ID_URI)
                            					.resolveTemplate(ArtikelResource.ARTIKEL_ID_PATH_PARAM, artikelId)
                            					.request()
                            					.accept(APPLICATION_JSON)
                            					.get();
            
            
            //Then
            assertThat(response.getStatus()).isEqualTo(HTTP_NOT_FOUND);

            response.close();
            
            LOGGER.finer("ENDE updateArtikelNotFound");
    }
    
    @Test
    @InSequence(4)
    public void updateArtikelUnauthorized() {
            LOGGER.finer("BEGINN updateArtikelUnauthorized");
            
            //Given
            final Long artikelId = ARTIKEL_ID_VORHANDEN;
            final String neueFarbe = NEUE_FARBE;

            
            Response response = getHttpsClient().target(ARTIKEL_ID_URI)
                            					.resolveTemplate(ArtikelResource.ARTIKEL_ID_PATH_PARAM, artikelId)
                            					.request()
                            					.accept(APPLICATION_JSON)
                            					.get();
            
            final Artikel artikel = response.readEntity(Artikel.class);
            assertThat(artikel.getId()).isEqualTo(artikelId);

            response.close();
            
            artikel.setFarbe(neueFarbe);

            //When
            response = getHttpsClient().target(ARTIKEL_URI)
            							.request()
            							.accept(APPLICATION_JSON)
            							.put(json(artikel));
            
            //Then
            assertThat(response.getStatus()).isEqualTo(HTTP_UNAUTHORIZED);

            response.close();
            
            LOGGER.finer("ENDE updateArtikelUnauthorized");
    }

    
    @Test
    @InSequence(5)
    public void updateArtikelNoContent() {
            LOGGER.finer("BEGINN updateArtikelNoContent");
            
            //Given
            final Long artikelId = ARTIKEL_ID_VORHANDEN;
            final String neueFarbe = NEUE_FARBE;

            
            Response response = getHttpsClient().target(ARTIKEL_ID_URI)
                            					.resolveTemplate(ArtikelResource.ARTIKEL_ID_PATH_PARAM, artikelId)
                            					.request()
                            					.accept(APPLICATION_JSON)
                            					.get();
            
            Artikel artikel = response.readEntity(Artikel.class);
            assertThat(artikel.getId()).isEqualTo(artikelId);

            response.close();
            
            artikel.setFarbe(neueFarbe);

            //When
            response = getHttpsClient(USERNAME_MITARBEITER, PASSWORD_MITARBEITER).target(ARTIKEL_URI)
            																		.request()
            																		.accept(APPLICATION_JSON)
            																		.put(json(artikel));
            
            //Then
            assertThat(response.getStatus()).isEqualTo(HTTP_OK);

            response.close();
            
            response = getHttpsClient().target(ARTIKEL_ID_URI)
                    							.resolveTemplate(ArtikelResource.ARTIKEL_ID_PATH_PARAM, artikelId)
                    							.request()
                    							.accept(APPLICATION_JSON)
                    							.get();

            artikel = response.readEntity(Artikel.class);
            assertThat(artikel.getId()).isEqualTo(artikelId);
            assertThat(artikel.getFarbe()).isEqualTo(neueFarbe);
            
            response.close();
            
            LOGGER.finer("ENDE updateArtikelNoContent");
    }
    
    @Test
    @InSequence(6)
    public void updateArtikelForbidden() {
            LOGGER.finer("BEGINN");
            
            //Given
            final Long artikelId = ARTIKEL_ID_VORHANDEN;
            final String neueFarbe = NEUE_FARBE;

            
            Response response = getHttpsClient().target(ARTIKEL_ID_URI)
                            					.resolveTemplate(ArtikelResource.ARTIKEL_ID_PATH_PARAM, artikelId)
                            					.request()
                            					.accept(APPLICATION_JSON)
                            					.get();
            
            final Artikel artikel = response.readEntity(Artikel.class);
            assertThat(artikel.getId()).isEqualTo(artikelId);

            response.close();
            
            artikel.setFarbe(neueFarbe);

            //When
            response = getHttpsClient(USERNAME_KUNDE, PASSWORD_KUNDE).target(ARTIKEL_URI)
            																		.request()
            																		.accept(APPLICATION_JSON)
            																		.put(json(artikel));
            
            //Then
            assertThat(response.getStatus()).isEqualTo(HTTP_FORBIDDEN);

            response.close();
            
            LOGGER.finer("ENDE updateArtikelForbidden");
    }
    
    @Test
    @InSequence(7)
    public void createArtikelUnauthorized() {
    	LOGGER.finer("BEGIN createArtikelUnauthorized");
    	//Given
    	final Artikel artikel = new Artikel(); 
    	artikel.setBezeichnung(NEUE_BEZEICHNUNG);
    	artikel.setVersion(NEUE_VERSION);
    	artikel.setKategorie(KategorieType.KUECHE);
    	artikel.setPreis(NEUER_PREIS);
    	artikel.setVerfuegbar(NEUE_VERFUEGBARKEIT);
    	artikel.setFarbe(NEUE_FARBE);
    	
    	//When
    	final Response response = getHttpsClient().target(ARTIKEL_URI)
					.request()
					.accept(APPLICATION_JSON)
					.post(json(artikel));
    	
    	//Then
    	assertThat(response.getStatus()).isEqualTo(HTTP_UNAUTHORIZED);
    	
    	response.close();
    	
    	LOGGER.finer("ENDE createArtikelUnauthorized");
    }
    
    
    @Test
    @InSequence(8)
    public void createArtikelForbidden() {
    	LOGGER.finer("BEGIN createArtikelUnauthorized");
    	
    	//Given
    	final Artikel artikel = new Artikel(); 
    	artikel.setBezeichnung(NEUE_BEZEICHNUNG);
    	artikel.setVersion(NEUE_VERSION);
    	artikel.setKategorie(KategorieType.KUECHE);
    	artikel.setPreis(NEUER_PREIS);
    	artikel.setVerfuegbar(NEUE_VERFUEGBARKEIT);
    	artikel.setFarbe(NEUE_FARBE);
    	
    	//When
    	final Response response = getHttpsClient(USERNAME_KUNDE, PASSWORD_KUNDE).target(ARTIKEL_URI)
					.request()
					.accept(APPLICATION_JSON)
					.post(json(artikel));
    	
    	//Then
    	assertThat(response.getStatus()).isEqualTo(HTTP_FORBIDDEN);
    	
    	response.close();
    	
    	LOGGER.finer("ENDE createArtikelUnauthorized");
    }
    
    @Test
    @InSequence(9)
    public void createArtikelCreated() {
    	LOGGER.finer("BEGIN createArtikelCreated");
    	
    	//Given
    	final Artikel artikel = new Artikel(); 
    	artikel.setBezeichnung(NEUE_BEZEICHNUNG);
    	artikel.setVersion(NEUE_VERSION);
    	artikel.setKategorie(NEUE_KATEGORIE);
    	artikel.setPreis(NEUER_PREIS);
    	artikel.setVerfuegbar(NEUE_VERFUEGBARKEIT);
    	artikel.setFarbe(NEUE_FARBE);
    	
    	//When
    	Response response = getHttpsClient(USERNAME_MITARBEITER, PASSWORD_MITARBEITER).target(ARTIKEL_URI)
					.request()
					.accept(APPLICATION_JSON)
					.post(json(artikel));
    	
    	
    	//Then
    	assertThat(response.getStatus()).isEqualTo(HTTP_CREATED);
		final String location = response.getLocation().toString();
    	
    	response.close();
    	
    	final int startPos = location.lastIndexOf('/');
		final String idStr = location.substring(startPos + 1);
		final Long id = Long.valueOf(idStr);
		assertThat(id).isPositive();
		
    	response = getHttpsClient().target(ARTIKEL_ID_URI)
                .resolveTemplate(ArtikelResource.ARTIKEL_ID_PATH_PARAM, id)
                .request().accept(APPLICATION_JSON).get();
    	
    	assertThat(response.getStatus()).isEqualTo(HTTP_OK);
    	
    	response.close();
    	
    	LOGGER.finer("ENDE createArtikelCreated");
    }
}
