package de.shop.lieferverwaltung.domain;

import static de.shop.util.Constants.KEINE_ID;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Date;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;







import org.jboss.logging.Logger;

import de.shop.bestellverwaltung.domain.Bestellung;

@Entity
@Table(name = "lieferung")
/*@NamedQueries({
	@NamedQuery(name  = Lieferung.FIND_LIEFERUNGEN_BY_LIEFERNR_FETCH_BESTELLUNGEN,
                query = "SELECT l"
                	    + " FROM Lieferung l LEFT JOIN FETCH l.bestellungen"
			            + " WHERE l.lieferNr LIKE :" + Lieferung.PARAM_LIEFERNR)
})
*/
@XmlRootElement
public class Lieferung implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private static final long MIN_ID = 1;
	

	
	private static final String PREFIX = "Lieferung.";
	public static final String FIND_LIEFERUNGEN_BY_LIEFERNR_FETCH_BESTELLUNGEN =
		                       PREFIX + "findLieferungenByLieferNrFetchBestellungen";
	public static final String PARAM_LIEFERNR = "lieferNr";
	
	@Id
	@GeneratedValue
	@Column(nullable = false, updatable = false)
	@Min(value = MIN_ID, message = "{lieferverwaltung.lieferung.id.min}")
	private Long id = KEINE_ID;
	
	
	@Basic(optional = false)
	@Temporal(TIMESTAMP)
	@XmlTransient
	private Date erzeugt;
	
	@Basic(optional = false)
	@Temporal(TIMESTAMP)
	@XmlTransient
	private Date aktualisiert;

	
	@OneToOne
	@JoinColumn(name = "bestellung_fk", nullable=false, unique=true)
	private Bestellung bestellung;
	
	@Transient
	@XmlTransient
	private URI bestellungUri;
	
	


	public Lieferung() {
		super();
	}
	

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

	@XmlTransient
	public Bestellung getBestellung() {
		return bestellung;
	}
	
	public void setBestellung(Bestellung bestellung) {
		if (this.bestellung == null) {
			this.bestellung = bestellung;
		}
	}
	
	public URI getBestellungUri() {
		return bestellungUri;
	}


	public void setBestellungUri(URI bestellungUri) {
		this.bestellungUri = bestellungUri;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aktualisiert == null) ? 0 : aktualisiert.hashCode());
		result = prime * result
				+ ((bestellung == null) ? 0 : bestellung.hashCode());
		result = prime * result + ((erzeugt == null) ? 0 : erzeugt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lieferung other = (Lieferung) obj;
		if (aktualisiert == null) {
			if (other.aktualisiert != null)
				return false;
		} else if (!aktualisiert.equals(other.aktualisiert))
			return false;
		if (bestellung == null) {
			if (other.bestellung != null)
				return false;
		} else if (!bestellung.equals(other.bestellung))
			return false;
		if (erzeugt == null) {
			if (other.erzeugt != null)
				return false;
		} else if (!erzeugt.equals(other.erzeugt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Lieferung [id=" + id + ", erzeugt=" + erzeugt
				+ ", aktualisiert=" + aktualisiert + ", bestellung_fk="
				+ bestellung + "]";
	}

	public void setValues(Lieferung lieferung) {
		
		
	}




}
