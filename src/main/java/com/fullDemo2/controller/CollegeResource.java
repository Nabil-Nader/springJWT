package com.fullDemo2.controller;

import com.fullDemo2.Entity.Branch;
import com.fullDemo2.Entity.College;
import com.fullDemo2.dto.BranchDTO;
import com.fullDemo2.dto.CollegeDTO;
import com.fullDemo2.exception.HttpResponse;
import com.fullDemo2.repo.CollegeRepo;
import com.fullDemo2.services.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping(path = "/college")
public class CollegeResource {

    public static final String College_DELETED_SUCCESSFULLY = "branch deleted successfully";


    @Autowired
    private CollegeService collegeRepo;

    @PutMapping("/change/College-name")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<College> changeName(@RequestParam("oldName")String oldName,@RequestParam("newName")String newName){
        College changeBranchName = collegeRepo.ChangeCollegeName(oldName, newName);

        return new ResponseEntity<>(changeBranchName,HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<College> updateBranch(CollegeDTO dto){

        College b = collegeRepo.saveOrUpdate(dto);

        return new ResponseEntity<>(b,HttpStatus.ACCEPTED);

    }

    @GetMapping("/get-Colleges")
    public ResponseEntity<List<College>> getAll(){
        List<College> colleges=  collegeRepo.getCollege();

        return new ResponseEntity<>(colleges,HttpStatus.OK);
    }

    @PostMapping("/add")
     public  ResponseEntity<College> addCollege(@RequestBody CollegeDTO dto){

        College c = collegeRepo.saveOrUpdate(dto);

         return new ResponseEntity<>(c,HttpStatus.CREATED);
    }


    @GetMapping("Colleges")
    public ResponseEntity<List<College>> getAllBranchs(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<College> list = collegeRepo.getAllCollege(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<College>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpResponse> deleteBranch(@RequestParam("bname")String bname){
        collegeRepo.deleteCollege(bname);
        return response(OK, College_DELETED_SUCCESSFULLY);

    }



    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus,
                                                  String message) {
        return new ResponseEntity<>(new
                HttpResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}


