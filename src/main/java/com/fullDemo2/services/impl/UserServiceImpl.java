package com.fullDemo2.services.impl;

import com.fullDemo2.Entity.MyUser;
import com.fullDemo2.Entity.UserPrincipal;
import com.fullDemo2.repo.MyUserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

import static com.fullDemo2.constant.UserImplConstant.FOUND_USER_BY_USERNAME;
import static com.fullDemo2.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private MyUserRepo userRepository;


    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(MyUserRepo userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findMyUserByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {

            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    @Override
    public MyUser register(MyUser myUser
    ) {
        MyUser user = new MyUser();

        user.setName("name");
        user.setUsername(myUser.getUsername());
        user.setPassword(encodePassword(myUser.getPassword()));


        user.setRole(myUser.getRole());

        userRepository.save(user);
        return user;
    }

    @Override
    public List<MyUser> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public MyUser findUserByUsername(String username) {
        return userRepository.findMyUserByUsername(username);
    }


    @Override
    public void deleteUser(String username) throws IOException {
        MyUser user = userRepository.findMyUserByName(username);

        userRepository.deleteById(user.getId());
    }


    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}


