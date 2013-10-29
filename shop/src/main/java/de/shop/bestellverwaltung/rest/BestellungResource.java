package de.shop.bestellverwaltung.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static de.shop.util.Constants.SELF_LINK;
import static de.shop.util.Constants.ADD_LINK;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;









import org.jboss.logging.Logger;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.rest.ArtikelResource;
import de.shop.artikelverwaltung.service.ArtikelService;
import de.shop.bestellverwaltung.domain.Bestellposten;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.service.BestellungServiceImpl;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.kundenverwaltung.rest.KundeResource;
import de.shop.kundenverwaltung.rest.UriHelperKunde;
import de.shop.lieferverwaltung.domain.Lieferung;
import de.shop.lieferverwaltung.rest.UriHelperLieferung;
import de.shop.lieferverwaltung.service.LieferService;
import de.shop.util.interceptor.Log;
import de.shop.util.rest.NotFoundException;
import de.shop.util.rest.UriHelper;



@Path("/bestellung")
@Produces({ APPLICATION_JSON, APPLICATION_XML + ";qs=0.75", TEXT_XML + ";qs=0.5"})
@RequestScoped
@Consumes
@Log
public class BestellungResource {
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Context
	private UriInfo uriInfo;
	
	@Inject
	private ArtikelService as;
	
	@Inject
	private LieferService ls;
	
	@Inject
	private UriHelperBestellung uriHelperBestellung;
	
	@Inject
	private UriHelperLieferung uriHelperLieferung;
	
	@Inject
	private UriHelper uriHelper;
	
	@Inject
	private KundeResource kundeResource;
	
	@Inject 
	private ArtikelResource artikelResource;
	
	@Inject
	private UriHelperKunde uriHelperKunde;
	
		
	@Inject
	private BestellungServiceImpl bs;

	
	@GET
	@Produces(TEXT_PLAIN)
	@Path("version")
	public String getVersion() {
		return "1.0";
	}
	
	@GET
	@Path("{id:[1-9][0-9]*}")
	public Response findBestellungById(@PathParam("id") Long id) {
		final Bestellung bestellung = bs.findBestellungById(id);
		
		if (bestellung == null) {
			final String msg = "Keine Bestellung gefunden mit der ID " + id;
			throw new NotFoundException(msg);
		}
		
		// URLs innerhalb der gefundenen Bestellung anpassen
		setStructuralLinks(bestellung, uriInfo);
		
		// Link-Header setzen
		return Response.ok(bestellung)
					   .links(getTransitionalLinks(bestellung, uriInfo))
					   .build();
			
	}
		
	private Link[] getTransitionalLinks(Bestellung bestellung, UriInfo uriInfo2) {
		final Link self = Link.fromUri(getUriBestellung(bestellung, uriInfo))
							  .rel(SELF_LINK)
							  .build();
		final Link add = Link.fromUri(uriHelper.getUri(BestellungResource.class, uriInfo))
				             .rel(ADD_LINK)
				             .build();
				
		return new Link[] { self, add };
	}

	private void setStructuralLinks(Bestellung bestellung, UriInfo uriInfo) {
		// URI fuer den Kunden setzen
		final AbstractKunde kunde = bestellung.getKunde();
		if (kunde != null) {
			final URI kundeUri = kundeResource.getUriKunde(bestellung.getKunde(), uriInfo);
			bestellung.setKundeUri(kundeUri);
		}
		
		// URI für Artikel in den Bestellpositionen setzen
		final List<Bestellposten> bestellposten = bestellung.getBestellposten();
		if (bestellposten != null && !bestellposten.isEmpty()) {
			for (Bestellposten bp : bestellposten) {
				final URI artikelUri = artikelResource.getArtikelUri(bp.getArtikel(), uriInfo);
				bp.setArtikelUri(artikelUri);
			}
			
		}
		LOGGER.trace(bestellung);
		
	}

	private URI getUriBestellung(Bestellung bestellung, UriInfo uriInfo) {
		return uriHelper.getUri(BestellungResource.class, "findBestellungById", bestellung.getId(), uriInfo);
	}

	//FIXME Die Methode macht überhaupt keinen Sinn?! findLieferungenByBestellungId aber mit Pfad kunde
	// im uriUpdate wird an die Lieferung der Kunde übergeben?!
	@GET
	@Path("{id:[1-9][0-9]*}/kunde")
	public Lieferung findLieferungenByBestellungId(@PathParam("id") Long id) {
		final Lieferung kunde = ls.findLieferungById(id);
		if (kunde == null) {
			final String msg = "Keine Bestellung gefunden mit der ID " + id;
			throw new NotFoundException(msg);
		}

		
		// URLs innerhalb der gefundenen Bestellung anpassen
		uriHelperLieferung.updateUriLieferung(kunde, uriInfo);
		return kunde;
	}
	
	
	@GET
	@Path("{id:[1-9][0-9]*}/kunde")
	public AbstractKunde findKundeByBestellungId(@PathParam("id") Long id) {
		final AbstractKunde kunde = bs.findKundeById(id);
		if (kunde == null) {
			final String msg = "Keine Bestellung gefunden mit der ID " + id;
			throw new NotFoundException(msg);
		}

		// URLs innerhalb der gefundenen Bestellung anpassen
		uriHelperKunde.updateUriKunde(kunde, uriInfo);
		return kunde;
	}

	
	
	
	@GET
	@Path("{posid:[1-9][0-9]*}/bestellung")
	public Bestellung findBestellungByPostenId(@PathParam("id") Long id) {

		final Bestellung posten = bs.findBestellungByPostenId(id);


		// URLs innerhalb der gefundenen Bestellung anpassen
		uriHelperBestellung.updateUriBestellung(posten, uriInfo);

		return posten;
	}
	

