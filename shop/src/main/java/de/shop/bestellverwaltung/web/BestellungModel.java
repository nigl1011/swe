package de.shop.bestellverwaltung.web;

import java.io.Serializable;

import javax.ejb.TransactionAttribute;
import javax.enterprise.context.SessionScoped;
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
@SessionScoped
public class BestellungModel implements Serializable {
	
	private static final long serialVersionUID = -2178007500385342348L;
	
	private static final String FLASH_BESTELLUNG = "bestellung";
	private static final String JSF_BESTELLVERWALTUNG = "/bestellverwaltung";
	private static final String JSF_VIEW_BESTELLUNG = JSF_BESTELLVERWALTUNG + "viewBestellung";
	
	private Bestellung bestellung;
	
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
	
	public Bestellung getBestellung() {
		return bestellung;
	}
	
	/**
	 * Action Methode: Bestellung zu gegebener ID suchen
	 */
	@TransactionAttribute
	@Log
	public String findBestellungById() {
		
		
		if (bestellungId == null) {
			return null;
		}
		
		bestellung = bs.findBestellungById(bestellungId);
		flash.put(FLASH_BESTELLUNG, bestellung);
		return JSF_VIEW_BESTELLUNG;
	}
}
