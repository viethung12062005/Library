/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book.dao;

import book.model.Book;

import java.sql.Date;
import java.util.List;

public interface BookDao {

    List<Book> searchBook_Registe(String id, String title, String author, String publish, String category);
    
    List<Book> searchBook_Manager(String id, String title, String author, String publish, String category);
    
    int updateInforBook(String id, String title, String author, String publisher, String category);
    
    List<Book> getAllBook();

    boolean checkId(String id);

    String generateIdBook(String idCat);

    int addBook(String id, String title, String author, String idPub, String idCat);
    
    boolean checkBookSame(String id, String title, String author, String idPub, String idCat);

    List<Book> getAll();

    String getTypeBook(String id);

    int getStatusBookCopy(String idCopy);

    boolean createRegisteBook(int id, Date dateNow);

    boolean registeBook(int id, String author, String title);

    boolean updateStatusBook(String id);

    List<Book> getAllBookById(String id);

    int getMaxCountIdByBookId(String id);

    String getCatCodeById(String id);

    String getIdCatByCategory(String category);
    
    String getIdPubByPublisher(String publisher);
    
    int generateIdRegiste();

    int generateIdRegisteDetail();

    public String getCountBookById(String id);
}
