package de.shop.lieferverwaltung.service;

import de.shop.util.AbstractShopException;


public abstract class LieferServiceException extends AbstractShopException {
	private static final long serialVersionUID = 1L;

	public LieferServiceException(String msg) {
		super(msg);
	}
	
	public LieferServiceException(String msg, Throwable t) {
		super(msg, t);
	}
}
