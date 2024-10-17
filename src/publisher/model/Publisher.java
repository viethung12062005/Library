/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publisher.model;

import publisher.dao.MysqlPublisherDao;
import publisher.dao.PublisherDao;

import java.util.List;


public class Publisher {

    String id;

    public Publisher() {
    }

    public Publisher(String id, String publisher) {
        this.id = id;
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    String publisher;
        
    public List<Publisher> getAll(){
        return getPub().getAll();
    }
    
    private PublisherDao getPub() {
        return MysqlPublisherDao.getIntance();
    }
}
