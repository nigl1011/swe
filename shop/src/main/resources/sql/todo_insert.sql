--transport
/*INSERT INTO transport_art VALUES (0, 'STRASSE');
INSERT INTO transport_art VALUES (1, 'SCHIENE');
INSERT INTO transport_art VALUES (2, 'LUFT');
INSERT INTO transport_art VALUES (3, 'WASSER');

--lieferung
INSERT INTO lieferung (id, liefernr, transport_art_fk, erzeugt, aktualisiert) VALUES (600,'20051005-001',0,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO lieferung (id, liefernr, transport_art_fk, erzeugt, aktualisiert) VALUES (601,'20051005-002',1,'02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO lieferung (id, liefernr, transport_art_fk, erzeugt, aktualisiert) VALUES (602,'20051005-003',2,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO lieferung (id, liefernr, transport_art_fk, erzeugt, aktualisiert) VALUES (603,'20051008-001',3,'04.08.2006 00:00:00','04.08.2006 00:00:00');


--bestellung-lieferung
INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (400,600);
INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (401,601);
INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (402,602);
INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (403,603);
*/