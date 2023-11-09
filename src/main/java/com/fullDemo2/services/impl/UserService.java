package com.fullDemo2.services.impl;


import com.fullDemo2.Entity.MyUser;
import com.fullDemo2.exception.domain.UserNotFoundException;
import com.fullDemo2.exception.domain.UsernameExistException;

import java.io.IOException;
import java.util.List;

public interface UserService {

    MyUser register(MyUser user) throws UserNotFoundException, UsernameExistException;

    List<MyUser> getUsers();

    MyUser findUserByUsername(String username);

    void deleteUser(String username) throws IOException;

}