package de.shop.bestellverwaltung.rest;

import static de.shop.util.TestConstants.BESTELLUNGEN_ID_URI;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_PATH_PARAM;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.fest.assertions.api.Assertions.assertThat;

import javax.ws.rs.core.Response;




import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.util.AbstractResourceTest;

@RunWith(Arquillian.class)
public class BestellungResourceTest extends AbstractResourceTest {

	@Test
	@InSequence(1)
	public void findBestellungById() {
		
		//Given
		Long bestellungId = Long.valueOf(400);
		
		//When
		final Response response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
				                                  .resolveTemplate(BESTELLUNGEN_ID_PATH_PARAM, bestellungId)
				                                  .request()
				                                  .accept(APPLICATION_JSON)
				                                  .get();
		
		//Then
		assertThat(response.getStatus()).isEqualTo(HTTP_OK);
		final Bestellung bestellung = response.readEntity(Bestellung.class);
		
		assertThat(bestellung.getId()).isEqualTo(bestellungId);
		assertThat(bestellung.getBestellposten()).isNotEmpty();
	}
}
