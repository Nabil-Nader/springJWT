package com.fullDemo2.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullDemo2.Entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CollegeDTO {

    private String cName ;


    private Set<Branch> branches ;

    @Column(name = "university_id")
    
    private  Long university_id ;


}
