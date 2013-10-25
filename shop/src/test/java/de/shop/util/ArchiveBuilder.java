package de.shop.util;

import static de.shop.util.TestConstants.WEB_PROJEKT;


import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public enum ArchiveBuilder {
	INSTANCE;
	
	static final String TEST_WAR = WEB_PROJEKT + ".war";
	
	private static final String CLASSES_DIR = "target/classes";
	private static final String WEBAPP_DIR = "src/main/webapp";
	private static final String WEBINF_CLASSES = "WEB-INF/classes";
	
	private WebArchive archive = ShrinkWrap.create(WebArchive.class, TEST_WAR);
	
	private void addWebInfWebsiten() {
		final JavaArchive tmp = ShrinkWrap.create(JavaArchive.class);
		tmp.as(ExplodedImporter.class).importDirectory(WEBAPP_DIR);
		archive.merge(tmp, "/");
	}
	
	private void addKlassen() {
		final JavaArchive tmp = ShrinkWrap.create(JavaArchive.class);
		tmp.as(ExplodedImporter.class).importDirectory(CLASSES_DIR);
		archive.merge(tmp, WEBINF_CLASSES);
	}
	
	
	private ArchiveBuilder() {
		addWebInfWebsiten();
		addKlassen();
	}
	
	public Archive<? extends Archive<?>> getArchive() {
		return archive;
	}
	
	public static ArchiveBuilder getInstance() {
		return INSTANCE;
	}
	
	
	
}
