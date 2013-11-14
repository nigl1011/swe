package de.shop.lieferverwaltung.domain;

import static de.shop.util.Constants.KEINE_ID;
//import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.net.URI;
//import java.util.ArrayList;
//import java.util.Collections;
import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
//import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
//import javax.persistence.OrderColumn;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.Valid;
//import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;



//import org.codehaus.jackson.annotate.JsonIgnore;
//import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.logging.Logger;

import de.shop.bestellverwaltung.domain.Bestellung;

@Entity
@Table(name = "lieferung")
@NamedQueries({
	@NamedQuery(name  = Lieferung.FIND_LIEFERUNGEN_BY_LIEFERNR_FETCH_BESTELLUNGEN,
                query = "SELECT l"
                	    + " FROM Lieferung l LEFT JOIN FETCH l.bestellungen"
			            + " WHERE l.lieferNr LIKE :" + Lieferung.PARAM_LIEFERNR)
})

@XmlRootElement
public class Lieferung implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private static final long MIN_ID = 1;
	
	private static final int LIEFERNR_LENGTH = 12;
	
	private static final String PREFIX = "Lieferung.";
	public static final String FIND_LIEFERUNGEN_BY_LIEFERNR_FETCH_BESTELLUNGEN =
		                       PREFIX + "findLieferungenByLieferNrFetchBestellungen";
	public static final String PARAM_LIEFERNR = "lieferNr";
	
	@Id
	@GeneratedValue
	@Column(nullable = false, updatable = false)
	@Min(value = MIN_ID, message = "{lieferverwaltung.lieferung.id.min}")
	private Long id = KEINE_ID;
	
	@Column(length = LIEFERNR_LENGTH, unique = true)
	@NotNull(message = "{bestellverwaltung.lieferung.lieferNr.notNull}")
	private String lieferNr;
	
	@Column(name = "transport_art_fk", length = 3)
	private TransportType transportArt;
	
	@Basic(optional = false)
	@Temporal(TIMESTAMP)
	@XmlTransient
	private Date erzeugt;
	
	@Basic(optional = false)
	@Temporal(TIMESTAMP)
	@XmlTransient
	private Date aktualisiert;

	
	@OneToOne(cascade = { PERSIST, REMOVE }, mappedBy = "lieferung")
	@Valid
	@NotNull(message = "{lieferverwaltung.lieferung.bestellung.notNull}")
	private Bestellung bestellung_fk;
	
	
	public void setValues(Lieferung l) {
		lieferNr = l.lieferNr;
		transportArt = l.transportArt;
	}
	

	public Lieferung() {
		super();
	}
	
	public Lieferung(String lieferNr, TransportType transportArt) {
		super();
		this.lieferNr = lieferNr;
		this.transportArt = transportArt;
	}
	
	@Transient
	private URI bestellungUri;

	/*
	 * public Lieferung(Long id, Date lieferdatum, Timestamp aktuell) { super();
	 * this.id = id; this.lieferdatum = lieferdatum; this.aktuell = aktuell; }
	 */
	@PrePersist
	private void prePersist() {
		erzeugt = new Date();
		aktualisiert = new Date();
	}
	
	@PostPersist
	private void postPersist() {
		LOGGER.debugf("Neue Lieferung mit ID=%d", id);
	}
	
	@PreUpdate
	private void preUpdate() {
		aktualisiert = new Date();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAktualisiert() {
		return aktualisiert == null ? null : (Date) aktualisiert.clone();
	}
	public void setAktualisiert(Date aktualisiert) {
		this.aktualisiert = aktualisiert == null ? null : (Date) aktualisiert.clone();
	}

	public String getLieferNr() {
		return lieferNr;
	}
	public void setLieferNr(String lieferNr) {
		this.lieferNr = lieferNr;
	}

	public TransportType getTransportArt() {
		return transportArt;
	}

	public void setTransportArt(TransportType transportArt) {
		this.transportArt = transportArt;
	}
	public Bestellung getBestellung() {
		return bestellung_fk;
	}
	
	public void setBestellung(Bestellung bestellung) {
		if (this.bestellung_fk == null) {
			this.bestellung_fk = bestellung;
		}
	}
		
		// Wiederverwendung der vorhandenen Collection
		
	/*	
		this.bestellungen.clear();
		if (bestellungen != null) {
			this.bestellungen.addAll(bestellungen);
		}
	}
	
	public void addBestellung(Bestellung bestellung) {
		if (bestellungen == null) {
			bestellungen = new HashSet<>();
		}
		bestellungen.add(bestellung);
	}

	public List<Bestellung> getBestellungenAsList() {
		return bestellungen == null ? null : new ArrayList<>(bestellungen);
	}

	public void setBestellungenAsList(List<Bestellung> bestellungen) {
		this.bestellungen = bestellungen == null ? null : new HashSet<>(bestellungen);
	}
	
	*/
	public Date getErzeugt() {
		return erzeugt == null ? null : (Date) erzeugt.clone();
	}
	public void setErzeugt(Date erzeugt) {
		this.erzeugt = erzeugt == null ? null : (Date) erzeugt.clone();
	}

	public URI getBestellungUri() {
		return bestellungUri;
	}

	public void setBestellungUri(URI bestellungUri) {
		this.bestellungUri = bestellungUri;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lieferNr == null) ? 0 : lieferNr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Lieferung other = (Lieferung) obj;
		
		if (lieferNr == null) {
			if (other.lieferNr != null) {
				return false;
			}
		}
		else if (!lieferNr.equals(other.lieferNr)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Lieferung [id=" + id + ", lieferNr=" + lieferNr + ", transportArt=" + transportArt
		       + ", erzeugt=" + erzeugt
		       + ", aktualisiert=" + aktualisiert + ']';
	}

}
