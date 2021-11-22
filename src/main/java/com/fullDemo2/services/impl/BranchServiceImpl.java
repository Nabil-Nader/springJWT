package com.fullDemo2.services.impl;

import com.fullDemo2.Entity.Branch;
import com.fullDemo2.dto.BranchDTO;
import com.fullDemo2.repo.BranchRepo;
import com.fullDemo2.services.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepo branchRepo ;

    @Override
    public Branch ChangeBranchName(String oldName, String newName){
        Branch changeBranchName = branchRepo.findBranchBybName(oldName);
        changeBranchName.setBName(newName);
        branchRepo.save(changeBranchName);
        return changeBranchName;
    }

    @Override
    public Branch saveOrUpdate(BranchDTO dto) {
        Branch branch;
        branch= branchRepo.findBranchBybName(dto.getBName());
        if(branch != null) {

            return branchRepo.save(branch);
        }else {
            branch = new Branch();
            branch.setBName(dto.getBName());
            branch.setCollege_id(dto.getCollege_id());
             branch.setMyUsers(dto.getMyUsers());

//            System.out.println(branch.getBName());

        branch = branchRepo.save(branch);

            return branch;
        }

    }


    @Override
    public List<Branch> getBranch() {
        return branchRepo.findAll();
    }


    @Override
    public List<Branch> getAllBranch(Integer pageNo ,Integer pageSize,String sortBy ){

        Pageable paging = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));

        Page<Branch> pagedResult = branchRepo.findAll(paging);

        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }else{
            return new ArrayList<Branch>();
        }

    }

    @Override
    public void deleteBranch(String bname) {
        Branch b = branchRepo.findBranchBybName(bname);
        branchRepo.delete(b);

    }
}
