CREATE OR REPLACE TYPE NameType AS OBJECT (
	firstname VARCHAR2(30),
	lastname VARCHAR2(30)
	);
	/

CREATE OR REPLACE TYPE AuthorType AS OBJECT (
	id NUMBER,
	name NameType,
	gender VARCHAR2(7),
	numofbooks NUMBER
	);
	/

CREATE TABLE AuthorTable OF AuthorType (
	id PRIMARY KEY
	);
	

CREATE OR REPLACE TYPE BookType AS OBJECT(
	id NUMBER,
	title VARCHAR2(50),
	author REF AuthorType,
	publisher VARCHAR2(30),
	stock NUMBER,
	price NUMBER,
	
	MEMBER FUNCTION addpriceofbooks(authorID IN NUMBER) RETURN NUMBER,
	MEMBER FUNCTION findstockwithzero RETURN NUMBER
	);
	/
	
CREATE TABLE BookTable OF BookType(
	id PRIMARY KEY,	
	author SCOPE IS AuthorTable CHECK (author IS NOT NULL)
	);
	


