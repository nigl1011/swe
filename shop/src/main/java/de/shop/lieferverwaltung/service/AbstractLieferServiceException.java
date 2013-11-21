package de.shop.lieferverwaltung.service;

import de.shop.util.AbstractShopException;


public abstract class AbstractLieferServiceException extends AbstractShopException {
	private static final long serialVersionUID = 1L;

	public AbstractLieferServiceException(String msg) {
		super(msg);
	}
	
	public AbstractLieferServiceException(String msg, Throwable t) {
		super(msg, t);
	}
}
