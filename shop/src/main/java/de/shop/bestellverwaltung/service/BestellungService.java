package de.shop.bestellverwaltung.service;

import java.util.List;
import java.util.Locale;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.lieferverwaltung.domain.Lieferung;

public interface BestellungService {

	Bestellung findBestellungById(Long id);
	List<Bestellung> findBestellungenByIds(List<Long> ids);
	Bestellung findBestellungByIdMitLieferungen(Long id);
	AbstractKunde findKundeById(Long id, Locale locale);
	List<Bestellung> findBestellungenByKunde(AbstractKunde kunde);
	Bestellung createBestellung(Bestellung bestellung, Long kundeId, Locale locale);
	Bestellung createBestellung(Bestellung bestellung, AbstractKunde kunde, Locale locale);
	List<Artikel> ladenhueter(int anzahl);
	//List<Bestellung> findBestellungenByKundeId(Long kundeId, Locale locale);
	List<Lieferung> findLieferungen(String nr);
	Lieferung createLieferung(Lieferung lieferung, List<Bestellung> bestellungen);
	Bestellung findBestellungByPostenId(Long id, Locale locale);
	
}
