package com.fullDemo2.services.impl;

import com.fullDemo2.Entity.Branch;
import com.fullDemo2.dto.BranchDTO;

import java.util.List;

public interface BranchService {
    Branch ChangeBranchName(String oldName, String newName);

    Branch saveOrUpdate(BranchDTO dto);

    List<Branch> getBranch();

    List<Branch> getAllBranch(Integer pageNo , Integer pageSize, String sortBy );


    void deleteBranch(String bname);



}
