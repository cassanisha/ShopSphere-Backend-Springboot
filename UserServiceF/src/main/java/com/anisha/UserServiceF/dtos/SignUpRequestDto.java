package com.anisha.UserServiceF.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String role;
    private String email;
    private String password;
}