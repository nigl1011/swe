package de.shop.lieferverwaltung.service;

import java.util.Collection;

import javax.validation.ConstraintViolation;

import de.shop.lieferverwaltung.domain.Lieferung;

public abstract class AbstractLieferValidationException extends AbstractLieferServiceException {
	private static final long serialVersionUID = 1L;
	private final Collection<ConstraintViolation<Lieferung>> violations;
	
	public AbstractLieferValidationException(Collection<ConstraintViolation<Lieferung>> violations) {
		super("Violations: " + violations);
		this.violations = violations;
	}
	
	public Collection<ConstraintViolation<Lieferung>> getViolations() {
		return violations;
	}

}
