package de.shop.bestellverwaltung.service;

import java.util.List;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.lieferverwaltung.domain.Lieferung;

public interface BestellungService {

	Bestellung findBestellungById(Long id);
	
	List<Bestellung> findBestellungenByIds(List<Long> ids);
	
	Bestellung findBestellungByIdMitLieferungen(Long id);
	
	AbstractKunde findKundeById(Long id);
	
	List<Bestellung> findBestellungenByKunde(AbstractKunde kunde);
	
	Bestellung createBestellung(Bestellung bestellung, String username);
	
	Bestellung createBestellung(Bestellung bestellung, AbstractKunde kunde);
	
	List<Artikel> ladenhueter(int anzahl);
	
	
	List<Lieferung> findLieferungen(String nr);
	
	Bestellung findBestellungByPostenId(Long id);
	
	Bestellung updateBestellung(Bestellung bestellung);
	
}
