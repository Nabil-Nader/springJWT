package com.fullDemo2.repo;

import com.fullDemo2.Entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UniversityRepo extends JpaRepository<University,Long> {

    University findUniversityByuName(String uName);
    University findUniversityById(Long id);

}
