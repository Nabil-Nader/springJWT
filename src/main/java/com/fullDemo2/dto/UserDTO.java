package com.fullDemo2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {


    private String name ;
    private String username ;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String role; //ROLE_USER{ read, edit }, ROLE_ADMIN {delete}

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String[] authorities;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long university_id;

    @Column(name = "branch_id")

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long branch_id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long college_id;

    private String userId;


}
