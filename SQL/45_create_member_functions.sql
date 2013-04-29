CREATE OR REPLACE TYPE BODY BookType AS 
	MEMBER FUNCTION addpriceofbooks(authorID IN NUMBER) RETURN NUMBER
	IS
		sumprice BookTable.price%TYPE;
	BEGIN
		SELECT SUM(price) INTO sumprice FROM BookTable
		WHERE DEREF(author).id = authorID;
		
		RETURN sumprice;
	END;
	
	MEMBER FUNCTION findstockwithzero RETURN NUMBER
	IS
		bookid NUMBER;
	BEGIN
		SELECT id INTO bookid FROM BookTable WHERE stock = 0;
		RETURN bookid;
	END;
END;
/
