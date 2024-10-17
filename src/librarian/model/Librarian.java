/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarian.model;

import librarian.dao.LibrarianDao;
import librarian.dao.MysqlLibrarianDao;


public class Librarian {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Librarian(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    public Librarian() {
    }

    public Librarian(String id, String pass, String name, String email, String phone) {
        this.id = id;
        this.pass = pass;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    private String pass;
    private String name;
    private String email;
    private String phone;

    private static LibrarianDao libDao() {
        return MysqlLibrarianDao.getInstance();
    }

    public int login() {
        return libDao().loginWithLibrarian(this);
    }

    public int changePassLib(Librarian librarian, String passNew, String passAgain) {
        return libDao().changePassLib(librarian, passNew, passAgain);
    }

    public boolean checkIdExist(String idLib) {
        return libDao().checkIdExist(idLib);
    }

    public int changePass(String username, String newPass) {
        return libDao().chagePass(username, newPass);
    }

    public int save() {
        return libDao().insert(this);
    }
}
