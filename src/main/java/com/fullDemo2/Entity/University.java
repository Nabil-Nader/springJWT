package com.fullDemo2.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter @Setter
public class University implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id ;

    private String uName;


    @OneToMany( fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id",referencedColumnName = "id")
    private Set<College> colleges ;






}
