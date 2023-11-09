package com.fullDemo2.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class MyUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String [] role;


}
