package de.shop.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProducer {
	@Produces
	@ApplicationScoped
	private static EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("de.shop.PU");
	}
	
	/*
	 * Aufruf der Disposer-Methode beim Herunterfahren der Anwendung wegen @ApplicationScoped
	 */
	@SuppressWarnings("unused")
	private static void closeEntityManagerFactory(@Disposes EntityManagerFactory emf) {
		if (emf.isOpen()) {
			emf.close();
		}
	}

	@Produces
	@RequestScoped
	private static EntityManager createEntityManager(EntityManagerFactory emf) {
		return emf.createEntityManager();
	}
	
	/*
	 * Aufruf der Disposer-Methode am Ende des Requests wegen @RequestScoped
	 */
	@SuppressWarnings("unused")
	private static void closeEntityManager(@Disposes EntityManager em) {
		if (em.isOpen()) {
			em.close();
		}
	}
}
