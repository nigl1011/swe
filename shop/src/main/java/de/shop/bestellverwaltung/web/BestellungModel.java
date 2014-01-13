package de.shop.bestellverwaltung.web;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static de.shop.util.Constants.JSF_DEFAULT_ERROR;
import static de.shop.util.Constants.JSF_REDIRECT_SUFFIX;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import de.shop.auth.web.AuthModel;
import de.shop.auth.web.KundeLoggedIn;
import de.shop.bestellverwaltung.domain.Bestellposten;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.service.BestellungService;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.kundenverwaltung.service.KundeService;
import de.shop.kundenverwaltung.service.KundeService.FetchType;
import de.shop.lieferverwaltung.domain.Lieferung;
import de.shop.lieferverwaltung.service.LieferService;
import de.shop.util.interceptor.Log;
import de.shop.util.web.Client;
import de.shop.util.web.Messages;
/**
 * Dialogsteuerung fuer die Bestellverwaltung
 * @author Michael
 *
 */
@Named
@SessionScoped
public class BestellungModel implements Serializable {
	
	private static final long serialVersionUID = -2178007500385342348L;
	
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
	private static final String FLASH_BESTELLUNG = "bestellung";
	private static final String JSF_BESTELLVERWALTUNG = "/bestellverwaltung/";
	private static final String JSF_VIEW_BESTELLUNG = JSF_BESTELLVERWALTUNG + "viewBestellung";
	private static final String JSF_FIND_BESTELLUNG = JSF_BESTELLVERWALTUNG + "findBestellung";
	private static final String JSF_FIND_LIEFERUNG = "/lieferverwaltung/viewLieferung";

	private static final String MSG_KEY_BESTELLUNG_NOT_FOUND_BY_ID = "bestellung.notFound.id";

	private static final String BESTELLUNG_ID_FORM = "form:idInput";
	
	private Bestellung bestellung;
	
	@Inject
	@Client
	private Locale locale;
	
	@Inject
	private Messages messages;
	
	@Inject
	private Warenkorb warenkorb;
		
	@Inject
	private BestellungService bs;
	
	@Inject
	private Lieferung lieferung;
	
	@Inject
	private LieferService ls;
	
	@Inject
	private KundeService ks;
	
	@Inject
	@KundeLoggedIn
	private AbstractKunde kunde;
	
	@Inject
	private AuthModel auth;
	
	@Inject
	private Flash flash;
	
	private Long lieferungId;
	
	private Long bestellungId;
	
	@Override
	public String toString() {
		return "BestellungModell [bestellungId" + bestellungId + "]";
	}
	
	public void setBestellungId(Long bestellungId) {
		this.bestellungId = bestellungId;
	}
	
	public Long getBestellungId() {
		return bestellungId;
	}
	
	public Bestellung getBestellung() {
		return bestellung;
	}
	public Lieferung getLieferung() {
		return lieferung;
	}
	
	/**
	 * Action Methode: Bestellung zu gegebener ID suchen
	 * Rollenbasiert nur als Admin, Mitarbeiter möglich
	 * 
	 */
	@Transactional
	@Log
	public String findBestellungById() {
		auth.preserveLogin();
		if (bestellungId == null) {
			return null;
		}
		bestellung = bs.findBestellungById(bestellungId);
		if (bestellung == null) {
			return findBestellungByIdErrorMsg(bestellungId.toString());
		}
		flash.put(FLASH_BESTELLUNG, bestellung);
		return JSF_FIND_BESTELLUNG + JSF_REDIRECT_SUFFIX;
	}
	private String findBestellungByIdErrorMsg(String bestellungId) {
		messages.error(MSG_KEY_BESTELLUNG_NOT_FOUND_BY_ID, locale, BESTELLUNG_ID_FORM, bestellungId);
		return null;
	}

	/**
	 * Action Methode: Bestellung absenden
	 */
	@Transactional
	@Log
	public String bestellen() {
		
		auth.preserveLogin();		
		if (warenkorb == null || warenkorb.getPositionen() == null || warenkorb.getPositionen().isEmpty()) {
			return JSF_DEFAULT_ERROR;
		}
		// Logged Kunde + Bestellungen ermitteln und neue ergänzen
		kunde = ks.findKundeById(kunde.getId(), FetchType.MIT_BESTELLUNGEN);
		
		// Warenkorb nur Posten mit Anzahl > 0 übernehmen
		final List<Bestellposten> positionen = warenkorb.getPositionen();
		final List<Bestellposten> neuePositionen = new ArrayList<>(positionen.size());
		for (Bestellposten bp : positionen) {
			if (bp.getAnzahl() > 0) {
				neuePositionen.add(bp);
			}
		}
		
		// Reset Warenkorb
		warenkorb.endConversation();
		
		// Neue Bestellung erzeugen
		Bestellung bestellung = new Bestellung();
		bestellung.setBestellpositionen(neuePositionen);
		LOGGER.tracef("Neue Bestellung: %s\nBestellpositionen: %s", bestellung, bestellung.getBestellposten());
		
		// Bestellung mit Kunden verknüpfen
		bestellung = bs.createBestellung(bestellung, kunde);
		
		// Bestellung im Flash speichern wegen Redirect
		flash.put(FLASH_BESTELLUNG, bestellung);
		
		return JSF_VIEW_BESTELLUNG;
	}
	/**
	 * Lieferung zu einer Bestellung auslösen
	 * 
	 */
	@Transactional
	@Log
	public String createLieferung() {
		
		lieferung = ls.createLieferung(lieferung, bestellung.getId());
		flash.put("lieferung", lieferung);
		return JSF_FIND_LIEFERUNG + JSF_REDIRECT_SUFFIX;
	}
}
