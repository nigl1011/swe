package de.shop.lieferverwaltung.service;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.jboss.logging.Logger;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.artikelverwaltung.service.InvalidArtikelIdException;
import de.shop.bestellverwaltung.service.BestellungServiceImpl;
import de.shop.lieferverwaltung.domain.Lieferung;
//import de.shop.lieferverwaltung.service.InvalidLieferIdException;
import de.shop.util.IdGroup;
import de.shop.util.Log;
import de.shop.util.ValidatorProvider;

@Log
public class LieferService implements Serializable {
	private static final long serialVersionUID = -3907630579991760308L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@PersistenceContext
	private transient EntityManager em;
	
	@Inject
	private BestellungServiceImpl bs;
	
	@Inject
	private ValidatorProvider validatorProvider;
	
	@PostConstruct
	private void postConstruct() {
		LOGGER.debugf("CDI-faehiges Bean %s wurde erzeugt", this);
	}
	
	@PreDestroy
	private void preDestroy() {
		LOGGER.debugf("CDI-faehiges Bean %s wird geloescht", this);
	}
	
	public Lieferung findLieferungById(Long id, Locale locale) {
		validateLieferungId(id, locale);
		final Lieferung lieferung = em.find(Lieferung.class, id);
		return lieferung;
	}

	//public List<Lieferung> FindLieferungByBestellung ()

	private void validateLieferungId(Long id, Locale locale) {
		final Validator validator = validatorProvider.getValidator(locale);
		final Set<ConstraintViolation<Artikel>> violations = validator
				.validateValue(Artikel.class, "id", id, IdGroup.class);
		if (!violations.isEmpty())
			throw new InvalidArtikelIdException(id, violations);
	}
	

	
	public Lieferung createLieferung(Lieferung lieferung, Locale locale) {
		if (lieferung == null) {
			return lieferung;
		}
		// Werden alle Constraints beim Einfuegen gewahrt?
		validateLieferung(lieferung, locale, Default.class);
		em.persist(lieferung);
		return lieferung;
	}

	private void validateLieferung(Lieferung lieferung, Locale locale,
			Class<?>... groups) {
		// Werden alle Constraints beim Einfuegen gewahrt?
		final Validator validator = validatorProvider.getValidator(locale);

		final Set<ConstraintViolation<Lieferung>> violations = validator
				.validate(lieferung, groups);
		if (!violations.isEmpty()) {
			throw new InvalidLieferException(lieferung, violations);
		}
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

	public Lieferung updateLieferung(Lieferung lieferung, Locale locale) {
		if (lieferung == null) {
			return null;
		}
		em.detach(lieferung);
		// Werden alle Constraints beim Modifizieren gewahrt?
		validateLieferung(lieferung, locale, Default.class, IdGroup.class);
		em.merge(lieferung);

		return lieferung;
	}
}


