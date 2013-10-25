DROP SEQUENCE hibernate_sequence;
CREATE SEQUENCE hibernate_sequence START WITH 5000;

--CREATE TABLE transport_art(id NUMBER(1) NOT NULL PRIMARY KEY, txt VARCHAR2(8) NOT NULL UNIQUE) CACHE;
--CREATE TABLE geschlecht(id NUMBER(1) NOT NULL PRIMARY KEY, txt VARCHAR2(10) NOT NULL UNIQUE) CACHE;
--CREATE TABLE hobby(id NUMBER(1) NOT NULL PRIMARY KEY, txt VARCHAR2(16) NOT NULL UNIQUE) CACHE;
--CREATE TABLE familienstand(id NUMBER(1) NOT NULL PRIMARY KEY, txt VARCHAR2(12) NOT NULL UNIQUE) CACHE;



--CREATE INDEX adresse__kunde_index ON adresse(kunde_fk);
--CREATE INDEX kunde_hobby__kunde_index ON kunde_hobby(kunde_fk);
--CREATE INDEX wartungsvertrag__kunde_index ON wartungsvertrag(kunde_fk);
--CREATE INDEX bestellung__kunde_index ON bestellung(kunde_fk);

--CREATE INDEX bestpos__bestellung_index ON bestellposten(bestellung_fk);
--CREATE INDEX bestpos__artikel_index ON bestellposten(artikel_fk);
