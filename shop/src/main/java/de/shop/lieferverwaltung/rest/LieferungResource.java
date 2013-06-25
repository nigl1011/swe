package de.shop.lieferverwaltung.rest;


import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.service.BestellungService;
import de.shop.lieferverwaltung.domain.Lieferung;
import de.shop.lieferverwaltung.service.LieferService;
import de.shop.util.LocaleHelper;
import de.shop.util.Log;
//import de.shop.util.Mock;
import de.shop.util.NotFoundException;
import de.shop.util.Transactional;

@Path("/lieferungen")
@Produces(APPLICATION_JSON)
@Consumes
@RequestScoped
@Transactional
@Log
public class LieferungResource {
	
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
		
	@Context
	private UriInfo uriInfo;
	
	@Context
	private HttpHeaders headers;

	@Inject
	private UriHelperLieferung uriHelperLieferung;
	
	@Inject
	private LieferService ls;
	
	@Inject
	private LocaleHelper localeHelper;
	
	@Inject
	private BestellungService bs;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	@GET
	@Produces(TEXT_PLAIN)
	@Path("version")
	public String getVersion() {
		return "1.0";
	}
	
	@GET
	@Path("{id:[1-9][0-9]*}")
	public Lieferung findLieferungById(@PathParam("id") Long id) {
		final Locale locale = localeHelper.getLocale(headers);
		final Lieferung lieferung = ls.findLieferungById(id, locale);
		if (lieferung == null) {
			throw new NotFoundException("Keine Lieferung mit der ID " + id + " gefunden.");
		}
		
		// TODO URLs innerhalb der gefundenen Lieferung anpassen
		uriHelperLieferung.updateUriLieferung(lieferung, uriInfo);
		return lieferung;
	}
	
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces
	public Response createLieferung(Lieferung lieferung) {
		final Locale locale = localeHelper.getLocale(headers);
		for(URI bestellUri : lieferung.getBestellungUri()){
			String bestellungString=bestellUri.toString();
			int startPos = bestellungString.lastIndexOf('/') + 1;
			final String bestellungId = bestellungString.substring(startPos);
			Bestellung bestellung = bs.findBestellungById(Long.valueOf(bestellungId));
			lieferung.addBestellung(bestellung);
			bestellung.addLieferung(lieferung);
		}
		lieferung = ls.createLieferung(lieferung, locale);
		final URI lieferungUri = uriHelperLieferung.getUriLieferung(lieferung, uriInfo);
		return Response.created(lieferungUri).build();
	}
	
	@PUT
	@Consumes(APPLICATION_JSON)
	@Produces
	public void updateLieferung(Lieferung lieferung) {
		// Vorhandenen Kunden ermitteln
		final Locale locale = localeHelper.getLocale(headers);
		final Lieferung origLieferung = ls.findLieferungById(lieferung.getId(), locale);
		if (origLieferung == null) {
			// TODO msg passend zu locale
			final String msg = "Kein Lieferng gefunden mit der ID " + lieferung.getId();
			throw new NotFoundException(msg);
		}
		LOGGER.tracef("Lieferung vorher: %s", origLieferung);
	
		// Daten des vorhandenen Kunden ueberschreiben
		origLieferung.setValues(lieferung);
		LOGGER.tracef("Lieferung nachher: %s", origLieferung);
		
		// Update durchfuehren
		lieferung = ls.updateLieferung(origLieferung, locale);
		if (lieferung == null) {
			// TODO msg passend zu locale
			final String msg = "Kein Leiferung gefunden mit der ID " + origLieferung.getId();
			throw new NotFoundException(msg);
		}
	
	}

}
