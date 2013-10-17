package de.shop.util.persistence;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class EntityManagerProducer {
	@PersistenceContext
	@Produces
	private EntityManager em;
}
//@Produces
//@ApplicationScoped
//private static EntityManagerFactory createEntityManagerFactory() {
//	return Persistence.createEntityManagerFactory("de.shop.PU");
//}
//
///*
// * Aufruf der Disposer-Methode beim Herunterfahren der Anwendung wegen @ApplicationScoped
// */
//@SuppressWarnings("unused")
//private static void closeEntityManagerFactory(@Disposes EntityManagerFactory emf) {
//	if (emf.isOpen()) {
//		emf.close();
//	}
//}
//
//@Produces
//@RequestScoped
//private static EntityManager createEntityManager(EntityManagerFactory emf) {
//	return emf.createEntityManager();
//}
//
///*
// * Aufruf der Disposer-Methode am Ende des Requests wegen @RequestScoped
// */
//@SuppressWarnings("unused")
//private static void closeEntityManager(@Disposes EntityManager em) {
//	if (em.isOpen()) {
//		em.close();
//	}
//}

