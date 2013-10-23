package de.shop.artikelverwaltung.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Collection;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.domain.KategorieType;
import de.shop.artikelverwaltung.service.ArtikelService;
import de.shop.util.LocaleHelper;
import de.shop.util.Log;
import de.shop.util.NotFoundException;


@Path("/artikel")
@Produces(APPLICATION_JSON)
@Consumes
@RequestScoped
@Transactional
@Log
public class ArtikelResource {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Context
	private UriInfo uriInfo;
	
	@Context
	private HttpHeaders headers;
	
	@Inject
	private UriHelperArtikel uriHelperArtikel;
	
	@Inject
	private ArtikelService as;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	@Inject
	private LocaleHelper localeHelper;
	
	@GET
	@Produces(TEXT_PLAIN)
	@Path("version")
	public String getVersion() {
		return "1.0";
	}
	
	@GET
	@Path("{id:[1-9][0-9]*}")
	public Artikel findArtikelById(@PathParam("id") Long id, @Context UriInfo uirInfo) {
		final Locale locale = localeHelper.getLocale(headers);
		final Artikel artikel = as.findArtikelById(id, locale);
		if (artikel == null) {
			throw new NotFoundException("Kein Artikel mit der ID " + id + " gefunden.");
		}
		
		return artikel;
	}
	
	//TODO @DefaultValue("") kann bei Enums nicht klappen und hier krachts auch. Loesung: Wrapper
	@GET
	public Collection<Artikel> findArtikelByKategorie(@QueryParam("kategorie") 
	@DefaultValue("") KategorieType kategorie) {
		
		Collection<Artikel> gesuchteArtikel = null;
			if ("".equals(kategorie)) {
				gesuchteArtikel = as.findVerfuegbareArtikel();
				if (gesuchteArtikel.isEmpty()) {
					throw new NotFoundException("Kein Artikel vorhanden");
					}
				}
			else {
				final Locale locale = localeHelper.getLocale(headers);
				gesuchteArtikel = as.findArtikelByKategorie(kategorie, locale);
				if (gesuchteArtikel.isEmpty()) {
					throw new NotFoundException("Keine Artikel aus der Kategorie " + kategorie + " gefunden");
				}
				
			}
	
	return gesuchteArtikel;
	}
	

	
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces
	public Response createArtikel(Artikel artikel) {
		//@SuppressWarnings("unused")

		final Locale locale = localeHelper.getLocale(headers);
		artikel = as.createArtikel(artikel, locale);
		final URI artikelUri = uriHelperArtikel.getUriArtikel(artikel, uriInfo);
		return Response.created(artikelUri).build();

	}
	
	@PUT
	@Consumes(APPLICATION_JSON)
	@Produces
	public void updateArtikel(Artikel artikel) {
		// Vorhandenen Kunden ermitteln
		final Locale locale = localeHelper.getLocale(headers);
		final Artikel origArtikel = as.findArtikelById(artikel.getId(), locale);
		if (origArtikel == null) {
			// TODO msg passend zu locale
			final String msg = "Kein Artikel gefunden mit der ID " + artikel.getId();
			throw new NotFoundException(msg);
		}
		LOGGER.tracef("Artikel vorher: %s", origArtikel);
	
		// Daten des vorhandenen Kunden ueberschreiben
		origArtikel.setValues(artikel);
		LOGGER.tracef("Kunde nachher: %s", origArtikel);
		
		// Update durchfuehren
		artikel = as.updateArtikel(origArtikel, locale);
		if (artikel == null) {
			// TODO msg passend zu locale
			final String msg = "Kein Artikel gefunden mit der ID " + origArtikel.getId();
			throw new NotFoundException(msg);
		}
	
	}
}
