package com.fullDemo2.services.impl;

import com.fullDemo2.Entity.*;
import com.fullDemo2.dto.UserDTO;
import com.fullDemo2.enumeration.Role;
import com.fullDemo2.repo.BranchRepo;
import com.fullDemo2.repo.CollegeRepo;
import com.fullDemo2.repo.MyUserRepo;
import com.fullDemo2.repo.UniversityRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.fullDemo2.constant.UserImplConstant.FOUND_USER_BY_USERNAME;
import static com.fullDemo2.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private MyUserRepo userRepository;
    private UniversityRepo universityRepo;
    private BranchRepo branchRepo ;
    private CollegeRepo collegeRepo;
    private BCryptPasswordEncoder passwordEncoder ;

    @Autowired
    public UserServiceImpl(MyUserRepo userRepository, UniversityRepo universityRepo, BranchRepo branchRepo, CollegeRepo collegeRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.universityRepo = universityRepo;
        this.branchRepo = branchRepo;
        this.collegeRepo = collegeRepo;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findMyUserByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
//            user.setLastLoginDateDisplay(user.getLastLoginDate());
//            user.setLastLoginDate(new Date());
//            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    @Override
    public MyUser register(String name,
                           String username,
                           String role,
                           String password,
                           Long uId,
                           Long cId,
                           Long bId) {
        MyUser user = new MyUser();
        user.setUserId(generateUserId());

        user.setName(name);
        user.setUsername(username);
        user.setPassword(encodePassword(password));


        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());

        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        user.setUniversity_id(uId);
        user.setBranch_id(bId);
        user.setCollege_id(cId);



        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        return user;
    }

    @Override
    public MyUser addNewUser(String name,
                             String username,
                             String role,
                             String password,
                             Long uId,
                             Long cId,
                             Long bId){

        MyUser user = new MyUser();
        user.setUserId(generateUserId());
        user.setName(name);
        user.setUsername(username);
        user.setPassword(encodePassword(password));
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        user.setUniversity_id(uId);
        user.setBranch_id(bId);
        user.setCollege_id(cId);

        System.out.println("USer branch: "+user.getBranch_id());
        System.out.println("User univ: "+user.getUniversity_id());

        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        return user;
    }


    @Override
    public void registerUser(UserDTO dto) {
        MyUser user = new MyUser();
        user.setUserId(generateUserId());
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setPassword(encodePassword(dto.getPassword()));
        user.setRole(getRoleEnumName(dto.getRole()).name());
        user.setAuthorities(getRoleEnumName(dto.getRole()).getAuthorities());
        user.setUniversity_id(dto.getUniversity_id());
        user.setBranch_id(dto.getBranch_id());
        user.setCollege_id(dto.getCollege_id());

        userRepository.save(user);


    }

    @Override
    public void add(UserDTO dto) {
        MyUser user ;

        if(dto.getUserId() != null){
            user = userRepository.findMyUserByUsername(dto.getUsername());
        }else{
            user = new MyUser();
            user.setUserId(generateUserId());
            user.setName(dto.getName());
            user.setUsername(dto.getUsername());
            user.setPassword(encodePassword(dto.getPassword()));
            user.setRole(getRoleEnumName(dto.getRole()).name());
            user.setAuthorities(getRoleEnumName(dto.getRole()).getAuthorities());
            user.setUniversity_id(dto.getUniversity_id());
            user.setBranch_id(dto.getBranch_id());
            user.setCollege_id(dto.getCollege_id());
        }

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
    public MyUser findUserByEmail(String email) {
        return userRepository.findMyUserByUsername(email);
    }

    @Override
    public void deleteUser(String username) throws IOException {
        MyUser user = userRepository.findMyUserByName(username);

        userRepository.deleteById(user.getId());
    }

    @Override
    public MyUser findMyUserByBranch(String bname, String username) {
        Branch branch = branchRepo.findBranchBybName(bname);

        for(MyUser u : branch.getMyUsers()){
            String fondName = u.getName();
            if(u.getBranch_id() == branch.getId() && u.getUsername().equals(username)){

                return u;
            }

        }
        return null;

    }

    @Override
    public MyUser findMyUserByCollage(String cname, String username) {
        College college = collegeRepo.findCollegeBycName(cname);

        for(Branch b : college.getBranches()){

            MyUser u = findMyUserByBranch(b.getBName(),username);
            if(u != null){
                return u;
            }
        }
        return null;
    }

    @Override
    public MyUser findMyUserByUniversity(String uname, String username) {
        University university = universityRepo.findUniversityByuName(uname);

        for(College c : university.getColleges()){
            MyUser u = findMyUserByCollage(c.getCName(),username);
            if(u != null){
                return u;
            }
        }

        return null;
    }

    @Override
    public List<MyUser> findByBranchToCollege(String cname, String bname) {
        College college = collegeRepo.findCollegeBycName(cname);
        Branch branch = branchRepo.findBranchBybName(bname);

        List<MyUser>users = new ArrayList();

        for(MyUser u : branch.getMyUsers()) {

            if(u.getId()== branch.getId() && u.getId()==college.getId()){
                users.add(u);
            }
        }

        return users;

    }

    //    Get ALL entity With pagination

    @Override
    public List<MyUser> getAllUser(Integer pageNo ,Integer pageSize,String sortBy ){

        Pageable paging = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));

        Page<MyUser> pagedResult = userRepository.findAll(paging);

        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }else{
            return new ArrayList<MyUser>();
        }

    }



    @Override
    public List<College> getAllCollege(Integer pageNo ,Integer pageSize,String sortBy ){

        Pageable paging = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));

        Page<College> pagedResult = collegeRepo.findAll(paging);

        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }else{
            return new ArrayList<College>();
        }

    }


    @Override
    public List<University> getAllUniversity(Integer pageNo ,Integer pageSize,String sortBy ){

        Pageable paging = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));

        Page<University> pagedResult = universityRepo.findAll(paging);

        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }else{
            return new ArrayList<University>();
        }

    }

    @Override
    public List<MyUser> findStudentWithBranch(String b_name) {
        List<MyUser> users = new ArrayList<>() ;
        Branch b = branchRepo.findBranchBybName(b_name);
        List<MyUser> allUSer = userRepository.findAll();
        for(MyUser u : allUSer) {

            if(u.getBranch_id()== b.getId()){
                users.add(u);
            }
        }

        return users;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }




}


