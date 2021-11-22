package com.fullDemo2.services;

import com.fullDemo2.Entity.College;
import com.fullDemo2.Entity.University;
import com.fullDemo2.dto.CollegeDTO;
import com.fullDemo2.dto.UniversityDTO;

import java.util.List;

public interface UniversityService {
    University ChangeUniversityName(String oldName, String newName);

    University saveOrUpdate(UniversityDTO dto);

    List<University> getUniversity();

    List<University> getAllUniversity(Integer pageNo , Integer pageSize, String sortBy );


    void deleteUniversity(String cname);



}
