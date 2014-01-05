--kunde
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, aktualisiert,geburtsdatum,geschlecht) VALUES (2,'Glaser','Nina','01.01.2001','P',1,null,1,'0,1',100.00,1,'nine@hska.de','1HNeOiZeFu7gP1lxi5tdAwGcB9i2xR+Q2jpmbuwTqzU=','01.08.2006 02:00:00','19.07.1990','W');
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, aktualisiert,geburtsdatum,geschlecht) VALUES (3,'Fischer','Kadda','01.01.2002','P',1,null,1,'0,4',10.00,1,'kadda@hska.de','TgdAhWK+24tgzgXB3s/jrRa3IjCWfeAfZAt+Rym0n84=','01.08.2006 02:00:00','15.02.1997','W');
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, aktualisiert,geburtsdatum,geschlecht) VALUES (4,'Mueller','Michi','01.01.2002','P',1,null,1,'0,3',10.00,1,'andi@hska.de','SyJ3d9TdH8Ycb4hPSGQdArTRIdP9Moywi1Ux/Kzav4o=','01.08.2006 02:00:00','15.02.1997','M');
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, aktualisiert,geburtsdatum,geschlecht) VALUES (5,'Becker','Marcel','01.01.2002','P',1,null,1,'0,2',10000.00,1,'beni@hska.de','7y0SfeN7lCuq0GFF5UsMYZofIjJ7LrvPvsePVWSv450=','01.08.2006 02:00:00','19.02.1989','M');
INSERT INTO kunde (id, nachname, vorname, seit, art, kategorie, familienstand, newsletter, rabatt, umsatz, version, email, password, aktualisiert,geburtsdatum,geschlecht) VALUES (6,'Kiwis','Isabella','01.01.2001','P',1,'L',1,'0,2',870.00,1,'isi@hska.de','5/bAEXdujbfNMwtUF0/Xb30CFrYSOHpf/PuB5vCRloM=','01.08.2006 02:00:00','12.08.1990','W');



-- FILE_TBL

CALL insert_file_kunde(6,1,0,'image.png','Privatkunde_6.png','png','I','01.01.2007 01:00:00','01.01.2007 01:00:00');
CALL insert_file_kunde(5,2,0,'video.mp4','Privatkunde_5.mp4','mp4','V','01.01.2007 01:00:00','01.01.2007 01:00:00');



--Kunde_rolle
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (2,'admin');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (2,'mitarbeiter');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (2,'kunde');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (2,'abteilungsleiter');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (3,'mitarbeiter');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (3,'kunde');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (4,'kunde');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (4,'mitarbeiter');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (5,'kunde');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (5,'mitarbeiter');
INSERT INTO kunde_rolle(kunde_fk, rolle) VALUES (6,'mitarbeiter');


--kunde-hobby
INSERT INTO kunde_hobby (kunde_fk, hobby) VALUES (2,'S');
INSERT INTO kunde_hobby (kunde_fk, hobby) VALUES (3,'S');
INSERT INTO kunde_hobby (kunde_fk, hobby) VALUES (4,'R');
INSERT INTO kunde_hobby (kunde_fk, hobby) VALUES (5,'L');
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
INSERT INTO artikel (id, version, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (300,1,'01.08.2006 00:00:00','Stuhl','01.08.2006 00:00:00','Pink','kueche',119,1);
INSERT INTO artikel (id, version, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (301,1,'01.08.2006 00:00:00','Werkbank','01.08.2006 00:00:00','Braun','werkstatt',119,1);
INSERT INTO artikel (id, version, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (302,1,'01.08.2006 00:00:00','Doppelbett','01.08.2006 00:00:00','Schwarz','schlafzimmer',299,1);
INSERT INTO artikel (id, version, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (303,1,'01.08.2006 00:00:00','Buerostuhl','01.08.2006 00:00:00','Schwarz','buero',890,1);
INSERT INTO artikel (id, version, aktualisiert, bezeichnung, erstellt, farbe, kategorie, preis, verfuegbar) VALUES (304,1,'01.08.2006 00:00:00','Arbeitsplatte','01.08.2006 00:00:00','Hellbraun','werkstatt',169,1);

--bestellung
INSERT INTO bestellung (id, version, kunde_fk, idx, status, erzeugt, aktualisiert) VALUES (400,1,2,0,'V','01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO bestellung (id, version, kunde_fk, idx, status, erzeugt, aktualisiert) VALUES (401,1,3,1,'V','02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO bestellung (id, version, kunde_fk, idx, status, erzeugt, aktualisiert) VALUES (402,1,4,0,'V','03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO bestellung (id, version, kunde_fk, idx, status, erzeugt, aktualisiert) VALUES (403,1,5,1,'V','04.08.2006 00:00:00','04.08.2006 00:00:00');
INSERT INTO bestellung (id, version, kunde_fk, idx, status, erzeugt, aktualisiert) VALUES (404,1,6,0,'I','05.08.2006 00:00:00','05.08.2006 00:00:00');

--bestellposten
INSERT INTO bestellposten (id, version, bestellung_fk, artikel_fk, anzahl, idx) VALUES (500,1,400,301,1,0);
INSERT INTO bestellposten (id, version, bestellung_fk, artikel_fk, anzahl, idx) VALUES (501,1,400,301,4,1);
INSERT INTO bestellposten (id, version, bestellung_fk, artikel_fk, anzahl, idx) VALUES (502,1,401,302,5,0);
INSERT INTO bestellposten (id, version, bestellung_fk, artikel_fk, anzahl, idx) VALUES (503,1,402,303,3,0);
INSERT INTO bestellposten (id, version, bestellung_fk, artikel_fk, anzahl, idx) VALUES (504,1,402,304,2,1);
INSERT INTO bestellposten (id, version, bestellung_fk, artikel_fk, anzahl, idx) VALUES (505,1,403,304,1,0);
INSERT INTO bestellposten (id, version, bestellung_fk, artikel_fk, anzahl, idx) VALUES (506,1,404,303,5,0);
INSERT INTO bestellposten (id, version, bestellung_fk, artikel_fk, anzahl, idx) VALUES (507,1,404,301,2,1);
INSERT INTO bestellposten (id, version, bestellung_fk, artikel_fk, anzahl, idx) VALUES (508,1,404,302,8,1);

--lieferung
INSERT INTO lieferung (id, erzeugt, aktualisiert, bestellung_fk) VALUES (700,'01.10.2006 00:00:00','01.10.2006 00:00:00',400);
INSERT INTO lieferung (id, erzeugt, aktualisiert, bestellung_fk) VALUES (701,'05.10.2006 00:00:00','05.10.2006 00:00:00',401);
INSERT INTO lieferung (id, erzeugt, aktualisiert, bestellung_fk) VALUES (702,'11.10.2006 00:00:00','11.10.2006 00:00:00',402);
INSERT INTO lieferung (id, erzeugt, aktualisiert, bestellung_fk) VALUES (703,'20.10.2006 00:00:00','20.10.2006 00:00:00',403);























