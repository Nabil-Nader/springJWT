package com.fullDemo2.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter@Setter
public class College implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id ;

    private String cName ;

    @OneToMany( fetch = FetchType.EAGER)
    @JoinColumn(name = "college_id",referencedColumnName = "id")
    private Set<Branch> branches ;

    @Column(name = "university_id")
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private  Long university_id ;


}
