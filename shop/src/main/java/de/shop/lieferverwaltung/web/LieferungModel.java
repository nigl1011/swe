package de.shop.lieferverwaltung.web;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import de.shop.auth.web.AuthModel;
import de.shop.lieferverwaltung.domain.Lieferung;
import de.shop.lieferverwaltung.service.LieferService;
import de.shop.util.interceptor.Log;

@Named
@RequestScoped
public class LieferungModel implements Serializable {
	
	private static final long serialVersionUID = 6473526291332635412L;
	
	private static final String FLASH_LIEFERUNG = "lieferung";
	private static final String JSF_LIEFERVERWALTUNG = "/lieferverwaltung/";
	private static final String JSF_VIEW_LIEFERUNG = JSF_LIEFERVERWALTUNG + "/viewLieferung";
	//private static final String JSF_FIND_LIEFERUNG = JSF_LIEFERVERWALTUNG + "findLieferung";
	
	private Lieferung lieferung;
	
	@Inject
	private LieferService ls;
	
	@Inject
	private AuthModel auth;
	
	@Inject
	private Flash flash;
	
	private Long lieferId;

	@Override
	public String toString() {
		return "LieferungModel [lieferId=" + lieferId + "]";
	}

	public Long getLieferId() {
		return lieferId;
	}
	
	public Lieferung getLieferung() {
		return lieferung;
	}

	public void setLieferId(Long lieferId) {
		this.lieferId = lieferId;
	}
	
	/**
	 * Action Methode, um eine Lieferung zu gegebener ID zu suchen
	 * @return URL fuer Anzeige der gefundenen Lieferung; sonst null
	 */
	/**@Log
	public String findLieferungById() {
		final Lieferung lieferung = ls.findLieferungById(lieferId);
		if (lieferung == null) {
			flash.remove(FLASH_LIEFERUNG);
			return null;
		}
		
		flash.put(FLASH_LIEFERUNG, lieferung);
		return JSF_VIEW_LIEFERUNG;
	}
	**/
	
	@Transactional
	@Log
	public String findLieferungById() {
		auth.preserveLogin();
		if (lieferId == null) {
			return null;
		}
		lieferung = ls.findLieferungById(lieferId);
		flash.put(FLASH_LIEFERUNG, lieferung);
		//return JSF_FIND_LIEFERUNG;
		return JSF_VIEW_LIEFERUNG;
	}
}
