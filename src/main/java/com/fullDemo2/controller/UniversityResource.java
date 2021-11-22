package com.fullDemo2.controller;

import com.fullDemo2.Entity.College;
import com.fullDemo2.Entity.University;
import com.fullDemo2.dto.CollegeDTO;
import com.fullDemo2.dto.UniversityDTO;
import com.fullDemo2.exception.HttpResponse;
import com.fullDemo2.services.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping(path = "/university")
public class UniversityResource {

    public static final String College_DELETED_SUCCESSFULLY = "branch deleted successfully";


    @Autowired
    private UniversityService universityService;

    @PutMapping("/change/College-name")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<University> changeName(@RequestParam("oldName")String oldName,@RequestParam("newName")String newName){
        University changeUniversityName = universityService.ChangeUniversityName(oldName, newName);

        return new ResponseEntity<>(changeUniversityName,HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<University> updateUniversity(UniversityDTO dto){

        University b = universityService.saveOrUpdate(dto);

        return new ResponseEntity<>(b,HttpStatus.ACCEPTED);

    }

    @GetMapping("/get-university")
    public ResponseEntity<List<University>> getAll(){
        List<University> universityList=  universityService.getUniversity();

        return new ResponseEntity<>(universityList,HttpStatus.OK);
    }

    @PostMapping("/add")
     public  ResponseEntity<University> addCollege(@RequestBody UniversityDTO dto){

        University c = universityService.saveOrUpdate(dto);

         return new ResponseEntity<>(c,HttpStatus.CREATED);
    }


    @GetMapping("universities")
    public ResponseEntity<List<University>> getAllUniversities(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<University> list = universityService.getAllUniversity(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<University>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpResponse> deleteBranch(@RequestParam("bname")String bname){
        universityService.deleteUniversity(bname);
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


