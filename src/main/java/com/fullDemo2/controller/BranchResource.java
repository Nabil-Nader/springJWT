package com.fullDemo2.controller;

import com.fullDemo2.Entity.Branch;
import com.fullDemo2.dto.BranchDTO;
import com.fullDemo2.exception.HttpResponse;
import com.fullDemo2.services.impl.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping(path = "/branch")
public class BranchResource {

    public static final String BRANCH_DELETED_SUCCESSFULLY = "branch deleted successfully";


    @Autowired
    private BranchService branchRepo ;

    @PutMapping("/change/branch-name")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<Branch> changeName(@RequestParam("oldName")String oldName,@RequestParam("newName")String newName){
        Branch changeBranchName = branchRepo.ChangeBranchName(oldName, newName);

        return new ResponseEntity<>(changeBranchName,HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Branch> updateBranch(BranchDTO dto){

        Branch b = branchRepo.saveOrUpdate(dto);

        return new ResponseEntity<>(b,HttpStatus.ACCEPTED);

    }

    @GetMapping("/get-branches")
    public ResponseEntity<List<Branch>> getAll(){
        List<Branch> branches=  branchRepo.getBranch();

        return new ResponseEntity<>(branches,HttpStatus.OK);
    }

    @PostMapping("/add")
     public  ResponseEntity<Branch> addBranch(@RequestBody BranchDTO dto){

        Branch b = branchRepo.saveOrUpdate(dto);

         return new ResponseEntity<>(b,HttpStatus.CREATED);
    }


    @GetMapping("branchs")
    public ResponseEntity<List<Branch>> getAllBranchs(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<Branch> list = branchRepo.getAllBranch(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<Branch>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpResponse> deleteBranch(@RequestParam("bname")String bname){
        branchRepo.deleteBranch(bname);
        return response(OK, BRANCH_DELETED_SUCCESSFULLY);

    }



    private class BranchNames {
        String oldName;
        String newName;
    }



    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus,
                                                  String message) {
        return new ResponseEntity<>(new
                HttpResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}


