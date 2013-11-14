package de.shop.lieferverwaltung.rest;


import static de.shop.util.Constants.ADD_LINK;
import static de.shop.util.Constants.LIST_LINK;
import static de.shop.util.Constants.REMOVE_LINK;
import static de.shop.util.Constants.SELF_LINK;
import static de.shop.util.Constants.UPDATE_LINK;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.lang.invoke.MethodHandles;
import java.net.URI;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.rest.BestellungResource;
import de.shop.kundenverwaltung.rest.KundeResource;
import de.shop.lieferverwaltung.domain.Lieferung;
import de.shop.lieferverwaltung.service.LieferService;
import de.shop.util.interceptor.Log;
import de.shop.util.rest.NotFoundException;
import de.shop.util.rest.UriHelper;


@Path("/lieferungen")
@Produces(APPLICATION_JSON)
@Consumes
@RequestScoped
@Transactional
@Log
public class LieferungResource {
	
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private static final String NOT_FOUND_ID = "bestellung.notFound.id";
		
	@Context
	private UriInfo uriInfo;
	
	@Context
	private HttpHeaders headers;

	@Inject
	private UriHelperLieferung uriHelperLieferung;
	
	@Inject
	private BestellungResource bestellungResource;
	
	@Inject
	private LieferService ls;
	
	//@Inject
	//private BestellungService bs;
	
	@Inject
	private UriHelper uriHelper;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	//TODO Bean Validation für Methodenparameter, z.B: @Valid, @Pattern, ...
	@GET
	@Produces(TEXT_PLAIN)
	@Path("version")
	public String getVersion() {
		return "1.0";
	}
	
	@GET
	@Path("{id:[1-9][0-9]*}")
	public Response findLieferungById(@PathParam("id") Long id) {
		final Lieferung lieferung = ls.findLieferungById(id);
		if (lieferung == null) {
			throw new NotFoundException("Keine Lieferung mit der ID " + id + " gefunden.");
		}
		
		setStructuralLinks(lieferung, uriInfo);
		
		return Response.ok(lieferung).links(getTransitionalLinks(lieferung, uriInfo)).build();
	}
	
	public void setStructuralLinks(Lieferung lieferung, UriInfo uriInfo) {
		// URI fuer Bestellungen setzen
		
		
		final Bestellung bestellung = lieferung.getBestellung();
		if (bestellung != null) {
				final URI bestellungUri = bestellungResource.getUriBestellung(bestellung, uriInfo);
				lieferung.setBestellungUri(bestellungUri);
			}
		
		
		LOGGER.trace(lieferung);
	}
	
	//TODO: Beim Erstellen der Lieferung muss Bestellung.Status auf Verschickt gesetzt werden
	/*
	 * Dafür gibt es schon die Methode updateBestellung(Bestellung bestellung)
	 */
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces
	@Transactional
	public Response createLieferung(Lieferung lieferung) {
		lieferung = ls.createLieferung(lieferung);
		
		if(lieferung == null) {
			return null;
		}
		final URI lieferungUri = uriHelperLieferung.getUriLieferung(lieferung, uriInfo);
		return Response.created(lieferungUri).build();
	}
	
	@PUT
	@Consumes(APPLICATION_JSON)
	@Produces
	@Transactional
	public void updateLieferung(Lieferung lieferung) {
		// Vorhandene Lieferung ermitteln
		final Lieferung origLieferung = ls.findLieferungById(lieferung.getId());
		if (origLieferung == null) {
			// msg passend zu locale
			throw new NotFoundException(NOT_FOUND_ID, lieferung.getId());
		}
		LOGGER.tracef("Lieferung vorher: %s", origLieferung);
	
		// Daten des vorhandenen Kunden ueberschreiben
		origLieferung.setValues(lieferung);
		LOGGER.tracef("Lieferung nachher: %s", origLieferung);
		
		// Update durchfuehren
		lieferung = ls.updateLieferung(origLieferung);
		if (lieferung == null) {
			// msg passend zu locale
			throw new NotFoundException(NOT_FOUND_ID, origLieferung.getId());
		}
	
	}

	public Link[] getTransitionalLinks(Lieferung lieferung, UriInfo uriInfo) {
			final Link self = Link.fromUri(getUriLieferung(lieferung, uriInfo)).rel(SELF_LINK).build();
			final Link list = Link.fromUri(uriHelper.getUri(LieferungResource.class, uriInfo)).rel(LIST_LINK).build();
			final Link add = Link.fromUri(uriHelper.getUri(LieferungResource.class, uriInfo)).rel(ADD_LINK).build();
			final Link update = Link.fromUri(uriHelper.getUri(LieferungResource.class, uriInfo)).rel(UPDATE_LINK).build();
			final Link remove = Link.fromUri(uriHelper.getUri(LieferungResource.class, "deleteKunde", lieferung.getId(), uriInfo))
					.rel(REMOVE_LINK)
					.build();
			return new Link[] {self, list, add, update, remove};
	}
	
	public URI getUriLieferung(Lieferung lieferung, UriInfo uriInfo) {
        return uriHelper.getUri(KundeResource.class, "findLieferungById", lieferung.getId(), uriInfo);
    }
}
