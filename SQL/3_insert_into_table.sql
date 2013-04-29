INSERT INTO BookTable(id, title, author, publisher, stock, price) VALUES(1,'The lord of the rings 1',(SELECT REF(au) FROM AuthorTable au WHERE au.id=3),'Seeds',10,11000);
INSERT INTO BookTable(id, title, author, publisher, stock, price) VALUES(2,'Dream on',(SELECT REF(au) FROM AuthorTable au WHERE au.id=4),'Sam and Pakers',0,16000);
INSERT INTO BookTable(id, title, author, publisher, stock, price) VALUES(3,'Harry Potter and the deathly hallows 1',(SELECT REF(au) FROM AuthorTable au WHERE au.id=1),'Literature note',6,8500);
INSERT INTO BookTable(id, title, author, publisher, stock, price) VALUES(4,'The lord of the rings 2',(SELECT REF(au) FROM AuthorTable au WHERE au.id=3),'Seeds',8,11000);

UPDATE AuthorTable au SET au.numofbooks = 1 WHERE au.id = 1;
UPDATE AuthorTable au SET au.numofbooks = 2 WHERE au.id = 3;
UPDATE AuthorTable au SET au.numofbooks = 1 WHERE au.id = 4;