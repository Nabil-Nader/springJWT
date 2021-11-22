package com.fullDemo2.services.impl;



import com.fullDemo2.Entity.Branch;
import com.fullDemo2.Entity.College;
import com.fullDemo2.Entity.MyUser;
import com.fullDemo2.Entity.University;
import com.fullDemo2.dto.UserDTO;
import com.fullDemo2.exception.domain.UserNotFoundException;
import com.fullDemo2.exception.domain.UsernameExistException;

import java.io.IOException;
import java.util.List;

public interface UserService {

    MyUser register(
            String name,
            String username,
            String role,
            String password,
            Long uId,
            Long cId,
            Long bId

    ) throws UserNotFoundException, UsernameExistException;


    void registerUser(UserDTO dto);
    void add(UserDTO dto);
    List<MyUser> getUsers();

    MyUser findUserByUsername(String username);

    MyUser findUserByEmail(String email);

    MyUser addNewUser(String name,
                      String username,
                      String role,
                      String password,
                      Long uId,
                      Long cId,
                      Long bId);



    void deleteUser(String username) throws IOException;


    MyUser findMyUserByBranch(String b_name,String username);
    MyUser findMyUserByCollage(String c_name,String username);
    MyUser findMyUserByUniversity(String u_name,String username);

    List<MyUser> findByBranchToCollege(String c_name , String b_name);
    List<MyUser> findStudentWithBranch(String b_name);

    List<MyUser> getAllUser(Integer pageNo ,Integer pageSize,String sortBy );
    List<College> getAllCollege(Integer pageNo ,Integer pageSize,String sortBy ) ;
    List<University> getAllUniversity(Integer pageNo , Integer pageSize, String sortBy );



}
