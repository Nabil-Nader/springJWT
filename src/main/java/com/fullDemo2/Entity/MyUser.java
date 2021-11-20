package com.fullDemo2.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter @Setter
public class MyUser  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id ;

    private String name ;
    private String username ;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String role; //ROLE_USER{ read, edit }, ROLE_ADMIN {delete}

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String[] authorities;


    @Column(name = "university_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long university_id;

    @Column(name = "branch_id")

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long branch_id;


    @Column(name = "college_id")

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long college_id;

    private String userId;








}
