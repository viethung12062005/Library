/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.dao;

import user.model.User;

import java.util.List;


public interface UserDao {

    int insert(User user);

    List<User> getAll();

    User findById(String id);

    String getNameById(String id);

    List<User> searchUser(String keyword);
    
    int updateInforUser(User userUpdate);

    public int changePass(String id, String newPass);
}
