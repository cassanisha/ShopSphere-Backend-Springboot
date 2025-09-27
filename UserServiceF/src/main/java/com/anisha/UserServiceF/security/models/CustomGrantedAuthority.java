package com.anisha.UserServiceF.security.models;

import com.anisha.UserServiceF.models.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;


@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;

    public CustomGrantedAuthority() {

    }

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getRole();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}