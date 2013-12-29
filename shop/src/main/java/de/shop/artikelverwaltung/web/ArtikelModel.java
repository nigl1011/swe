package de.shop.artikelverwaltung.web;

import static de.shop.util.Constants.JSF_REDIRECT_SUFFIX;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.List;






import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.service.ArtikelService;
import de.shop.auth.web.AuthModel;
import de.shop.util.interceptor.Log;
import de.shop.util.web.Client;
import de.shop.util.web.Messages;
/*
 * Dialogsteuerung für ArtikelService
 */
@Named
@SessionScoped
public class ArtikelModel implements Serializable {
	private static final long serialVersionUID = 58038678073649354L;

	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	private static final String JSF_VIEW_ARTIKEL = "/artikelverwaltung/viewArtikel";
	private static final String JSF_LIST_ARTIKEL = "/artikelverwaltung/listArtikel";
	private static final String FLASH_ARTIKEL = "artikel";
	
	private static final String JSF_SELECT_ARTIKEL = "/artikelverwaltung/selectArtikel";
	private static final String SESSION_VERFUEGBARE_ARTIKEL = "verfuegbareArtikel";
	
	private static final String MSG_KEY_ARTIKEL_NOT_FOUND_BY_ID = "artikel.notFound.id";
	private static final String CLIENT_ID_ARTIKELID = "form:artikelIdInput";
	
	private static final String MSG_KEY_ARTIKEL_NOT_FOUND_BY_BEZEICHNUNG = "artikel.notFound.bezeichnung";
	private static final String CLIENT_ID_ARTIKEL_BEZEICHNUNG = "form:bezeichnung";
	
	private Long artikelId;
	private String bezeichnung;
	private Artikel artikel;
	
	private List<String> kategorie;
	
	@Inject
	private ArtikelService as;
	
	@Inject
	private AuthModel auth;
	
	@Inject
	private Messages messages;
	
	@Inject
	@Client
	private Locale locale;
	
	@Inject
	private Flash flash;
	
	@Inject 
	private transient HttpSession session;
	
	private Artikel neuerArtikel;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}

	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	@Override
	public String toString() {
		return "ArtikelModel [bezeichnung=" + bezeichnung + "]";
	}
	public Long getArtikelId() {
		return artikelId;
	}
	public void setArtikelId(Long artikelId) {
		this.artikelId = artikelId;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public Artikel getArtikel() {
		return artikel;
	}
	
	public List<String> getKategorie() {
		return kategorie;
	}
	
	public void setKategorie(List<String> kategorie) {
		this.kategorie = kategorie;
	}
	
	public Artikel getNeuerArtikel() {
		return neuerArtikel;
	}
	
	@Log
	public String findArtikelByBezeichnung() {
		final List<Artikel> artikel = as.findArtikelByBezeichnung(bezeichnung);
		
		if (artikel == null || artikel.isEmpty()) {
			// Keine Artikel mit der gesuchten Bezeichnung gefunden
			return findArtikelByBezeichnungErrorMsg(bezeichnung.toString());
		}
		
		flash.put(FLASH_ARTIKEL, artikel);
		
		return JSF_LIST_ARTIKEL;
	}
	
	private String findArtikelByBezeichnungErrorMsg(String bezeichnung) {
		messages.error(MSG_KEY_ARTIKEL_NOT_FOUND_BY_BEZEICHNUNG, locale, CLIENT_ID_ARTIKEL_BEZEICHNUNG, bezeichnung);
		return null;
	}

	@Log
	public String selectArtikel() {
		if (session.getAttribute(SESSION_VERFUEGBARE_ARTIKEL) == null) {
			final List<Artikel> alleArtikel = as.findVerfuegbareArtikel();
			session.setAttribute(SESSION_VERFUEGBARE_ARTIKEL, alleArtikel);
		}
		
		return JSF_SELECT_ARTIKEL;
	}
	public void createEmptyArtikel() {
		if (neuerArtikel != null) {
			return;
		}
		
		// neues Artikel Objekt anlegen
		neuerArtikel = new Artikel();
		
	}
	@Transactional
	@Log
	public String createArtikel() {
		
		neuerArtikel = as.createArtikel(neuerArtikel);
		
		
		// Aufbereitung für viewArtikel.xhtml
		artikelId = neuerArtikel.getId();
		artikel = neuerArtikel;
		neuerArtikel = null;
		
		// Artikel im Flash speichern wegen Redirect zum neuen Artikel
		flash.put(FLASH_ARTIKEL, artikel);
		
		return JSF_VIEW_ARTIKEL;
		
		
		
	}
	@Log
	public String findArtikelById() {
		if (artikelId == null) {
			return null;
		}
		artikel = as.findArtikelById(artikelId);
		if (artikel == null) {
			// Kein Artikel zur ID gefunden
			return findArtikelByIdErrorMsg(artikelId.toString());
		}
		flash.put(FLASH_ARTIKEL, artikel);
		
		return JSF_VIEW_ARTIKEL;
	}
	private String findArtikelByIdErrorMsg(String id) {
		messages.error(MSG_KEY_ARTIKEL_NOT_FOUND_BY_ID, locale, CLIENT_ID_ARTIKELID, id);
		return null;
	}

	@TransactionAttribute
	@Log
	public String update() {
		auth.preserveLogin();
	
	//TODO: update implementieren
	return null;
	}
}
