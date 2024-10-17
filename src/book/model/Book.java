/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book.model;

import book.dao.BookDao;
import book.dao.MysqlBookDao;
import category.model.Category;
import publisher.model.Publisher;
import BookCopy.BookCopy;
import java.util.List;


public class Book {

    private String id;



    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Category getCategory() {
        return category;
    }
    private String title;
    private String author;
    private Publisher publisher;
    private Category category;

    public static class BookBuilder {

        private String id;
        private List<BookCopy> listCopies;
        private String title;
        private String author;
        private Publisher publisher;
        private Category category;

        public BookBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public BookBuilder setListCopies(List<BookCopy> listCopies) {
            this.listCopies = listCopies;
            return this;
        }

        public BookBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public BookBuilder setPublisher(Publisher publisher) {
            this.publisher = publisher;
            return this;
        }

        public BookBuilder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }

    public BookDao bookDao() {
        return MysqlBookDao.getInstance();
    }



    public List<Book> searchBook_Registe(String idSearch, String titleSearch, String authorSearch, String publishSearch, String categorySearch) {
        return bookDao().searchBook_Registe(idSearch, titleSearch, authorSearch, publishSearch, categorySearch);
    }
    
    public List<Book> searchBook_Manager(String idSearch, String titleSearch, String authorSearch, String publishSearch, String categorySearch) {
        return bookDao().searchBook_Manager(idSearch, titleSearch, authorSearch, publishSearch, categorySearch);
    }
    
    public int updateInforBook(String id, String title, String author, String publisher, String category){
        return bookDao().updateInforBook(id, title, author, publisher, category);
    }
    
    public List<Book> getAllBook() {
        return bookDao().getAllBook();
    }

    public int addBook(String id, String title, String author, String idPub, String idCat) {
        return bookDao().addBook(id, title, author, idPub, idCat);
    }

    public String getCountBookById(String idBook) {
        return bookDao().getCountBookById(idBook);
    }

    public String generateIdBook(String idCat) {
        return bookDao().generateIdBook(idCat);
    }
    
    public boolean checkBookSame(String id, String title, String author, String idPub, String idCat){
        return bookDao().checkBookSame(id, title, author, idPub, idCat);
    }
    
    public String getIdPubByPublisher(String publisher) {
        return bookDao().getIdPubByPublisher(publisher);
    }
    
    public String getIdCatByCategory(String category) {
        return bookDao().getIdCatByCategory(category);
    }
}
