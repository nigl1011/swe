package de.shop.bestellverwaltung.service;

import static de.shop.util.Constants.KEINE_ID;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;







import org.jboss.logging.Logger;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.bestellverwaltung.domain.Bestellposten;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.domain.StatusType;
import de.shop.lieferverwaltung.domain.Lieferung;
import de.shop.lieferverwaltung.service.LieferService;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.kundenverwaltung.service.KundeService;
import de.shop.util.interceptor.Log;

@Dependent
@Log
public class BestellungServiceImpl implements Serializable, BestellungService {
	private static final long serialVersionUID = -9145947650157430928L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Inject
	private transient EntityManager em;
	
	@Inject
	private KundeService ks;
	
	@Inject
	private LieferService ls;
	
	@Inject
	@NeueBestellung
	private transient Event<Bestellung> event;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	/**
	 */
	@Override
	public Bestellung findBestellungById(Long id) {
		final Bestellung bestellung = em.find(Bestellung.class, id);
		return bestellung;
	}
	
	@Override
	public Bestellung findBestellungByPostenId(Long id) {
		final Bestellung bestellung = em.find(Bestellung.class, id);
		return bestellung;
	}


	@Override
	public Bestellung findBestellungByIdMitLieferungen(Long id) {
		try {
			final Bestellung bestellung = em.createNamedQuery(Bestellung.FIND_BESTELLUNG_BY_ID_FETCH_LIEFERUNGEN,
                                                              Bestellung.class)
                                            .setParameter(Bestellung.PARAM_ID, id)
					                        .getSingleResult();
			return bestellung;
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public AbstractKunde findKundeById(Long id) {
		try {
			final AbstractKunde kunde = em.createNamedQuery(Bestellung.FIND_KUNDE_BY_ID, AbstractKunde.class)
                                          .setParameter(Bestellung.PARAM_ID, id)
					                      .getSingleResult();
			return kunde;
		}
		catch (NoResultException e) {
			return null;
		}
	}

	/**
	 */
	@Override
	public List<Bestellung> findBestellungenByKunde(AbstractKunde kunde) {
		if (kunde == null) {
			return Collections.emptyList();
		}
		final List<Bestellung> bestellungen = em.createNamedQuery(Bestellung.FIND_BESTELLUNGEN_BY_KUNDE,
                                                                  Bestellung.class)
                                                .setParameter(Bestellung.PARAM_KUNDE, kunde)
				                                .getResultList();
		return bestellungen;
	}

	@Override
	public Bestellung createBestellung(Bestellung bestellung, String username) {
		if (bestellung == null) {
			return null;
		}
		
		// Den persistenten Kunden mit der transienten Bestellung verknuepfen
		final AbstractKunde kunde = ks.findKundeByUserName(username);
		return createBestellung(bestellung, kunde);
	}
	

	@Override
	public Bestellung createBestellung(Bestellung bestellung,
			                           AbstractKunde kunde) {
		if (bestellung == null) {
			return null;
		}
		
		// Den persistenten Kunden mit der transienten Bestellung verknuepfen
		if (!em.contains(kunde)) {
			kunde = ks.findKundeById(kunde.getId(), KundeService.FetchType.MIT_BESTELLUNGEN);
		}
		kunde.addBestellung(bestellung);
		bestellung.setKunde(kunde);
		
		// Vor dem Abspeichern IDs zuruecksetzen:
		// IDs koennten einen Wert != null haben, wenn sie durch einen Web Service uebertragen wurden
		bestellung.setId(KEINE_ID);
		for (Bestellposten bp : bestellung.getBestellposten()) {
			bp.setId(KEINE_ID);
			LOGGER.tracef("Bestellposition: %s", bp);				
		}
		
		em.persist(bestellung);
		event.fire(bestellung);

		return bestellung;
	}
	
	@Override
	public List<Artikel> ladenhueter(int anzahl) {
		final List<Artikel> artikel = em.createNamedQuery(Bestellposten.FIND_LADENHUETER, Artikel.class)
				                        .setMaxResults(anzahl)
				                        .getResultList();
		return artikel;
	}
	
	@Override
	public List<Lieferung> findLieferungen(String nr) {
		final List<Lieferung> lieferungen =
				              em.createNamedQuery(Lieferung.FIND_LIEFERUNGEN_BY_LIEFERNR_FETCH_BESTELLUNGEN,
                                                  Lieferung.class)
                                .setParameter(Lieferung.PARAM_LIEFERNR, nr)
				                .getResultList();
		return lieferungen;
	}


	@Override
	public List<Bestellung> findBestellungenByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		
		// SELECT b
		// FROM   Bestellung b LEFT JOIN FETCH b.lieferungen
		// WHERE  b.id = <id> OR ...

		final CriteriaBuilder builder = em.getCriteriaBuilder();
		final CriteriaQuery<Bestellung> criteriaQuery  = builder.createQuery(Bestellung.class);
		final Root<Bestellung> b = criteriaQuery.from(Bestellung.class);
		b.fetch("lieferungen", JoinType.LEFT);
		
		// Die Vergleichen mit "=" als Liste aufbauen
		final Path<Long> idPath = b.get("id");
		final List<Predicate> predList = new ArrayList<>();
		for (Long id : ids) {
			final Predicate equal = builder.equal(idPath, id);
			predList.add(equal);
		}
		// Die Vergleiche mit "=" durch "or" verknuepfen
		final Predicate[] predArray = new Predicate[predList.size()];
		final Predicate pred = builder.or(predList.toArray(predArray));
		criteriaQuery.where(pred).distinct(true);

		final TypedQuery<Bestellung> query = em.createQuery(criteriaQuery);
		final List<Bestellung> bestellungen = query.getResultList();
		return bestellungen;
	}
	
	
	@Override
	public Bestellung updateBestellung(Bestellung bestellung) {
		if (bestellung == null) {
			return null;
		}
		//Bestellung vom EntityManager trennen
		
		em.detach(bestellung);
		
		
		/*	Pr�fen ob Lieferung schon existiert 
		 * Wenn das der Fall ist pr�fen ob StatusFlag noch InBearbeitung, dann auf Verschickt setzen
		 * auf jeden Fall eine Exception werfen, da nach Versenden
		 * keine Bearbeitung an der Bestellung mehr m�glich !
		 *  
		 */
		if (bestellung.getLieferung() != null && bestellung.getStatus().equals(StatusType.VERSCHICKT)) 	{
					
			throw new BestellungVerschicktException(bestellung.getId());
		}
			
		/*
		 * Nochmal pr�fen ob das Flag auf verschickt gesetzt wurde
		 * --> Lieferung absetzen
		 * 
		 */
		else {
			if (bestellung.getStatus().equals(StatusType.VERSCHICKT)) {
				ls.createLieferung(bestellung);
			}
						
		}
		
		
		bestellung = em.merge(bestellung);
		
		return bestellung;
		
		
		
		
		
	}


}
	
