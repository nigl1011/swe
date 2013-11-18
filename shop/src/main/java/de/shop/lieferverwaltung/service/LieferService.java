package de.shop.lieferverwaltung.service;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.bestellverwaltung.service.BestellungService;
import de.shop.lieferverwaltung.domain.Lieferung;
import de.shop.util.interceptor.Log;
import static de.shop.util.Constants.KEINE_ID;;

@Dependent
@Log
public class LieferService implements Serializable {
	private static final long serialVersionUID = -3907630579991760308L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Inject
	private transient EntityManager em;
	
	@Inject
	private BestellungService bs;
	

	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	public Lieferung findLieferungById(Long id) {
		final Lieferung lieferung = em.find(Lieferung.class, id);
		return lieferung;
	}
	
	public Lieferung createLieferung(Bestellung bestellung) {
		if (bestellung == null) {
			return null;
		}
		final Lieferung lieferung = new Lieferung();
		lieferung.setBestellung(bestellung);
		
		
		em.persist(lieferung);
		return lieferung;
	}

	
	public List<Lieferung> findLieferungByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}
		

		final CriteriaBuilder builder = em.getCriteriaBuilder();
		final CriteriaQuery<Lieferung> criteriaQuery = builder.createQuery(Lieferung.class);
		final Root<Lieferung> a = criteriaQuery.from(Lieferung.class);

		final Path<Long> idPath = a.get("id");

		
		Predicate pred = null;
		if (ids.size() == 1) {
			// Genau 1 id: kein OR notwendig
			pred = builder.equal(idPath, ids.get(0));
		}
		else {
			// Mind. 2x id, durch OR verknuepft
			final Predicate[] equals = new Predicate[ids.size()];
			int i = 0;
			for (Long id : ids) {
				equals[i++] = builder.equal(idPath, id);
			}
			
			pred = builder.or(equals);
		}
		
		criteriaQuery.where(pred);
		
		final TypedQuery<Lieferung> query = em.createQuery(criteriaQuery);

		final List<Lieferung> lieferung = query.getResultList();
		return lieferung;
	}

	/*
	 * Lieferung für eine Bestellung auslösen
	 */
	
	public Lieferung createLieferung(Lieferung lieferung, Long bestellungId) {
		if (lieferung == null || bestellungId == null) {
			return null;
		}
		
		Bestellung bestellung = bs.findBestellungById(bestellungId);
		/*
		 * Status auf Verschickt setzen
		 */
		bs.updateBestellung(bestellung);
		
		lieferung.setBestellung(bestellung);
		
		lieferung.setId(KEINE_ID);
		
		em.persist(lieferung);		
		return lieferung;
	}	

	public Bestellung findBestellungByLieferungId(Long id) {
		final Bestellung bestellung = em.find(Bestellung.class, id);
		return bestellung;
	}
}


