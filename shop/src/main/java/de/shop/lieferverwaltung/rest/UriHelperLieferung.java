package de.shop.lieferverwaltung.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;


import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.rest.UriHelperBestellung;
import de.shop.lieferverwaltung.domain.Lieferung;


@ApplicationScoped
public class UriHelperLieferung {

	public URI getUriLieferung(Lieferung lieferung, UriInfo uriInfo) {
		final UriBuilder ub = uriInfo.getBaseUriBuilder()
		                             .path(LieferungResource.class)
		                             .path(LieferungResource.class, "findLieferungById");
		final URI uri = ub.build(lieferung.getId());
		return uri;
	}
	
	
	@Inject
	private UriHelperBestellung uriHelperBestellung;

	
	public void updateUriLieferung(Lieferung lieferung, UriInfo uriInfo) {
		final Set<Bestellung> bestellungen = lieferung.getBestellungen();
		final List<URI> uris = new ArrayList<URI>();
		if (bestellungen != null && !bestellungen.isEmpty()) {
			for (Bestellung bestellung : bestellungen) {
				uris.add(uriHelperBestellung.getUriBestellung(bestellung, uriInfo));
			}
			
			lieferung.setBestellungUri(uris);
			
			
		}	
		
		
	}

}
