--kunde
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (2,'Nine','Glas','01.01.2001','F',1,null,1,'0,1',100.00,1,'nine@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','19.07.1990',0);
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (3,'Kadda','Blu','01.01.2002','F',1,null,1,'0,8',10.00,1,'kadda@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','15.02.1997',0);
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (4,'Andreas','Tube','01.01.2002','F',1,null,1,'0,8',10.00,1,'andi@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','15.02.1997',1);
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (5,'Beni','Socke','01.01.2002','F',1,null,1,'0,2',10000.00,1,'beni@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','19.02.1989',1);
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, erzeugt, aktualisiert,geburtsdatum,geschlecht) VALUES (6,'Isabella','Kiwi','01.01.2001','P',1,'L',1,'0,2',870.00,1,'isi@hska.de','1','01.08.2006 00:00:00','01.08.2006 02:00:00','12.08.1990',0);

--kunde-hobby
INSERT INTO kunde_hobby (kunde_fk, hobby) VALUES (6,'L');

--adresse
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, version, erzeugt, aktualisiert) VALUES (1,'76133','Karlsruhe','Moltkeweg','30',2,1,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, version, erzeugt, aktualisiert) VALUES (3,'76133','Karlsruhe','Silvertal','32',3,1,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, version, erzeugt, aktualisiert) VALUES (4,'76133','Karlsruhe','Ottobaumstr','33',4,1,'04.08.2006 00:00:00','04.08.2006 00:00:00');
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, version, erzeugt, aktualisiert) VALUES (5,'76133','Karlsruhe','Moltkeweg','34',5,1,'05.08.2006 00:00:00','05.08.2006 00:00:00');
INSERT INTO adresse (id, plz, ort, strasse, hausnr, kunde_fk, version, erzeugt, aktualisiert) VALUES (6,'76133','Karlsruhe','Moltkeweg','35',6,1,'06.08.2006 00:00:00','06.08.2006 00:00:00');

--wartungsvertrag
INSERT INTO wartungsvertrag (nr, datum, inhalt, kunde_fk, idx, erzeugt, aktualisiert, version) VALUES (1,'31.01.2005','Wartungsvertrag_1_K101',2,0,'01.08.2006 00:00:00','01.08.2006 00:00:00',1);
INSERT INTO wartungsvertrag (nr, datum, inhalt, kunde_fk, idx, erzeugt, aktualisiert, version) VALUES (2,'31.01.2006','Wartungsvertrag_2_K101',3,1,'02.08.2006 00:00:00','02.08.2006 00:00:00',1);
INSERT INTO wartungsvertrag (nr, datum, inhalt, kunde_fk, idx, erzeugt, aktualisiert, version) VALUES (1,'30.06.2006','Wartungsvertrag_1_K102',4,0,'03.08.2006 00:00:00','03.08.2006 00:00:00',1);

--artikel
INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (300,'01.08.2006 00:00:00','Stuhl','01.08.2006 00:00:00','pink',1,119,1);
INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (301,'01.08.2006 00:00:00','Werkbank','01.08.2006 00:00:00','braun',1,119,1);
INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (302,'01.08.2006 00:00:00','Doppelbett','01.08.2006 00:00:00','schwarz',1,299,1);
INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (303,'01.08.2006 00:00:00','Buerostuhl','01.08.2006 00:00:00','schwarz',2,890,1);
INSERT INTO artikel (id, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (304,'01.08.2006 00:00:00','Arbeitsplatte','01.08.2006 00:00:00','hellbraun',1,169,1);

--bestellung
INSERT INTO bestellung (id, version, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (400,1,2,0,'I',300,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO bestellung (id, version, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (401,1,3,1,'I',200,'02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO bestellung (id, version, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (402,1,4,0,'I',100,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO bestellung (id, version, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (403,1,5,1,'I',50,'04.08.2006 00:00:00','04.08.2006 00:00:00');
INSERT INTO bestellung (id, version, kunde_fk, idx, status, gesamtpreis, erzeugt, aktualisiert) VALUES (404,1,6,0,'I',10,'05.08.2006 00:00:00','05.08.2006 00:00:00');

--bestellposten
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (500,400,301,1,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (501,400,301,4,1);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (502,401,302,5,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (503,402,303,3,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (504,402,304,2,1);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (505,403,304,1,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (506,404,303,5,0);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (507,404,301,2,1);
INSERT INTO bestellposten (id, bestellung_fk, artikel_fk, anzahl, idx) VALUES (508,404,302,8,1);























