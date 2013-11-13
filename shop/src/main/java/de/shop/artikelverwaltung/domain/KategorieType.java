package de.shop.artikelverwaltung.domain;

import de.shop.artikelverwaltung.domain.KategorieType;

//TODO TypeConverter für Kategorie implementieren und load.sql anpassen!
public enum KategorieType {
	SCHLAFZIMMER("schlafzimmer"),
	KUECHE("kueche"),
	WOHNZIMMER("wohnzimmer"),
	BADEZIMMER("badezimmer"),
	GARTEN("garten"),
	KINDERZIMMER("kinderzimmer"),
	GARDEROBE("garderobe"),
	WERKSTATT("werkstatt"),
	BUERO("buero");
	
private String internal;
	
	private KategorieType(String internal) {
		this.internal = internal;
	}
	
	public String getInternal() {
		return internal;
	}
	
	public static KategorieType build(String internal) {
		if (internal == null) {
			return null;
		}
		
		return KategorieType.valueOf(internal.toUpperCase());
	}
}
