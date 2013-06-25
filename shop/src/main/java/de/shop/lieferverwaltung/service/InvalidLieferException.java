package de.shop.lieferverwaltung.service;

import java.util.Collection;

import javax.validation.ConstraintViolation;

import de.shop.lieferverwaltung.domain.Lieferung;


/**
 * Exception, die ausgeloest wird, wenn die Attributwerte einer Lieferung nicht korrekt sind
 */
public class InvalidLieferException extends LieferValidationException {
	private static final long serialVersionUID = 1L;
	private final Lieferung lieferung;
	
	public InvalidLieferException(Lieferung lieferung,
			                     Collection<ConstraintViolation<Lieferung>> violations) {
		super(violations);
		this.lieferung = lieferung;
	}

	public Lieferung getLieferung() {
		return lieferung;
	}
}
