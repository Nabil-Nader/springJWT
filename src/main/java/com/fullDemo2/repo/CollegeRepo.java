package com.fullDemo2.repo;

import com.fullDemo2.Entity.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CollegeRepo extends JpaRepository<College,Long> {

    College findCollegeBycName(String cName);


}
