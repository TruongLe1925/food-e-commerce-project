package com.myproject.e_commerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name= "password")
    private String password;
    @Column(name = "enabled")
    private boolean enabled;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "user")
    private Set<Authority> authorities;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "user")
    private CustomerDetails customerDetails;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", authorities=" + authorities +
                ", customerDetails=" + customerDetails +
                '}';
    }
}
