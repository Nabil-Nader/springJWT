package com.fullDemo2.dto;

import com.fullDemo2.Entity.College;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

public class UniversityDTO {


    private String uName;

    private Set<College> colleges ;


}
