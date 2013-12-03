package de.shop.bestellverwaltung.web;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.service.BestellungService;
import de.shop.util.interceptor.Log;
/**
 * Dialogsteuerung fuer die Bestellverwaltung
 * @author Michael
 *
 */
@Named
@RequestScoped
public class BestellungModel implements Serializable {
	
	private static final long serialVersionUID = -2178007500385342348L;
	
	private static final String FLASH_BESTELLUNG = "bestellung";
	private static final String JSF_VIEW_BESTELLUNG = "/bestellverwaltung/viewBestellung";
	
	@Inject
	private BestellungService bs;
	
	@Inject
	private Flash flash;
	
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
	
	/**
	 * Action Methode: Bestellung zu gegebener ID suchen
	 */
	@Log
	public String findBestellungById() {
		final Bestellung bestellung = bs.findBestellungById(bestellungId);
		
		if (bestellung == null) {
			flash.remove(FLASH_BESTELLUNG);
			return null;
		}
		
		flash.put(FLASH_BESTELLUNG, bestellung);
		return JSF_VIEW_BESTELLUNG;
	}
}
