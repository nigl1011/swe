package de.shop.util;

import static de.shop.util.TestConstants.*;
import static org.apache.http.conn.ssl.SSLSocketFactory.TLS;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.ws.rs.client.Client;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;


public abstract class AbstractResourceTest {
	private static ResteasyClientBuilder resteasyClientBuilder;
	private static SSLSocketFactory socketFactory;
	
	private AbstractHttpClient httpClient;
	private Client client;
	
	@Deployment(name = ArchiveBuilder.TEST_WAR, testable = false)
	@OverProtocol(value = "Servlet 3.0")
	protected static Archive<?> deployment() {
		return ArchiveBuilder.getInstance().getArchive();
	}
	
	@BeforeClass
	public static void init() {
		resteasyClientBuilder = new ResteasyClientBuilder();
	
		try {
			final KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);
			final Path path = Paths.get(System.getenv("JBOSS_HOME"), "standalone",
											"configuration", TRUSTSTORE_NAME);
			try (InputStream stream = Files.newInputStream(path)) {
				trustStore.load(stream, TRUSTSTORE_PASSWORD.toCharArray());
			}
			socketFactory = new SSLSocketFactory(TLS, null, null, 
												trustStore, null, null,
												new BrowserCompatHostnameVerifier());
		}
		catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException
			       | KeyManagementException | UnrecoverableKeyException e) {
				throw new IllegalStateException(e);
		}
	
	}
	@Before
	public void before() {
		httpClient = new DefaultHttpClient();
		httpClient.getConnectionManager()
				  .getSchemeRegistry()
				  .register(new Scheme(HTTPS, PORT, socketFactory));
		
		ClientHttpEngine engine = new ApacheHttpClient4Engine(httpClient);
		client = resteasyClientBuilder.httpEngine(engine)
								      .build();
				 
	}
	@After
	public void after() {
		httpClient.getConnectionManager().shutdown();
	}
	
	protected Client getHttpsClient() {
		return client;
	}
	
}
