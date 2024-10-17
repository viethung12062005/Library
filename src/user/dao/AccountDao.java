/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.dao;

import user.model.Account;


public interface AccountDao {
     boolean insert(Account acc);
     int login(Account acc);
     int changePass(Account account, String passNew, String passAgain);
     
}
