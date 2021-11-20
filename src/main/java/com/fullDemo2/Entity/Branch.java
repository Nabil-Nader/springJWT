package com.fullDemo2.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter @Setter
public class Branch implements Serializable {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String bName ;

    @OneToMany( fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id",referencedColumnName = "id")

    private Set<MyUser> myUsers ;

    @Column(name = "college_id")
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long college_id;




}
