/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registeterm.model;


public class RegisterTermDetail {

    public RegisterTermDetail(int id, String idCopy, String title) {
        this.id = id;
        this.idCopy = idCopy;
        this.title = title;
    }

    public RegisterTermDetail() {
    }
    private int id; 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCopy() {
        return idCopy;
    }

    public void setIdCopy(String idCopy) {
        this.idCopy = idCopy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    private String idCopy;
    private String title;
}
