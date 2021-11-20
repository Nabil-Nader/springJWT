package com.fullDemo2.repo;

import com.fullDemo2.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepo extends JpaRepository<MyUser,Long> {

    MyUser findMyUserByName(String name);

    MyUser findMyUserByUsername(String username);


}
