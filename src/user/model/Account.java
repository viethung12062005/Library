/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.model;

import user.dao.AccountDao;
import user.dao.MysqlAccountDao;

import java.sql.SQLException;


public class Account {

    private String username;

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    private String password;

    public boolean save() {
        return accountDao().insert(this);
    }

    private static AccountDao accountDao() {
        return  MysqlAccountDao.getInstance();
    }

    public int login() throws SQLException {
        return accountDao().login(this);
    }
    
    public int changePass(Account account, String passNew, String passAgain){
        return accountDao().changePass(account, passNew, passAgain);
    }
}
