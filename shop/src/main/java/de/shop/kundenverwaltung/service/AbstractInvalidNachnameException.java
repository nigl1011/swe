package de.shop.kundenverwaltung.service;

import java.util.Collection;

import javax.validation.ConstraintViolation;

import de.shop.kundenverwaltung.domain.AbstractKunde;

public abstract class AbstractInvalidNachnameException extends AbstractKundeValidationException {
	private static final long serialVersionUID = -8973151010781329074L;
	
	private final String nachname;
	
	public AbstractInvalidNachnameException(String nachname, 
			Collection<ConstraintViolation<AbstractKunde>> violations) {
		super(violations);
		this.nachname = nachname;
	}

	public String getNachname() {
		return nachname;
	}
}
