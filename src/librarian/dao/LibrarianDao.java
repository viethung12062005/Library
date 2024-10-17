/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarian.dao;

import librarian.model.Librarian;


public interface LibrarianDao {

    int loginWithLibrarian(Librarian lib);

    int changePassLib(Librarian lib, String passNew, String passAgain);

    public boolean checkIdExist(String idLib);

    int chagePass(String id, String pass);

    int insert(Librarian aThis);
}
