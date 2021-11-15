package com.example.springDemo.service;


import com.example.springDemo.domain.User;
import com.example.springDemo.exception.domain.UserNotFoundException;
import com.example.springDemo.exception.domain.UsernameExistException;

 import java.io.IOException;
import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email,String password) throws UserNotFoundException, UsernameExistException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User addNewUser(String firstName,
                    String lastName,
                    String username,
                    String email,
                    String role,
                    String password);



    void deleteUser(String username) throws IOException;


}
