package com.fullDemo2.services.impl;

import com.fullDemo2.Entity.College;
import com.fullDemo2.dto.CollegeDTO;
import com.fullDemo2.repo.CollegeRepo;
import com.fullDemo2.services.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollegeImpl implements CollegeService {

    @Autowired
    private CollegeRepo collegeRepo;

    @Override
    public College ChangeCollegeName(String oldName, String newName){
        College changeBranchName = collegeRepo.findCollegeBycName(oldName);
        changeBranchName.setCName(newName);
        collegeRepo.save(changeBranchName);
        return changeBranchName;
    }

    @Override
    public College saveOrUpdate(CollegeDTO dto) {
        College college;
        college= collegeRepo.findCollegeBycName(dto.getCName());
        if(college != null) {

            return collegeRepo.save(college);
        }else {
            college = new College();
            college.setCName(dto.getCName());
            college.setUniversity_id(dto.getUniversity_id());
            college.setBranches(dto.getBranches());

//            System.out.println(branch.getBName());

            college = collegeRepo.save(college);

            return college;
        }

    }


    @Override
    public List<College> getCollege() {
        return collegeRepo.findAll();
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
    public void deleteCollege(String cname) {
        College college = collegeRepo.findCollegeBycName(cname);
        collegeRepo.delete(college);

    }
}
