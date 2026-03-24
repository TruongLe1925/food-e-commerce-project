package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {
    private Set<Authority> authorities;
    private String username;
    private  String password;
    private String enable;
}
