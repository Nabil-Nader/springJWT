package com.fullDemo2.services.impl;

import com.fullDemo2.Entity.University;
import com.fullDemo2.dto.UniversityDTO;
import com.fullDemo2.repo.UniversityRepo;
import com.fullDemo2.services.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniversityImpl implements UniversityService {

    @Autowired
    private UniversityRepo universityRepo;

    @Override
    public University ChangeUniversityName(String oldName, String newName){
        University changeUniversityName = universityRepo.findUniversityByuName(oldName);
        changeUniversityName.setUName(newName);
        universityRepo.save(changeUniversityName);
        return changeUniversityName;
    }

    @Override
    public University saveOrUpdate(UniversityDTO dto) {
        University university;
        university= universityRepo.findUniversityByuName(dto.getUName());
        if(university != null) {

            return universityRepo.save(university);
        }else {
            university = new University();
            university.setUName(dto.getUName());
            university.setColleges(dto.getColleges());

//            System.out.println(branch.getBName());

            university = universityRepo.save(university);

            return university;
        }

    }


    @Override
    public List<University> getUniversity() {
        return universityRepo.findAll();
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
    public void deleteUniversity(String cname) {
        University university = universityRepo.findUniversityByuName(cname);
        universityRepo.delete(university);

    }
}
