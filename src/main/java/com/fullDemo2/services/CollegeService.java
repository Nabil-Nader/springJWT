package com.fullDemo2.services;

import com.fullDemo2.Entity.Branch;
import com.fullDemo2.Entity.College;
import com.fullDemo2.dto.BranchDTO;
import com.fullDemo2.dto.CollegeDTO;

import java.util.List;

public interface CollegeService {
    College ChangeCollegeName(String oldName, String newName);

    College saveOrUpdate(CollegeDTO dto);

    List<College> getCollege();

    List<College> getAllCollege(Integer pageNo , Integer pageSize, String sortBy );


    void deleteCollege(String cname);



}
