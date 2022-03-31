package com.fullDemo2.enumeration;


import static com.fullDemo2.constant.Authority.*;

public enum Role {
    ROLE_STUDENT(STUDENT_AUTHORITIES),
    ROLE_EX_STUDENT(STUDENT_AUTHORITIES),
    ROLE_PRINCIPLE (MANAGER_AUTHORITIES),
    ROLE_HEAD(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);


    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
