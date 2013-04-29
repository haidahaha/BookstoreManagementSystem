package kr.ac.kaist.iu1nguoi.java;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class BookstoreManagementSystem {

    static Scanner stdin = new Scanner(System.in);

    static Connection con = null;

    static CallableStatement cs = null;
    
    public static void main(String[] args) {
        int func;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    "server", "username", "password");

            do {
                System.out.println();
                System.out.println("[CS560] Bookstore management system");
                System.out.println("----------------------------------");
                System.out.println("[0] Quit");
                System.out.println("[1] Print authors");
                System.out.println("[2] Print books");
                System.out.println("[3] Add a new author");
                System.out.println("[4] Add a new book");
                System.out.println("[5] Restock book");
                System.out.println("[6] Search books with title");
                System.out.println("[7] Sell book");
                System.out.println("[8] Delete book");
                System.out.println("----------------------------------");
                System.out.print("Command >> ");
                func = stdin.nextInt();
                stdin.nextLine();
                switch (func) {
                case 0:
                    System.out.println("Bye~!");
                    break;
                case 1:
                	function1_print_authors();
                    break;
                case 2:
                    function2_print_books();
                    break;
                case 3:
                    function3_add_author();
                    break;
                case 4:
                    function4_add_book();
                    break;
                case 5: 
                    function5_restock_book();
                    break;
                case 6:
                    function6_search_book();
                    break;
                case 7:
                    function7_sell_book();
                    break;
                case 8:
                    function8_delete_book();
                    break;
                default:
                    System.out.println("Wrong input. Try again!");
                }
            } while (func != 0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function1_print_authors() {
        // TODO 
        int authorid, numbooks;
        String gender,name;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exist = false;

        System.out.println("Print authors");
        System.out.println("[AUTHORS Table]");

        System.out.println();
        System.out.printf("%-4s   %-30s   %-10s  %-5s %n", "ID", "NAME", "GENDER", "NUM_OF_BOOKS");
        System.out.println("------------------------------------------------------------");

        try {
            stmt = con.prepareStatement("SELECT id, au.name.firstname, au.name.lastname, gender, numofbooks FROM authortable au ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                authorid = rs.getInt(1);                
                name = rs.getString(2) + " " + rs.getString(3);
                gender = rs.getString(4);
                numbooks = rs.getInt(5);
                System.out.printf("%-4d   %-30s   %-10s  %-5d %n", authorid, name, gender, numbooks);
                exist = true;
            }
            
            if (!exist) 
                System.out.println("There is no author.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function2_print_books() {
        // TODO 
        int bookid, stock, price;
        String title, author, publisher;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exist = false;

        System.out.println("Print books");
        System.out.println("[BOOKS Table]");

        System.out.println();
        System.out.printf("%-4s   %-30s   %-20s   %-15s  %-5s  %-5s %n", "ID", "TITLE", "AUTHOR", "PUBLISHER", "STOCK", "PRICE");
        System.out.println("------------------------------------------------------------");

        try {
            stmt = con.prepareStatement("SELECT id, title, deref(author).name.firstname, deref(author).name.lastname, publisher, stock, price FROM booktable ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                bookid = rs.getInt(1);
                title = rs.getString(2);
                author = rs.getString(3) + " " + rs.getString(4);;
                publisher = rs.getString(5);
                stock = rs.getInt(6);
                price = rs.getInt(7);
                System.out.printf("%-4d   %-30s   %-20s   %-15s  %-5d  %-5d %n", bookid, title, author, publisher, stock, price);
                exist = true;
            }
            
            if (!exist) 
                System.out.println("There is no book.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function3_add_author() {
        // TODO 
        int authorid, numbooks, maxid;
        String gender,name, newfirstname, newlastname, newgender;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("Add a new author");
        System.out.print("First Name : ");
        newfirstname = stdin.nextLine();
        System.out.print("Last name : ");
        newlastname = stdin.nextLine();
        System.out.print("Gender : ");
        newgender = stdin.nextLine();
                
        System.out.println("[AUTHORS Table]");

        System.out.println();
        System.out.printf("%-4s   %-30s   %-10s  %-5s %n", "ID", "NAME", "GENDER", "NUM_OF_BOOKS");
        System.out.println("------------------------------------------------------------");

        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement("SELECT MAX(id) FROM authortable");
            rs = stmt.executeQuery();
            
            if (rs.next())
                maxid = rs.getInt(1) + 1;
            else
                maxid = 1;
            
            stmt = con.prepareStatement("INSERT INTO authortable VALUES(?,NameType(?,?),?,?)");
            stmt.setInt(1, maxid);
            stmt.setString(2, newfirstname);
            stmt.setString(3, newlastname);
            stmt.setString(4, newgender);
            stmt.setInt(5,0);
            stmt.executeUpdate();
            
            stmt = con.prepareStatement("SELECT id, au.name.firstname, au.name.lastname, gender, numofbooks FROM authortable au ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                authorid = rs.getInt(1);                
                name = rs.getString(2) + " " + rs.getString(3);
                gender = rs.getString(4);
                numbooks = rs.getInt(5);
                System.out.printf("%-4d   %-30s   %-10s  %-5d %n", authorid, name, gender, numbooks);
            }
            con.commit();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                con.rollback();
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function4_add_book() {
        // TODO 
        int authorid, numbooks, maxid,bookid, stock, price,newid,newprice,newstock;
        String gender,name,title, author, publisher,newtitle,newpublisher;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("Add a new book");
        System.out.print("Title : ");
        newtitle = stdin.nextLine();
        System.out.print("Author's ID : ");
        newid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("Publisher : ");
        newpublisher = stdin.nextLine();
        System.out.print("Stock : ");
        newstock = stdin.nextInt(); 
        stdin.nextLine();
        System.out.print("Price : ");
        newprice = stdin.nextInt();
        stdin.nextLine();
                

        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement("SELECT MAX(id) FROM booktable");
            rs = stmt.executeQuery();
            
            if (rs.next())
                maxid = rs.getInt(1) + 1;
            else
                maxid = 1;
            
            stmt = con.prepareStatement("INSERT INTO booktable VALUES(?,?,(SELECT REF(au) FROM AuthorTable au WHERE au.id=?),?,?,?)");
            stmt.setInt(1, maxid);
            stmt.setString(2, newtitle);
            stmt.setInt(3,newid);
            stmt.setString(4, newpublisher);
            stmt.setInt(5,newstock);
            stmt.setInt(6,newprice);
            stmt.executeUpdate();
            
            
            stmt = con.prepareStatement("UPDATE authortable SET numofbooks = numofbooks + 1 WHERE id = ?");
            stmt.setInt(1, newid);
            stmt.executeUpdate();
            
            
            System.out.println();
            System.out.println("[BOOKS Table]");
            System.out.printf("%-4s   %-30s   %-20s   %-15s  %-5s  %-5s %n", "ID", "TITLE", "AUTHOR", "PUBLISHER", "STOCK", "PRICE");
            System.out.println("------------------------------------------------------------");
            
            stmt = con.prepareStatement("SELECT id, title, deref(author).name.firstname, deref(author).name.lastname, publisher, stock, price FROM booktable ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                bookid = rs.getInt(1);
                title = rs.getString(2);
                author = rs.getString(3) + " " + rs.getString(4);;
                publisher = rs.getString(5);
                stock = rs.getInt(6);
                price = rs.getInt(7);
                System.out.printf("%-4d   %-30s   %-20s   %-15s  %-5d  %-5d %n", bookid, title, author, publisher, stock, price);
            }
            

            System.out.println();
            System.out.println("[AUTHORS Table]");
            System.out.printf("%-4s   %-30s   %-10s  %-5s %n", "ID", "NAME", "GENDER", "NUM_OF_BOOKS");
            System.out.println("------------------------------------------------------------");
            
            stmt = con.prepareStatement("SELECT id, au.name.firstname, au.name.lastname, gender, numofbooks FROM authortable au ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                authorid = rs.getInt(1);                
                name = rs.getString(2) + " " + rs.getString(3);
                gender = rs.getString(4);
                numbooks = rs.getInt(5);
                System.out.printf("%-4d   %-30s   %-10s  %-5d %n", authorid, name, gender, numbooks);
            }
            con.commit();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                con.rollback();
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function5_restock_book() {
        // TODO 
        int bookid, stock, price,newid,newstock;
        String title, author, publisher;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("Restock book");
        System.out.print("Book ID : ");
        newid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("Restock : ");
        newstock = stdin.nextInt(); 
        stdin.nextLine();
                

        try {
            stmt = con.prepareStatement("UPDATE booktable SET stock = stock + ? WHERE id = ?");
            stmt.setInt(1, newstock);
            stmt.setInt(2, newid);
            stmt.executeUpdate();
            
            
            System.out.println();
            System.out.println("[BOOKS Table]");
            System.out.printf("%-4s   %-30s   %-20s   %-15s  %-5s  %-5s %n", "ID", "TITLE", "AUTHOR", "PUBLISHER", "STOCK", "PRICE");
            System.out.println("------------------------------------------------------------");
            
            stmt = con.prepareStatement("SELECT id, title, deref(author).name.firstname, deref(author).name.lastname, publisher, stock, price FROM booktable ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                bookid = rs.getInt(1);
                title = rs.getString(2);
                author = rs.getString(3) + " " + rs.getString(4);;
                publisher = rs.getString(5);
                stock = rs.getInt(6);
                price = rs.getInt(7);
                System.out.printf("%-4d   %-30s   %-20s   %-15s  %-5d  %-5d %n", bookid, title, author, publisher, stock, price);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());           
            
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function6_search_book() {
        // TODO 
        int bookid, stock, price;
        String title, author, publisher,keyword;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exist = false;
        
        System.out.println("Search Books with tile");
        System.out.print("> Keyword : ");
        keyword = stdin.nextLine();
                

        try {            
            System.out.println();
            System.out.println("[BOOKS Table]");
            System.out.printf("%-4s   %-30s   %-20s   %-15s  %-5s  %-5s %n", "ID", "TITLE", "AUTHOR", "PUBLISHER", "STOCK", "PRICE");
            System.out.println("------------------------------------------------------------");
            
            stmt = con.prepareStatement("SELECT id, title, deref(author).name.firstname, deref(author).name.lastname, publisher, stock, price FROM booktable WHERE title like '%" + keyword + "%' ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                bookid = rs.getInt(1);
                title = rs.getString(2);
                author = rs.getString(3) + " " + rs.getString(4);;
                publisher = rs.getString(5);
                stock = rs.getInt(6);
                price = rs.getInt(7);
                System.out.printf("%-4d   %-30s   %-20s   %-15s  %-5d  %-5d %n", bookid, title, author, publisher, stock, price);
                exist = true;
            }

            if (!exist) 
                System.out.println("There is no book with given keyword.");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
           
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function7_sell_book() {
        // TODO 
        int bookid, stock, price,newid,currentstock;
        String title, author, publisher;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("Sell book");
        System.out.print("Book ID : ");
        newid = stdin.nextInt();
        stdin.nextLine();                

        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement("SELECT stock FROM booktable WHERE id = ?");
            stmt.setInt(1, newid);
            rs = stmt.executeQuery();
            
            if (rs.next())
                currentstock = rs.getInt(1);
            else
                currentstock = 0;
            
            if (currentstock == 0)
                System.out.println("There is no enough number of books to sell.");
            else
            {
                stmt = con.prepareStatement("UPDATE booktable SET stock = stock - 1 WHERE id = ?");                
                stmt.setInt(1, newid);
                stmt.executeUpdate();
                
                
                System.out.println();
                System.out.println("[BOOKS Table]");
                System.out.printf("%-4s   %-30s   %-20s   %-15s  %-5s  %-5s %n", "ID", "TITLE", "AUTHOR", "PUBLISHER", "STOCK", "PRICE");
                System.out.println("------------------------------------------------------------");
                
                stmt = con.prepareStatement("SELECT id, title, deref(author).name.firstname, deref(author).name.lastname, publisher, stock, price FROM booktable ORDER BY id");
                rs = stmt.executeQuery();

                while (rs.next()) {                
                    bookid = rs.getInt(1);
                    title = rs.getString(2);
                    author = rs.getString(3) + " " + rs.getString(4);;
                    publisher = rs.getString(5);
                    stock = rs.getInt(6);
                    price = rs.getInt(7);
                    System.out.printf("%-4d   %-30s   %-20s   %-15s  %-5d  %-5d %n", bookid, title, author, publisher, stock, price);
                }
                con.commit();
            }
            

        } catch (Exception e) {
            System.out.println(e.getMessage());           
            try {
                con.rollback();
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function8_delete_book() {
        // TODO 
        int authorid, numbooks, bookid, stock, price,newid;
        String gender,name,title, author, publisher;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        System.out.println("Delete book");
        System.out.print("Book ID : ");
        newid = stdin.nextInt();
        stdin.nextLine();      
                

        try {
            con.setAutoCommit(false);            
            
            stmt = con.prepareStatement("UPDATE authortable SET numofbooks = numofbooks - 1 WHERE id = (SELECT DEREF(author).id FROM booktable WHERE id = ?)");
            stmt.setInt(1, newid);
            stmt.executeUpdate();
            
            stmt = con.prepareStatement("DELETE FROM booktable WHERE id = ?");
            stmt.setInt(1, newid);
            stmt.executeUpdate();
            
            
            System.out.println();
            System.out.println("[BOOKS Table]");
            System.out.printf("%-4s   %-30s   %-20s   %-15s  %-5s  %-5s %n", "ID", "TITLE", "AUTHOR", "PUBLISHER", "STOCK", "PRICE");
            System.out.println("------------------------------------------------------------");
            
            stmt = con.prepareStatement("SELECT id, title, deref(author).name.firstname, deref(author).name.lastname, publisher, stock, price FROM booktable ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                bookid = rs.getInt(1);
                title = rs.getString(2);
                author = rs.getString(3) + " " + rs.getString(4);;
                publisher = rs.getString(5);
                stock = rs.getInt(6);
                price = rs.getInt(7);
                System.out.printf("%-4d   %-30s   %-20s   %-15s  %-5d  %-5d %n", bookid, title, author, publisher, stock, price);
            }
            

            System.out.println();
            System.out.println("[AUTHORS Table]");
            System.out.printf("%-4s   %-30s   %-10s  %-5s %n", "ID", "NAME", "GENDER", "NUM_OF_BOOKS");
            System.out.println("------------------------------------------------------------");
            
            stmt = con.prepareStatement("SELECT id, au.name.firstname, au.name.lastname, gender, numofbooks FROM authortable au ORDER BY id");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                authorid = rs.getInt(1);                
                name = rs.getString(2) + " " + rs.getString(3);
                gender = rs.getString(4);
                numbooks = rs.getInt(5);
                System.out.printf("%-4d   %-30s   %-10s  %-5d %n", authorid, name, gender, numbooks);
            }
            con.commit();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                con.rollback();
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
}
