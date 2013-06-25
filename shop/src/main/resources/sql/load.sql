DROP SEQUENCE hibernate_sequence;
CREATE SEQUENCE hibernate_sequence START WITH 5000;

INSERT INTO kunde (id, nachname, vorname, seit, art, familienstand_fk, newsletter, rabatt, umsatz, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (1,'Admin','Admin','01.01.2001','F',NULL,1,'0,1',0,'admin@hska.de','1','01.08.2006 00:00:00','01.08.2006 00:00:00','01.01.1990',1);
INSERT INTO kunde (id, nachname, vorname, seit, art, familienstand_fk, newsletter, rabatt, umsatz, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (2,'Nine','Glas','01.01.2001','F',NULL,1,'0,1',100.00,'nine@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','19.07.1990',0);
INSERT INTO kunde (id, nachname, vorname, seit, art, familienstand_fk, newsletter, rabatt, umsatz, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (3,'Kadda','Blu','01.01.2002','F',NULL,1,'0,8',10.00,'kadda@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','15.02.1997',0);
INSERT INTO kunde (id, nachname, vorname, seit, art, familienstand_fk, newsletter, rabatt, umsatz, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (4,'Andreas','Tube','01.01.2002','F',NULL,1,'0,8',10.00,'andi@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','15.02.1997',1);
INSERT INTO kunde (id, nachname, vorname, seit, art, familienstand_fk, newsletter, rabatt, umsatz, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (5,'Beni','Socke','01.01.2002','F',NULL,1,'0,2',10000.00,'beni@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','19.02.1989',1);
INSERT INTO kunde (id, nachname, vorname, seit, art, familienstand_fk, newsletter, rabatt, umsatz, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (6,'Isabella','Kiwi','01.01.2001','P',NULL,1,'0,2',870.00,'isi@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','12.08.1990',0);

INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, erzeugt, aktualisiert) VALUES (1,'76133','Karlsruhe','Moltkeweg','30',1,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, erzeugt, aktualisiert) VALUES (2,'76133','Karlsruhe','Moltkeweg','31',2,'02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, erzeugt, aktualisiert) VALUES (3,'76133','Karlsruhe','Silvertal','32',3,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, erzeugt, aktualisiert) VALUES (4,'76133','Karlsruhe','Ottobaumstr','33',4,'04.08.2006 00:00:00','04.08.2006 00:00:00');
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, erzeugt, aktualisiert) VALUES (5,'76133','Karlsruhe','Moltkeweg','34',5,'05.08.2006 00:00:00','05.08.2006 00:00:00');
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, erzeugt, aktualisiert) VALUES (6,'76133','Karlsruhe','Moltkeweg','35',6,'06.08.2006 00:00:00','06.08.2006 00:00:00');

INSERT INTO kunde_hobby (kunde_fk, hobby_fk) VALUES (1,0);
INSERT INTO kunde_hobby (kunde_fk, hobby_fk) VALUES (2,1);
INSERT INTO kunde_hobby (kunde_fk, hobby_fk) VALUES (3,0);
INSERT INTO kunde_hobby (kunde_fk, hobby_fk) VALUES (4,2);
INSERT INTO kunde_hobby (kunde_fk, hobby_fk) VALUES (5,1);
INSERT INTO kunde_hobby (kunde_fk, hobby_fk) VALUES (6,2);

INSERT INTO wartungsvertrag (nr, datum, inhalt, kunde_fk, idx, erzeugt, aktualisiert) VALUES (1,'31.01.2005','Wartungsvertrag_1_K101',1,0,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO wartungsvertrag (nr, datum, inhalt, kunde_fk, idx, erzeugt, aktualisiert) VALUES (2,'31.01.2006','Wartungsvertrag_2_K101',2,1,'02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO wartungsvertrag (nr, datum, inhalt, kunde_fk, idx, erzeugt, aktualisiert) VALUES (1,'30.06.2006','Wartungsvertrag_1_K102',3,0,'03.08.2006 00:00:00','03.08.2006 00:00:00');


INSERT INTO bestellung (id, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (400,1,0,1,300,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO bestellung (id, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (401,2,1,1,200,'02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO bestellung (id, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (402,3,0,1,100,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO bestellung (id, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (403,4,1,1,50,'04.08.2006 00:00:00','04.08.2006 00:00:00');
INSERT INTO bestellung (id, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (404,5,0,1,10,'05.08.2006 00:00:00','05.08.2006 00:00:00');

INSERT INTO lieferung (id, liefernr, transport_art_fk, erzeugt, aktualisiert) VALUES (600,'20051005-001',0,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO lieferung (id, liefernr, transport_art_fk, erzeugt, aktualisiert) VALUES (601,'20051005-002',1,'02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO lieferung (id, liefernr, transport_art_fk, erzeugt, aktualisiert) VALUES (602,'20051005-003',2,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO lieferung (id, liefernr, transport_art_fk, erzeugt, aktualisiert) VALUES (603,'20051008-001',3,'04.08.2006 00:00:00','04.08.2006 00:00:00');


INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (400,600);
INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (401,600);
INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (402,601);
INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (402,602);
INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (403,602);
INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (404,603);

INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (300,'01.08.2006 00:00:00','Stuhl','01.08.2006 00:00:00','pink',1,119,1);
INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (301,'01.08.2006 00:00:00','Werkbank','01.08.2006 00:00:00','braun',1,119,1);
INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (302,'01.08.2006 00:00:00','Doppelbett','01.08.2006 00:00:00','schwarz',1,299,1);
INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (303,'01.08.2006 00:00:00','Buerostuhl','01.08.2006 00:00:00','schwarz',2,890,1);
INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (304,'01.08.2006 00:00:00','Arbeitsplatte','01.08.2006 00:00:00','hellbraun',1,169,1);


INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (500,400,301,1,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (501,400,301,4,1);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (502,401,302,5,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (503,402,303,3,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (504,402,304,2,1);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (505,403,304,1,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (506,404,303,5,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (507,404,301,2,1);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (508,404,302,8,1);

CREATE INDEX adresse__kunde_index ON adresse(kunde_fk);
CREATE INDEX kunde_hobby__kunde_index ON kunde_hobby(kunde_fk);
CREATE INDEX wartungsvertrag__kunde_index ON wartungsvertrag(kunde_fk);
CREATE INDEX bestellung__kunde_index ON bestellung(kunde_fk);
--CREATE INDEX bestpos__bestellung_index ON bestellposten(bestellung_fk);
--CREATE INDEX bestpos__artikel_index ON bestellposten(artikel_fk);

DROP TABLE geschlecht;
CREATE TABLE geschlecht(id NUMBER(1) NOT NULL PRIMARY KEY, txt VARCHAR2(10) NOT NULL UNIQUE) CACHE;
INSERT INTO geschlecht VALUES (0, 'MAENNLICH');
INSERT INTO geschlecht VALUES (1, 'WEIBLICH');

DROP TABLE familienstand;
CREATE TABLE familienstand(id NUMBER(1) NOT NULL PRIMARY KEY, txt VARCHAR2(12) NOT NULL UNIQUE) CACHE;
INSERT INTO familienstand VALUES(0, 'LEDIG');
INSERT INTO familienstand VALUES(1, 'VERHEIRATET');
INSERT INTO familienstand VALUES(2, 'GESCHIEDEN');
INSERT INTO familienstand VALUES(3, 'VERWITWET');

DROP TABLE hobby;
CREATE TABLE hobby(id NUMBER(1) NOT NULL PRIMARY KEY, txt VARCHAR2(16) NOT NULL UNIQUE) CACHE;
INSERT INTO hobby VALUES (0, 'SPORT');
INSERT INTO hobby VALUES (1, 'LESEN');
INSERT INTO hobby VALUES (2, 'REISEN');

DROP TABLE transport_art;
CREATE TABLE transport_art(id NUMBER(1) NOT NULL PRIMARY KEY, txt VARCHAR2(8) NOT NULL UNIQUE) CACHE;
INSERT INTO transport_art VALUES (0, 'STRASSE');
INSERT INTO transport_art VALUES (1, 'SCHIENE');
INSERT INTO transport_art VALUES (2, 'LUFT');
INSERT INTO transport_art VALUES (3, 'WASSER');

-- ===============================================================================
-- Fremdschluessel in den bereits *generierten* Tabellen auf die obigen "Enum-Tabellen" anlegen
-- ===============================================================================
ALTER TABLE kunde ADD CONSTRAINT kunde__geschlecht_fk FOREIGN KEY (geschlecht_fk) REFERENCES geschlecht;
ALTER TABLE kunde ADD CONSTRAINT kunde__familienstand_fk FOREIGN KEY (familienstand_fk) REFERENCES familienstand;
ALTER TABLE kunde_hobby ADD CONSTRAINT kunde_hobby__hobby_fk FOREIGN KEY (hobby_fk) REFERENCES hobby;
ALTER TABLE lieferung ADD CONSTRAINT lieferung__transport_art_fk FOREIGN KEY (transport_art_fk) REFERENCES transport_art;