	//TODO Methode anpassen, Username anhand Principal ermitteln
	@POST
	@Consumes({ APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
	@Transactional
	public Response createBestellung(@Valid Bestellung bestellung) {
		// Schluessel des Kunden extrahieren
		final String kundeUriStr = bestellung.getKundeUri().toString();
		int startPos = kundeUriStr.lastIndexOf('/') + 1;
		final String kundeIdStr = kundeUriStr.substring(startPos);
		Long kundeId = null;
		try {
			kundeId = Long.valueOf(kundeIdStr);
		}
		catch (NumberFormatException e) {
			throw new NotFoundException("Kein Kunde vorhanden mit der ID " + kundeIdStr, e);
		}
		
		// persistente Artikel ermitteln
		final Collection<Bestellposten> bestellposten = bestellung.getBestellposten();
		final List<Long> artikelIds = new ArrayList<>(bestellposten.size());
		for (Bestellposten bp : bestellposten) {
			final String artikelUriStr = bp.getArtikelUri().toString();
			startPos = artikelUriStr.lastIndexOf('/') + 1;
			final String artikelIdStr = artikelUriStr.substring(startPos);
			Long artikelId = null;
			try {
				artikelId = Long.valueOf(artikelIdStr);
			}
			catch (NumberFormatException e) {
				// Ungueltige Artikel-ID: wird nicht beruecksichtigt
				continue;
			}
			artikelIds.add(artikelId);
			bp.setBestellung(bestellung);
		}
		
		if (artikelIds.isEmpty()) {
			// keine einzige gueltige Artikel-ID
			final StringBuilder sb = new StringBuilder("Keine Artikel vorhanden mit den IDs: ");
			for (Bestellposten bp : bestellposten) {
				final String artikelUriStr = bp.getArtikelUri().toString();
				startPos = artikelUriStr.lastIndexOf('/') + 1;
				sb.append(artikelUriStr.substring(startPos));
				sb.append(' ');
			}
			throw new NotFoundException(sb.toString());
		}

		final Collection<Artikel> gefundeneArtikel = as.findArtikelByIds(artikelIds);
		if (gefundeneArtikel.isEmpty()) {
			throw new NotFoundException("Keine Artikel vorhanden mit den IDs: " + artikelIds);
		}
		
		// Bestellpositionen haben URLs fuer persistente Artikel.
		// Diese persistenten Artikel wurden in einem DB-Zugriff ermittelt (s.o.)
		// Fuer jede Bestellposition wird der Artikel passend zur Artikel-URL bzw. Artikel-ID gesetzt.
		// Bestellpositionen mit nicht-gefundene Artikel werden eliminiert.
		int i = 0;
		final List<Bestellposten> neueBestellpositionen = new ArrayList<>(bestellposten.size());
		for (Bestellposten bp : bestellposten) {
			// Artikel-ID der aktuellen Bestellposition (s.o.):
			// artikelIds haben gleiche Reihenfolge wie bestellpositionen
			final long artikelId = artikelIds.get(i++);
			
			// Wurde der Artikel beim DB-Zugriff gefunden?
			for (Artikel artikel : gefundeneArtikel) {
				if (artikel.getId().longValue() == artikelId) {
					// Der Artikel wurde gefunden
					bp.setArtikel(artikel);
					neueBestellpositionen.add(bp);
					break;					
				}
			}
		}
		bestellung.setBestellpositionen(neueBestellpositionen);
		
		bestellung = bs.createBestellung(bestellung, kundeId);

		final URI bestellungUri = uriHelperBestellung.getUriBestellung(bestellung, uriInfo);
		final Response response = Response.created(bestellungUri).build();
		LOGGER.fatal(bestellungUri.toString());
		
		return response;
	}
	
	
	@PUT
	@Consumes({ APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
	@Transactional
	public void updateBestellung(@Valid Bestellung bestellung) {
		// Vorhandenen Kunden ermitteln
		final Bestellung origBestellung = bs.findBestellungById(bestellung.getId());
		if (origBestellung == null) {
			// TODO msg passend zu locale
			final String msg = "Kein Bestellung gefunden mit der ID " + bestellung.getId();
			throw new NotFoundException(msg);
		}
		LOGGER.tracef("Bestellung vorher: %s", origBestellung);
	
		// Daten des vorhandenen Kunden ueberschreiben
		origBestellung.setValues(bestellung);
		LOGGER.tracef("Bestellung nachher: %s", origBestellung);
		
		// Update durchfuehren
		bestellung = bs.updateBestellung(origBestellung);
		if (bestellung == null) {
			// TODO msg passend zu locale
			final String msg = "Kein Bestellung gefunden mit der ID " + origBestellung.getId();
			throw new NotFoundException(msg);
		}
	
	}
}
